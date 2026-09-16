package com.wakana.realestateworks.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.response.CheckResult;
import com.wakana.realestateworks.dto.response.DailyWorkSummaryResponse;
import com.wakana.realestateworks.dto.response.MonthlyWorkSummaryResponse;
import com.wakana.realestateworks.dto.response.PresenceDashboardResponse;
import com.wakana.realestateworks.dto.response.PresenceHistoryResponse;
import com.wakana.realestateworks.dto.response.WorkerResponseDto;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.exception.ResourceNotFoundException;
import com.wakana.realestateworks.model.PointingAddress;
import com.wakana.realestateworks.model.PresenceLog;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.model.WorkerPresence;
import com.wakana.realestateworks.repository.PointingAddressRepository;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.repository.WorkerPresenceRepository;
import com.wakana.realestateworks.services.AuthenticationService;
import com.wakana.realestateworks.services.WorkerService;
import com.wakana.realestateworks.util.QRCodeUtil;

import lombok.RequiredArgsConstructor;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerPresenceRepository presenceRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final RealEstatePropertyRepository realEstatePropertyRepository;
    private final PointingAddressRepository pointingAddressRepository;
    private final Map<Long, LocalDateTime> lastCheckMap = new HashMap<>();

    @Override
    public User createWorker(SignUpRequest signUpRequest, Long propertyId) {

        User savedUser = authenticationService.signUp(signUpRequest);

        RealEstateProperty existingProperty = realEstatePropertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + propertyId));
        savedUser.setAssignedCompany(existingProperty);

        savedUser.setManager(existingProperty.getPromoter());

        return userRepository.save(savedUser);
    }

    @Override
    public Page<WorkerResponseDto> getWorkersByProperty(Long propertyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        List<ProfilEnum> profils = List.of(ProfilEnum.WORKER, ProfilEnum.ADMIN, ProfilEnum.SITE_MANAGER);

        Page<User> workers = userRepository.findByAssignedCompanyIdAndProfilIn(propertyId, profils, pageable);

        LocalDate today = LocalDate.now();

        return workers.map(user -> {
            boolean present = presenceRepository
                    .findByWorkerIdAndDate(user.getId(), today)
                    .map(presence -> {
                        List<PresenceLog> logs = presence.getLogs();
                        if (logs != null && !logs.isEmpty()) {
                            PresenceLog lastLog = logs.get(logs.size() - 1);
                            // présent uniquement si checkIn exist et checkOut est null
                            return lastLog.getCheckInTime() != null && lastLog.getCheckOutTime() == null;
                        }
                        return false;
                    })
                    .orElse(false);

            return new WorkerResponseDto(
                    user.getId(),
                    user.getPrenom(),
                    user.getNom(),
                    user.getTelephone(),
                    present);
        });
    }

    @Override
    public CheckResult handleCheck(Long workerId, String qrCodeText, double workerLat, double workerLon) {
        LocalDateTime now1 = LocalDateTime.now();
        User worker = getWorkerById(workerId); // récupérer le worker pour son nom
        String workerName = worker.getPrenom(); // ou getFullName()

        String companyName = worker.getAssignedCompany().getName(); // ou getFullName()

        // 1. Limitation de fréquence
        if (lastCheckMap.containsKey(workerId)) {
            LocalDateTime lastCheck = lastCheckMap.get(workerId);
            Duration diff = Duration.between(lastCheck, now1);
            if (diff.getSeconds() < 60) {
                long secondsLeft = 60 - diff.getSeconds();
                return new CheckResult(false,
                        "Veuillez attendre " + secondsLeft + " secondes avant de pointer à nouveau.");
            }
        }

        // 2. Décoder le QR code
        String[] qrData = QRCodeUtil.decodeQrCode(qrCodeText);
        if (qrData == null || qrData.length == 0) {
            return new CheckResult(false, "QR Code invalide ou illisible.");
        }

        Long propertyId;
        try {
            propertyId = Long.parseLong(qrData[0]);
        } catch (NumberFormatException e) {
            return new CheckResult(false, "QR Code invalide : ID de propriété incorrect.");
        }

        // 3. Vérifier assignation
        RealEstateProperty property = realEstatePropertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Propriété introuvable."));

        boolean isWorkerAssigned = property.getWorkers().stream()
                .anyMatch(w -> w.getId().equals(workerId));

        if (!isWorkerAssigned) {
            return new CheckResult(false, "Accès refusé : Vous n'êtes pas assigné à cette propriété.");
        }

        // 4. Vérifier position GPS
        List<PointingAddress> pointingAddresses = pointingAddressRepository.findByRealEstatePropertyId(propertyId);

        boolean isNear = pointingAddresses.stream().anyMatch(addr -> {
            double distance = haversine(workerLat, workerLon, addr.getLatitude(), addr.getLongitude());
            return distance <= 20.0; // tolérance 20 m
        });

        if (!isNear) {
            return new CheckResult(false,
                    "Pointage refusé : vous n'êtes pas à proximité d'un point de pointage autorisé.");
        }

        // 5. Gestion check-in / check-out
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        WorkerPresence presence = presenceRepository.findByWorkerIdAndDate(workerId, today)
                .orElseGet(() -> {
                    WorkerPresence newPresence = new WorkerPresence();
                    newPresence.setWorker(worker);
                    newPresence.setDate(today);
                    newPresence.setLogs(new ArrayList<>());
                    return newPresence;
                });

        List<PresenceLog> logs = presence.getLogs();
        PresenceLog currentLog = logs.isEmpty() ? null : logs.get(logs.size() - 1);

        lastCheckMap.put(workerId, now1);

        if (currentLog == null || currentLog.getCheckOutTime() != null) {
            // Check-in
            PresenceLog newLog = new PresenceLog();
            newLog.setCheckInTime(now);
            newLog.setPresence(presence);
            newLog.setLatitude(workerLat);
            newLog.setLongitude(workerLon);
            logs.add(newLog);
            presenceRepository.save(presence);

            String message = "Bonjour " + workerName + ", bienvenue chez " + companyName;
            return new CheckResult(true, message);
        } else {
            // Check-out
            currentLog.setCheckOutTime(now);
            presenceRepository.save(presence);

            String message = "Au revoir " + workerName;
            return new CheckResult(true, message);
        }
    }

    private User getWorkerById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    @Override
    public double getTodayPresenceRate(Long promoterId) {
        LocalDate today = LocalDate.now();

        // Total des ouvriers affectés à une propriété du promoteur
        long totalWorkers = userRepository.countByAssignedCompany_Promoter_IdAndProfil(promoterId, ProfilEnum.WORKER);

        if (totalWorkers == 0) {
            return 0.0;
        }

        // Nombre d'ouvriers présents aujourd'hui (ayant fait check-in)
        long presentWorkers = presenceRepository
                .countByWorker_AssignedCompany_Promoter_IdAndDateAndCheckInTimeIsNotNull(promoterId, today);

        return (double) presentWorkers / totalWorkers * 100.0;
    }

    /*
     * Par propriété
     * 
     * @Override
     * public double getTodayPresenceRate(Long propertyId) {
     * LocalDate today = LocalDate.now();
     * 
     * // Total des ouvriers affectés à la propriété
     * long totalWorkers =
     * userRepository.countByAssignedPropertyIdAndProfil(propertyId,
     * ProfilEnum.WORKER);
     * 
     * if (totalWorkers == 0) {
     * return 0.0;
     * }
     * 
     * // Nombre de check-in aujourd'hui
     * long presentWorkers = presenceRepository
     * .countByWorker_AssignedProperty_IdAndDateAndCheckInTimeIsNotNull(propertyId,
     * today);
     * 
     * return (double) presentWorkers / totalWorkers * 100.0;
     * }
     */

    @Override
    public Page<User> getUsersByManagerAndProfil(Long managerId, ProfilEnum type, Pageable pageable) {
        Page<User> users = userRepository.findByManagerIdAndProfil(managerId, type, pageable);
        return users;
    }

    @Override
    public User linkUserToManager(Long managerId, SignUpRequest signUpRequest) {
        User savedUser = authenticationService.signUp(signUpRequest);

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        savedUser.setManager(manager);

        return userRepository.save(savedUser);

    }

    @Override
    public Page<User> getUsersByManagerExcludingSupplierAndSubcontractor(Long managerId, Pageable pageable) {
        return userRepository.findByManagerIdAndProfilNotIn(managerId,
                List.of(ProfilEnum.SUPPLIER, ProfilEnum.SUBCONTRACTOR), pageable);
    }

    @Override
    public PresenceDashboardResponse getPresenceDashboardData(Long workerId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<WorkerPresence> presences = presenceRepository.findByWorkerIdAndDateBetween(workerId, startOfMonth,
                endOfMonth);

        int daysPresent = 0;
        long totalWorkedMinutes = 0;

        for (WorkerPresence presence : presences) {
            List<PresenceLog> logs = presence.getLogs();

            long dailyMinutes = calculateWorkedMinutes(logs, presence.getDate(), LocalDate.now(), false);
            if (dailyMinutes > 0) {
                daysPresent++;
                totalWorkedMinutes += dailyMinutes;
            }
        }

        long totalWorkedHours = totalWorkedMinutes / 60;

        return new PresenceDashboardResponse(daysPresent, totalWorkedHours);
    }

    @Override
    public PresenceHistoryResponse getPresenceHistory(Long workerId, LocalDate date) {
        WorkerPresence presence = presenceRepository.findByWorkerIdAndDate(workerId, date)
                .orElseThrow(() -> new RuntimeException("Presence not found for worker on this date"));

        List<PresenceLog> logs = presence.getLogs();
        LocalDate today = LocalDate.now();

        long totalMinutes = calculateWorkedMinutes(logs, date, today, true);

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        String formattedTime = String.format("%dh %02dmin", hours, minutes);

        return new PresenceHistoryResponse(logs, formattedTime);
    }

    @Override
    public MonthlyWorkSummaryResponse getMonthlyWorkSummary(Long workerId, YearMonth month) {
        List<DailyWorkSummaryResponse> summaryList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        long totalMinutesWorked = 0;

        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate currentDate = month.atDay(day);

            // Ignorer les week-ends
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }

            Optional<WorkerPresence> presenceOpt = presenceRepository.findByWorkerIdAndDate(workerId, currentDate);
            long dailyMinutes = 0;

            if (presenceOpt.isPresent()) {
                WorkerPresence presence = presenceOpt.get();
                List<PresenceLog> logs = presence.getLogs();

                dailyMinutes = calculateWorkedMinutes(logs, currentDate, today, true);
            }

            totalMinutesWorked += dailyMinutes;

            long dailyHours = dailyMinutes / 60;
            long dailyRemainingMinutes = dailyMinutes % 60;
            String formattedDailyTime = String.format("%dh %02dmin", dailyHours, dailyRemainingMinutes);

            summaryList.add(new DailyWorkSummaryResponse(
                    currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    formattedDailyTime));
        }

        long totalHours = totalMinutesWorked / 60;
        long totalRemainingMinutes = totalMinutesWorked % 60;
        String totalFormatted = String.format("%dh %02dmin", totalHours, totalRemainingMinutes);

        return new MonthlyWorkSummaryResponse(summaryList, totalFormatted);
    }

    private long calculateWorkedMinutes(List<PresenceLog> logs, LocalDate workDate, LocalDate today,
            boolean includeOpenLog) {
        long totalMinutes = logs.stream()
                .filter(log -> log.getCheckInTime() != null)
                .mapToLong(log -> {
                    LocalTime checkOut = log.getCheckOutTime();

                    if (checkOut == null && includeOpenLog && workDate.equals(today)) {
                        checkOut = LocalTime.now();
                    }

                    if (checkOut == null) {
                        return 0;
                    }

                    return ChronoUnit.MINUTES.between(log.getCheckInTime(), checkOut);
                })
                .sum();

            LocalTime breakStart = LocalTime.of(13, 0);
            boolean breakHasStarted = workDate.isBefore(today)
                || (workDate.equals(today) && !LocalTime.now().isBefore(breakStart));

            return totalMinutes > 0 && breakHasStarted ? Math.max(0, totalMinutes - 60) : totalMinutes;
    }

    @Override
    public User linkUserToProperty(Long userId, Long propertyId) {
        RealEstateProperty property = realEstatePropertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Propriété introuvable avec ID: " + propertyId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec ID: " + userId));

        if (property.getWorkers().contains(user)) {
            throw new IllegalArgumentException("Cet utilisateur est déjà lié à cette propriété.");
        }

        user.setAssignedCompany(property);

        user.setManager(property.getPromoter());

        return userRepository.save(user);
    }

    /**
     * Calcule la distance en mètres entre 2 points GPS avec la formule de Haversine
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // rayon de la Terre en mètres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
