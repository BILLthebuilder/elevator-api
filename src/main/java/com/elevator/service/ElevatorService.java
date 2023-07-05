package com.elevator.service;

import com.elevator.Enum.Direction;
import com.elevator.Enum.ElevatorState;
import com.elevator.model.Building;
import com.elevator.model.Elevator;
import com.elevator.repository.BuildingRepository;
import com.elevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Slf4j
@Service
public class ElevatorService {

    private final ScheduledExecutorService executorService;

    private final BuildingRepository buildingRepository;

    private final ElevatorRepository elevatorRepository;

    @Transactional
    public void callElevator(int fromFloor, int toFloor) {
       Optional<Elevator> elevator = findNearestElevator(fromFloor);
        if (elevator.isPresent()) {
            elevator.get().setTargetFloor(toFloor);
            if (elevator.get().getCurrentFloor() < toFloor) {
                elevator.get().setDirection(Direction.UP);
            } else {
                elevator.get().setDirection(Direction.DOWN);
            }
            if (elevator.get().getElevatorState() == ElevatorState.IDLE) {
                scheduleElevatorMovement(elevator);
            }
        }
    }

    public Elevator getElevatorInfo(UUID buildingId) {
       return  elevatorRepository.findById(buildingId);
    }

    public void openElevatorDoors(UUID elevatorId) {
        var building = new Building();

        Optional<Elevator> elevator = building.getElevators().stream()
                .filter(ev -> ev.getId().equals(elevatorId))
                .findFirst();
        if (elevator.isPresent() && elevator.get().getElevatorState() == ElevatorState.MOVING) {
            // Stop the elevator
            elevator.get().setElevatorState(ElevatorState.STOPPED);
            // Schedule a task to close the doors after 2 seconds
            executorService.schedule(() -> closeElevatorDoors(elevator), 2, TimeUnit.SECONDS);
        }
    }

    private void closeElevatorDoors(Optional<Elevator> elevator) {
        elevator.get().setElevatorState(ElevatorState.MOVING);
        // Resume elevator movement
        scheduleElevatorMovement(elevator);
    }

    private Optional<Elevator> findNearestElevator(int floor) {
        // Find the nearest idle or moving elevator based on the current floor
        // We then return the elevator with the minimum distance from the desired floor
       var building = new Building();
        return building.getElevators().stream()
                .filter(elevator -> elevator.getElevatorState() == ElevatorState.IDLE || elevator.getElevatorState() == ElevatorState.MOVING)
                .min(Comparator.comparingInt(elevator -> Math.abs(elevator.getCurrentFloor() - floor)));
    }

    private void scheduleElevatorMovement(Optional<Elevator> elevator) {
        executorService.schedule(() -> moveElevator(elevator), 5, TimeUnit.SECONDS);
    }

    private void moveElevator(Optional<Elevator> elevator) {

        int currentFloor = elevator.get().getCurrentFloor();
        int targetFloor = elevator.get().getTargetFloor();

        if (currentFloor < targetFloor) {
            elevator.get().setElevatorState(ElevatorState.MOVING);
            elevator.get().setDirection(Direction.UP);
            for (int floor = currentFloor + 1; floor <= targetFloor; floor++) {
                elevator.get().setCurrentFloor(floor);
            }
        } else if (currentFloor > targetFloor) {
            elevator.get().setElevatorState(ElevatorState.MOVING);
            elevator.get().setDirection(Direction.DOWN);
            for (int floor = currentFloor - 1; floor >= targetFloor; floor--) {
                elevator.get().setCurrentFloor(floor);
            }
        }

        elevator.get().setElevatorState(ElevatorState.IDLE);
    }

    public boolean buildingExists(UUID id) {
        return buildingRepository.findById(id) != null;
    }
}
