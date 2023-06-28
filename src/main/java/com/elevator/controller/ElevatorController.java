package com.elevator.controller;

import com.elevator.dto.ElevatorStatus;
import com.elevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elevator")
@RequiredArgsConstructor
public class ElevatorController {
    private final ElevatorService elevatorService;


    @PostMapping("/call")
    public void callElevator(@RequestParam int fromFloor, @RequestParam int toFloor) {
        elevatorService.callElevator(fromFloor, toFloor);
    }

    @GetMapping("/status")
    public ElevatorStatus getElevatorStatus() {
        return elevatorService.getElevatorStatus();
    }
}
