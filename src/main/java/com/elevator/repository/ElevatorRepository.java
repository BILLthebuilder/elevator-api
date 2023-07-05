package com.elevator.repository;

import com.elevator.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ElevatorRepository extends JpaRepository<Elevator, Long> {

    Elevator findById(UUID id);
}
