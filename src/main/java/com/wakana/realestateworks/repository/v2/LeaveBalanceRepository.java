package com.wakana.realestateworks.repository.v2;

import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.model.v2.LeaveBalance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByWorker(User worker);

    Optional<LeaveBalance> findByWorkerId(Long workerId);
}