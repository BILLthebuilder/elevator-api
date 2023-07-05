package com.elevator.controller;

import com.elevator.model.Building;
import com.elevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.*;

import com.elevator.model.Elevator;


@RestController
@RequestMapping("/api/v1/elevator")
@RequiredArgsConstructor
public class ElevatorController {
    private final Building building;
    private final ScheduledExecutorService executorService;

    private final ElevatorService elevatorService;


    @PostMapping("/call")
    public ResponseEntity<String> callElevator(@RequestParam int fromFloor, @RequestParam int toFloor) {
        // Logic to call the elevator from a floor to another floor
        // Update elevator's target floor, state, and direction
        // Schedule a task to move the elevator

        return ResponseEntity.ok("Elevator called from floor " + fromFloor + " to floor " + toFloor);
    }

    @GetMapping("/info")
    public ResponseEntity<Elevator> getElevatorInfo(@RequestParam String elevatorId) {
        // Logic to get real-time information about the elevator
        // Retrieve the elevator object by ID
        var elevator = new Elevator();
        return ResponseEntity.ok(elevator);
    }

    @PostMapping("/doors/open")
    public ResponseEntity<String> openElevatorDoors(@RequestParam String elevatorId) {
        // Logic to open the elevator doors

        return ResponseEntity.ok("Elevator doors opened");
    }

    @PostMapping("/doors/close")
    public ResponseEntity<String> closeElevatorDoors(@RequestParam String elevatorId) {
        // Logic to close the elevator doors

        return ResponseEntity.ok("Elevator doors closed");
    }

    // Task to simulate elevator movement
    @Scheduled(fixedRate = 5000) // Execute every 5 seconds
    public void moveElevator() {
        // Logic to update elevator's current floor, state, and direction
        // Check if the elevator has reached its target floor
        // If so, update state and direction accordingly
    }
}
