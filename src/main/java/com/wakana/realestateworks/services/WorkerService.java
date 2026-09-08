package com.wakana.realestateworks.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.realestateworks.dto.SignUpRequest;
import com.wakana.realestateworks.dto.response.CheckResult;
import com.wakana.realestateworks.dto.response.DailyWorkSummaryResponse;
import com.wakana.realestateworks.dto.response.MonthlyWorkSummaryResponse;
import com.wakana.realestateworks.dto.response.PresenceDashboardResponse;
import com.wakana.realestateworks.dto.response.PresenceHistoryResponse;
import com.wakana.realestateworks.dto.response.WorkerResponseDto;
import com.wakana.realestateworks.enums.ProfilEnum;
import com.wakana.realestateworks.model.User;

public interface WorkerService {
    public User createWorker(SignUpRequest signUpRequest, Long propertyId);

    Page<WorkerResponseDto> getWorkersByProperty(Long propertyId, int page, int size);

    CheckResult  handleCheck(Long workerId, String qrCodeText, double workerLat, double workerLon);

    double getTodayPresenceRate(Long propertyId);

    Page<User> getUsersByManagerAndProfil(Long managerId, ProfilEnum profil, Pageable pageable);

    User linkUserToManager(Long managerId, SignUpRequest signUpRequest);

    User linkUserToProperty(Long userId, Long propertyId);

    Page<User> getUsersByManagerExcludingSupplierAndSubcontractor(Long managerId, Pageable pageable);

    PresenceDashboardResponse getPresenceDashboardData(Long workerId);

    PresenceHistoryResponse getPresenceHistory(Long workerId, LocalDate date);

    MonthlyWorkSummaryResponse getMonthlyWorkSummary(Long workerId, YearMonth month);

}
