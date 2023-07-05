package com.elevator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElevatorMovement {

    private final ElevatorService elevatorService;
    @Scheduled(fixedRate = 5000) // Execute every 5 seconds
    public void moveElevator() {
        // Logic to update elevator's current floor, state, and direction
        // Check if the elevator has reached its target floor
        // If so, update state and direction accordingly

    }
}
