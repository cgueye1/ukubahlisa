package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.response.CheckResult;
import com.wakana.realestateworks.dto.response.MonthlyWorkSummaryResponse;
import com.wakana.realestateworks.dto.response.PresenceDashboardResponse;
import com.wakana.realestateworks.dto.response.PresenceHistoryResponse;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.services.WorkerService;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping(value = "/save/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createWorker(@ModelAttribute SignUpRequest signUpRequest,
            @PathVariable Long propertyId) {
        return ResponseEntity.ok(workerService.createWorker(signUpRequest, propertyId));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getWorkersByProperty(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(workerService.getWorkersByProperty(propertyId, page, size));
    }

    @PostMapping("/{workerId}/check")
    public ResponseEntity<String> check(
            @PathVariable Long workerId,
            @RequestParam String qrCodeText,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        // handleCheck retourne un objet contenant le message + le type de status
        CheckResult result = workerService.handleCheck(workerId, qrCodeText, latitude, longitude);

        // Si pointage réussi → HTTP 200, sinon 400
        HttpStatus status = result.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(result.getMessage());
    }

    @GetMapping("/manager/{managerId}/presence-rate")
    public ResponseEntity<Double> getTodayPresenceRate(@PathVariable Long managerId) {
        double rate = workerService.getTodayPresenceRate(managerId);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/{managerId}/team/others")
    public Page<User> getTeamExcludingSupplierAndSubcontractor(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return workerService.getUsersByManagerExcludingSupplierAndSubcontractor(managerId, pageable);
    }

    @GetMapping("/{managerId}/suppliers")
    public Page<User> getSuppliersByManager(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return workerService.getUsersByManagerAndProfil(managerId, ProfilEnum.SUPPLIER, pageable);
    }

    @GetMapping("/{managerId}/subcontractors")
    public Page<User> getSubcontractorsByManager(
            @PathVariable Long managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return workerService.getUsersByManagerAndProfil(managerId, ProfilEnum.SUBCONTRACTOR, pageable);
    }

    @PostMapping("/create/{managerId}")
    public ResponseEntity<?> create(
            @RequestBody SignUpRequest signUpRequest,
            @PathVariable Long managerId) {

        return ResponseEntity.ok(workerService.linkUserToManager(managerId, signUpRequest));
    }

    @GetMapping("/{workerId}/mobile/dashboard")
    public ResponseEntity<PresenceDashboardResponse> getPresenceDashboard(@PathVariable Long workerId) {
        PresenceDashboardResponse data = workerService.getPresenceDashboardData(workerId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{workerId}/presence-history")
    public ResponseEntity<PresenceHistoryResponse> getPresenceHistory(
            @PathVariable Long workerId,
            @RequestParam(required = false) String date // Format: dd-MM-yyyy
    ) {
        try {
            LocalDate parsedDate;
            if (date == null) {
                parsedDate = LocalDate.now(); // valeur par défaut = aujourd'hui
            } else {
                parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
            PresenceHistoryResponse response = workerService.getPresenceHistory(workerId, parsedDate);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{workerId}/monthly-summary")
    public ResponseEntity<MonthlyWorkSummaryResponse> getMonthlyWorkSummary(
            @PathVariable Long workerId,
            @RequestParam(required = false) String month // Format: mm-yyyy
    ) {
        try {
            YearMonth targetMonth;
            if (month == null) {
                targetMonth = YearMonth.now();
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
                targetMonth = YearMonth.parse(month, formatter);
            }

            MonthlyWorkSummaryResponse summary = workerService.getMonthlyWorkSummary(workerId, targetMonth);
            return ResponseEntity.ok(summary);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{propertyId}/workers/{userId}")
    public ResponseEntity<User> linkWorkerToProperty(
            @PathVariable Long propertyId,
            @PathVariable Long userId) {
        User linkedUser = workerService.linkUserToProperty(userId, propertyId);
        return ResponseEntity.ok(linkedUser);
    }

}
