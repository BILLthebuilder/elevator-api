package com.elevator.service;

import com.elevator.dto.ElevatorStatus;
import com.elevator.model.Elevator;
import com.elevator.model.ElevatorLog;
import com.elevator.repository.ElevatorLogRepository;
import com.elevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevatorService {
    private final ElevatorRepository elevatorRepository;
    private final ElevatorLogRepository elevatorLogRepository;



    public void callElevator(int fromFloor, int toFloor) {
        // Logic to call the elevator and update its state and direction
        var elevator = new Elevator();
        // Save the elevator log
        var log = new ElevatorLog();
        log.setElevatorId(elevator.getId().toString());
        log.setPlace("Elevator " + elevator.getId());
        log.setState(elevator.getState());
        log.setDirection(elevator.getDirection());
        elevatorLogRepository.save(log);
    }

    public ElevatorStatus getElevatorStatus() {
        // Logic to get the real-time elevator status
        var elevatorStatus = new ElevatorStatus("","","","");
        return elevatorStatus;
    }
}