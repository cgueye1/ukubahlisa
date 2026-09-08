package com.wakana.realestateworks.services.impl.v2;

import com.wakana.realestateworks.dto.v2.LeaveRequestDto;
import com.wakana.realestateworks.dto.v2.LeaveReviewDto;
import com.wakana.realestateworks.dto.v2.response.LeaveBalanceSummaryDto;
import com.wakana.realestateworks.dto.v2.response.LeaveRequestResponseDto;
import com.wakana.realestateworks.dto.v2.response.PagedLeaveRequestDto;
import com.wakana.realestateworks.enums.LeaveRequestStatus;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.model.v2.LeaveBalance;
import com.wakana.realestateworks.model.v2.LeaveRequest;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.repository.v2.LeaveBalanceRepository;
import com.wakana.realestateworks.repository.v2.LeaveRequestRepository;
import com.wakana.realestateworks.services.v2.LeaveService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final RealEstatePropertyRepository realEstatePropertyRepository;

    // ── Accrual ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void accrueMonthlyLeaveForWorkers(Long realEstateId) {
        accrueForList(userRepository.findWorkersByRealEstateId(realEstateId));
    }

    @Override
    @Transactional
    public void accrueMonthlyLeaveForAllWorkers() {
        accrueForList(userRepository.findAllWorkers());
    }

    private void accrueForList(List<User> workers) {
        for (User worker : workers) {
            LeaveBalance b = leaveBalanceRepository.findByWorker(worker)
                    .orElseGet(() -> createInitialBalance(worker));
            b.setTotalAccruedDays(b.getTotalAccruedDays() + 1.0);
            leaveBalanceRepository.save(b);
        }
    }

    // ── Summary (last 10) ────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public LeaveBalanceSummaryDto getWorkerLeaveSummary(Long realEstateId, Long workerId) {
        assertWorkerBelongsToProperty(realEstateId, workerId);
        User worker = findUserOrThrow(workerId);
        LeaveBalance balance = leaveBalanceRepository.findByWorker(worker)
                .orElseGet(() -> createInitialBalance(worker));

        List<LeaveRequest> last10 = leaveRequestRepository
                .findTop10ByWorkerIdAndRealEstatePropertyIdOrderByCreatedAtDesc(workerId, realEstateId);

        return LeaveBalanceSummaryDto.builder()
                .workerId(worker.getId())
                .workerFirstName(worker.getPrenom())
                .workerLastName(worker.getNom())
                .totalAccruedDays(balance.getTotalAccruedDays())
                .usedDays(balance.getUsedDays())
                .pendingDays(balance.getPendingDays())
                .remainingDays(balance.getRemainingDays())
                .history(last10.stream().map(this::toResponseDto).collect(Collectors.toList()))
                .build();
    }

    // ── Submit ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public LeaveRequestResponseDto submitLeaveRequest(Long realEstateId, Long workerId, LeaveRequestDto dto) {
        assertWorkerBelongsToProperty(realEstateId, workerId);
        User worker = findUserOrThrow(workerId);
        RealEstateProperty property = findPropertyOrThrow(realEstateId);

        if (dto.getStartDate().isAfter(dto.getEndDate()))
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        if (dto.getStartDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");

        if (leaveRequestRepository.existsOverlappingRequest(workerId, realEstateId, dto.getStartDate(),
                dto.getEndDate()))
            throw new IllegalStateException("Une demande chevauche déjà cette période sur ce chantier");

        double requestedDays = countWorkingDays(dto.getStartDate(), dto.getEndDate());
        LeaveBalance balance = leaveBalanceRepository.findByWorker(worker)
                .orElseGet(() -> createInitialBalance(worker));

        if (balance.getRemainingDays() < requestedDays)
            throw new IllegalStateException(
                    String.format("Solde insuffisant. Disponible: %.1f, Demandé: %.1f",
                            balance.getRemainingDays(), requestedDays));

        LeaveRequest request = new LeaveRequest();
        request.setWorker(worker);
        request.setRealEstateProperty(property);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setRequestedDays(requestedDays);
        request.setReason(dto.getReason());
        request.setStatus(LeaveRequestStatus.PENDING);
        leaveRequestRepository.save(request);

        balance.setPendingDays(balance.getPendingDays() + requestedDays);
        leaveBalanceRepository.save(balance);

        return toResponseDto(request);
    }

    // ── Review ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public LeaveRequestResponseDto reviewLeaveRequest(Long realEstateId, Long requestId,
            Long managerId, LeaveReviewDto reviewDto) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable : " + requestId));

        if (!request.getRealEstateProperty().getId().equals(realEstateId))
            throw new IllegalArgumentException("Cette demande n'appartient pas à la propriété " + realEstateId);
        if (request.getStatus() != LeaveRequestStatus.PENDING)
            throw new IllegalStateException("Cette demande a déjà été traitée");

        LeaveRequestStatus decision;
        try {
            decision = LeaveRequestStatus.valueOf(reviewDto.getDecision().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Décision invalide. Valeurs : APPROVED, REJECTED");
        }

        if (decision == LeaveRequestStatus.REJECTED
                && (reviewDto.getManagerComment() == null || reviewDto.getManagerComment().isBlank()))
            throw new IllegalArgumentException("Un motif de refus est obligatoire");

        User manager = findUserOrThrow(managerId);
        LeaveBalance balance = leaveBalanceRepository.findByWorker(request.getWorker())
                .orElseThrow(() -> new IllegalStateException("Solde introuvable"));

        double days = request.getRequestedDays();
        if (decision == LeaveRequestStatus.APPROVED) {
            balance.setPendingDays(Math.max(0, balance.getPendingDays() - days));
            balance.setUsedDays(balance.getUsedDays() + days);
        } else {
            balance.setPendingDays(Math.max(0, balance.getPendingDays() - days));
        }

        leaveBalanceRepository.save(balance);
        request.setStatus(decision);
        request.setManagerComment(reviewDto.getManagerComment());
        request.setReviewedBy(manager);
        request.setReviewedAt(LocalDateTime.now());
        leaveRequestRepository.save(request);

        return toResponseDto(request);
    }

    // ── Paginated search with Specification ──────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PagedLeaveRequestDto getLeaveRequests(Long realEstateId, String statusStr,
            String search, Pageable pageable) {
        LeaveRequestStatus status = null;
        if (statusStr != null && !statusStr.isBlank()) {
            try {
                status = LeaveRequestStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Statut invalide. Valeurs acceptées : PENDING, APPROVED, REJECTED");
            }
        }

        Specification<LeaveRequest> spec = Specification
                .where(LeaveRequestSpecification.byRealEstate(realEstateId))
                .and(LeaveRequestSpecification.byStatus(status))
                .and(LeaveRequestSpecification.byWorkerSearch(search));

        Page<LeaveRequest> page = leaveRequestRepository.findAll(spec, pageable);

        return PagedLeaveRequestDto.builder()
                .content(page.getContent().stream().map(this::toResponseDto).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static double countWorkingDays(LocalDate start, LocalDate end) {
        long count = 0;
        LocalDate d = start;
        while (!d.isAfter(end)) {
            DayOfWeek dow = d.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY)
                count++;
            d = d.plusDays(1);
        }
        return count;
    }

    private void assertWorkerBelongsToProperty(Long realEstateId, Long workerId) {
        boolean belongs = userRepository.findWorkersByRealEstateId(realEstateId)
                .stream().anyMatch(u -> u.getId().equals(workerId));
        if (!belongs)
            throw new IllegalArgumentException(
                    "Le worker " + workerId + " n'est pas assigné à la propriété " + realEstateId);
    }

    private LeaveBalance createInitialBalance(User worker) {
        LeaveBalance b = new LeaveBalance();
        b.setWorker(worker);
        b.setTotalAccruedDays(0);
        b.setUsedDays(0);
        b.setPendingDays(0);
        return leaveBalanceRepository.save(b);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + id));
    }

    private RealEstateProperty findPropertyOrThrow(Long id) {
        return realEstatePropertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable : " + id));
    }

    private LeaveRequestResponseDto toResponseDto(LeaveRequest r) {
        return LeaveRequestResponseDto.builder()
                .id(r.getId())
                .workerId(r.getWorker().getId())
                .workerFirstName(r.getWorker().getPrenom())
                .workerLastName(r.getWorker().getNom())
                .realEstateId(r.getRealEstateProperty().getId())
                .realEstateName(r.getRealEstateProperty().getName())
                .startDate(r.getStartDate())
                .endDate(r.getEndDate())
                .requestedDays(r.getRequestedDays())
                .reason(r.getReason())
                .status(r.getStatus())
                .managerComment(r.getManagerComment())
                .reviewedByName(r.getReviewedBy() != null
                        ? r.getReviewedBy().getPrenom() + " " + r.getReviewedBy().getNom()
                        : null)
                .reviewedAt(r.getReviewedAt())
                .createdAt(r.getCreatedAt())
                .build();
    }
}

/*
 * @Slf4j
 * 
 * @Service
 * 
 * @RequiredArgsConstructor
 * public class LeaveServiceImpl implements LeaveService {
 * 
 * private final LeaveBalanceRepository leaveBalanceRepository;
 * private final LeaveRequestRepository leaveRequestRepository;
 * private final UserRepository userRepository;
 * private final RealEstatePropertyRepository realEstatePropertyRepository;
 * 
 * // -------------------------------------------------------------------------
 * // Accrual — scoped to one property
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional
 * public void accrueMonthlyLeaveForWorkers(Long realEstateId) {
 * log.info("Leave accrual triggered for real estate {}", realEstateId);
 * List<User> workers = userRepository.findWorkersByRealEstateId(realEstateId);
 * accrueForList(workers);
 * log.info("Leave accrual done: {} workers on property {}", workers.size(),
 * realEstateId);
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Accrual — global (called by scheduler)
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional
 * public void accrueMonthlyLeaveForAllWorkers() {
 * log.info("Global monthly leave accrual started");
 * List<User> workers = userRepository.findAllWorkers();
 * accrueForList(workers);
 * log.info("Global monthly leave accrual done: {} workers", workers.size());
 * }
 * 
 * private void accrueForList(List<User> workers) {
 * for (User worker : workers) {
 * LeaveBalance balance = leaveBalanceRepository.findByWorker(worker)
 * .orElseGet(() -> createInitialBalance(worker));
 * balance.setTotalAccruedDays(balance.getTotalAccruedDays() + 1.0);
 * leaveBalanceRepository.save(balance);
 * }
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Summary
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * public LeaveBalanceSummaryDto getWorkerLeaveSummary(Long realEstateId, Long
 * workerId) {
 * assertWorkerBelongsToProperty(realEstateId, workerId);
 * User worker = findUserOrThrow(workerId);
 * 
 * LeaveBalance balance = leaveBalanceRepository.findByWorker(worker)
 * .orElseGet(() -> createInitialBalance(worker));
 * 
 * List<LeaveRequest> history = leaveRequestRepository
 * .findByWorkerIdAndRealEstatePropertyIdOrderByCreatedAtDesc(workerId,
 * realEstateId);
 * 
 * return LeaveBalanceSummaryDto.builder()
 * .workerId(worker.getId())
 * .workerFirstName(worker.getPrenom())
 * .workerLastName(worker.getNom())
 * .totalAccruedDays(balance.getTotalAccruedDays())
 * .usedDays(balance.getUsedDays())
 * .pendingDays(balance.getPendingDays())
 * .remainingDays(balance.getRemainingDays())
 * .history(history.stream().map(this::toResponseDto).collect(Collectors.toList(
 * )))
 * .build();
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Submit leave request
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional
 * public LeaveRequestResponseDto submitLeaveRequest(Long realEstateId, Long
 * workerId, LeaveRequestDto dto) {
 * assertWorkerBelongsToProperty(realEstateId, workerId);
 * User worker = findUserOrThrow(workerId);
 * RealEstateProperty property = findPropertyOrThrow(realEstateId);
 * 
 * if (dto.getStartDate().isAfter(dto.getEndDate())) {
 * throw new
 * IllegalArgumentException("La date de début doit être avant la date de fin");
 * }
 * if (dto.getStartDate().isBefore(LocalDate.now())) {
 * throw new
 * IllegalArgumentException("La date de début ne peut pas être dans le passé");
 * }
 * 
 * boolean overlap = leaveRequestRepository.existsOverlappingRequest(
 * workerId, realEstateId, dto.getStartDate(), dto.getEndDate());
 * if (overlap) {
 * throw new
 * IllegalStateException("Une demande de congé chevauche déjà cette période sur ce chantier"
 * );
 * }
 * 
 * double requestedDays = countWorkingDays(dto.getStartDate(),
 * dto.getEndDate());
 * 
 * LeaveBalance balance = leaveBalanceRepository.findByWorker(worker)
 * .orElseGet(() -> createInitialBalance(worker));
 * 
 * if (balance.getRemainingDays() < requestedDays) {
 * throw new IllegalStateException(
 * String.
 * format("Solde insuffisant. Disponible: %.1f jour(s), Demandé: %.1f jour(s)",
 * balance.getRemainingDays(), requestedDays));
 * }
 * 
 * LeaveRequest request = new LeaveRequest();
 * request.setWorker(worker);
 * request.setRealEstateProperty(property);
 * request.setStartDate(dto.getStartDate());
 * request.setEndDate(dto.getEndDate());
 * request.setRequestedDays(requestedDays);
 * request.setReason(dto.getReason());
 * request.setStatus(LeaveRequestStatus.PENDING);
 * leaveRequestRepository.save(request);
 * 
 * balance.setPendingDays(balance.getPendingDays() + requestedDays);
 * leaveBalanceRepository.save(balance);
 * 
 * log.info("Leave request created: worker={} property={} days={} ({} → {})",
 * workerId, realEstateId, requestedDays, dto.getStartDate(), dto.getEndDate());
 * 
 * return toResponseDto(request);
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Review
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional
 * public LeaveRequestResponseDto reviewLeaveRequest(Long realEstateId, Long
 * requestId,
 * Long managerId, LeaveReviewDto reviewDto) {
 * LeaveRequest request = leaveRequestRepository.findById(requestId)
 * .orElseThrow(() -> new
 * IllegalArgumentException("Demande de congé introuvable : " + requestId));
 * 
 * // Ensure request belongs to the given property
 * if (!request.getRealEstateProperty().getId().equals(realEstateId)) {
 * throw new
 * IllegalArgumentException("Cette demande n'appartient pas à la propriété " +
 * realEstateId);
 * }
 * 
 * if (request.getStatus() != LeaveRequestStatus.PENDING) {
 * throw new IllegalStateException("Cette demande a déjà été traitée");
 * }
 * 
 * User manager = findUserOrThrow(managerId);
 * LeaveBalance balance =
 * leaveBalanceRepository.findByWorker(request.getWorker())
 * .orElseThrow(() -> new
 * IllegalStateException("Solde de congé introuvable pour ce worker"));
 * 
 * LeaveRequestStatus decision;
 * try {
 * decision = LeaveRequestStatus.valueOf(reviewDto.getDecision().toUpperCase());
 * } catch (IllegalArgumentException e) {
 * throw new
 * IllegalArgumentException("Décision invalide. Valeurs acceptées : APPROVED, REJECTED"
 * );
 * }
 * 
 * if (decision == LeaveRequestStatus.REJECTED
 * && (reviewDto.getManagerComment() == null ||
 * reviewDto.getManagerComment().isBlank())) {
 * throw new IllegalArgumentException("Un motif de refus est obligatoire");
 * }
 * 
 * double days = request.getRequestedDays();
 * 
 * if (decision == LeaveRequestStatus.APPROVED) {
 * balance.setPendingDays(Math.max(0, balance.getPendingDays() - days));
 * balance.setUsedDays(balance.getUsedDays() + days);
 * } else {
 * balance.setPendingDays(Math.max(0, balance.getPendingDays() - days));
 * }
 * 
 * leaveBalanceRepository.save(balance);
 * 
 * request.setStatus(decision);
 * request.setManagerComment(reviewDto.getManagerComment());
 * request.setReviewedBy(manager);
 * request.setReviewedAt(LocalDateTime.now());
 * leaveRequestRepository.save(request);
 * 
 * log.info("Leave request {} {} by manager {} on property {}", requestId,
 * decision, managerId, realEstateId);
 * 
 * return toResponseDto(request);
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Pending requests for a property
 * // -------------------------------------------------------------------------
 * 
 * @Override
 * 
 * @Transactional(readOnly = true)
 * public List<LeaveRequestResponseDto> getAllPendingRequests(Long realEstateId)
 * {
 * return leaveRequestRepository
 * .findByRealEstatePropertyIdAndStatus(realEstateId,
 * LeaveRequestStatus.PENDING)
 * .stream()
 * .map(this::toResponseDto)
 * .collect(Collectors.toList());
 * }
 * 
 * // -------------------------------------------------------------------------
 * // Helpers
 * // -------------------------------------------------------------------------
 * 
 * public static double countWorkingDays(LocalDate start, LocalDate end) {
 * long count = 0;
 * LocalDate current = start;
 * while (!current.isAfter(end)) {
 * DayOfWeek dow = current.getDayOfWeek();
 * if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY)
 * count++;
 * current = current.plusDays(1);
 * }
 * return count;
 * }
 * 
 * private void assertWorkerBelongsToProperty(Long realEstateId, Long workerId)
 * {
 * boolean belongs = userRepository.findWorkersByRealEstateId(realEstateId)
 * .stream().anyMatch(u -> u.getId().equals(workerId));
 * if (!belongs) {
 * throw new IllegalArgumentException(
 * "Le worker " + workerId + " n'est pas assigné à la propriété " +
 * realEstateId);
 * }
 * }
 * 
 * private LeaveBalance createInitialBalance(User worker) {
 * LeaveBalance b = new LeaveBalance();
 * b.setWorker(worker);
 * b.setTotalAccruedDays(0);
 * b.setUsedDays(0);
 * b.setPendingDays(0);
 * return leaveBalanceRepository.save(b);
 * }
 * 
 * private User findUserOrThrow(Long id) {
 * return userRepository.findById(id)
 * .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : "
 * + id));
 * }
 * 
 * private RealEstateProperty findPropertyOrThrow(Long id) {
 * return realEstatePropertyRepository.findById(id)
 * .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable : " +
 * id));
 * }
 * 
 * private LeaveRequestResponseDto toResponseDto(LeaveRequest r) {
 * return LeaveRequestResponseDto.builder()
 * .id(r.getId())
 * .workerId(r.getWorker().getId())
 * .workerFirstName(r.getWorker().getPrenom())
 * .workerLastName(r.getWorker().getNom())
 * // .realEstateId(r.getRealEstateProperty().getId())
 * // .realEstateName(r.getRealEstateProperty().getName())
 * .startDate(r.getStartDate())
 * .endDate(r.getEndDate())
 * .requestedDays(r.getRequestedDays())
 * .reason(r.getReason())
 * .status(r.getStatus())
 * .managerComment(r.getManagerComment())
 * .reviewedByName(r.getReviewedBy() != null
 * ? r.getReviewedBy().getPrenom() + " " + r.getReviewedBy().getNom()
 * : null)
 * .reviewedAt(r.getReviewedAt())
 * .createdAt(r.getCreatedAt())
 * .build();
 * }
 * }
 */