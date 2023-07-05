package com.elevator.controller;

import com.elevator.model.Building;
import com.elevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

import com.elevator.model.Elevator;


@RestController
@RequestMapping("/api/v1/elevator")
@RequiredArgsConstructor
public class ElevatorController {
    private final ElevatorService elevatorService;
    @PostMapping("/call")
    public ResponseEntity<String> callElevator(@RequestParam int fromFloor, @RequestParam int toFloor) {
       elevatorService.callElevator(fromFloor,toFloor);
        return ResponseEntity.ok("Elevator called from floor " + fromFloor + " to floor " + toFloor);
    }

    @GetMapping("/info")
    public ResponseEntity<Elevator> getElevatorInfo(@RequestParam UUID elevatorId) {
        // Logic to get real-time information about the elevator
        // Retrieve the elevator object by ID
        var elevator = elevatorService.getElevatorInfo(elevatorId);
        return ResponseEntity.ok(elevator);
    }

    @PostMapping("/doors/open")
    public ResponseEntity<String> openElevatorDoors(@RequestParam UUID elevatorId) {
        // Logic to open the elevator doors
        elevatorService.openElevatorDoors(elevatorId);
        return ResponseEntity.ok("Elevator doors will be open for two seconds");
    }

    @PostMapping("/doors/close")
    public ResponseEntity<String> closeElevatorDoors(@RequestParam Elevator elevator) {
        elevatorService.closeElevatorDoors(Optional.of(elevator));
        return ResponseEntity.ok("Elevator doors closed");
    }
}
