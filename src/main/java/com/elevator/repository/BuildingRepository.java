package com.elevator.repository;

import com.elevator.model.Building;
import com.elevator.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, Long> {

 Building findById(UUID id);


}
