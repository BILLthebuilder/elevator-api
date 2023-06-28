package com.elevator.repository;

import com.elevator.model.ElevatorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ElevatorLogRepository extends JpaRepository<ElevatorLog, Long> {
}
