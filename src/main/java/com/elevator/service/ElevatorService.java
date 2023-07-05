package com.elevator.service;

import com.elevator.Enum.ElevatorDirection;
import com.elevator.Enum.ElevatorState;
import com.elevator.model.Building;
import com.elevator.model.Elevator;
import com.elevator.model.ElevatorLog;
import com.elevator.repository.BuildingRepository;
import com.elevator.repository.ElevatorLogRepository;
import com.elevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
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

    private final ElevatorLogRepository elevatorLogRepository;

    private final ElevatorLog elevatorLog;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Transactional
    public void callElevator(int fromFloor, int toFloor) {
       Optional<Elevator> elevator = findNearestElevator(fromFloor);
        if (elevator.isPresent()) {
            elevator.get().setTargetFloor(toFloor);
            if (elevator.get().getCurrentFloor() < toFloor) {
                elevator.get().setElevatordirection(ElevatorDirection.UP);
            } else {
                elevator.get().setElevatordirection(ElevatorDirection.DOWN);
            }
            if (elevator.get().getElevatorState() == ElevatorState.IDLE) {
                scheduleElevatorMovement(elevator);
            }
        }
        elevatorLog.setElevatorState(elevator.get().getElevatorState());
        elevatorLog.setElevatordirection(elevator.get().getElevatordirection());
        elevatorLog.setPlace(elevator.get().getCurrentFloor());
        elevatorLogRepository.save(elevatorLog);
    }

    public Elevator getElevatorInfo(UUID elevatorID) {
       return  elevatorRepository.findById(elevatorID);
    }

    public void openElevatorDoors(UUID elevatorId) {
        var building = new Building();

        Optional<Elevator> elevator = building.getElevators().stream()
                .filter(ev -> ev.getId().equals(elevatorId))
                .findFirst();
        if (elevator.isPresent() && elevator.get().getElevatorState() == ElevatorState.MOVING) {
            // Stop the elevator
            log.info("opening------"+dateFormat.format(new Date())+"------------");
            elevator.get().setElevatorState(ElevatorState.STOPPED);
            // Schedule a task to close the doors after 2 seconds
            executorService.schedule(() -> closeElevatorDoors(elevator), 2, TimeUnit.SECONDS);
            log.info("closed------"+dateFormat.format(new Date())+"------------");
        }
        elevatorLog.setElevatorState(elevator.get().getElevatorState());
        elevatorLog.setElevatordirection(elevator.get().getElevatordirection());
        elevatorLog.setPlace(elevator.get().getCurrentFloor());
        elevatorLogRepository.save(elevatorLog);
    }

    public void closeElevatorDoors(Optional<Elevator> elevator) {
        elevator.get().setElevatorState(ElevatorState.MOVING);
        // Resume elevator movement
        scheduleElevatorMovement(elevator);

        elevatorLog.setElevatorState(elevator.get().getElevatorState());
        elevatorLog.setElevatordirection(elevator.get().getElevatordirection());
        elevatorLog.setPlace(elevator.get().getCurrentFloor());
        elevatorLogRepository.save(elevatorLog);
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
            elevator.get().setElevatordirection(ElevatorDirection.UP);
            for (int floor = currentFloor + 1; floor <= targetFloor; floor++) {
                elevator.get().setCurrentFloor(floor);
            }
        } else if (currentFloor > targetFloor) {
            elevator.get().setElevatorState(ElevatorState.MOVING);
            elevator.get().setElevatordirection(ElevatorDirection.DOWN);
            for (int floor = currentFloor - 1; floor >= targetFloor; floor--) {
                elevator.get().setCurrentFloor(floor);
            }
        }

        elevator.get().setElevatorState(ElevatorState.IDLE);

        elevatorLog.setElevatorState(elevator.get().getElevatorState());
        elevatorLog.setElevatordirection(elevator.get().getElevatordirection());
        elevatorLog.setPlace(elevator.get().getCurrentFloor());
        elevatorLogRepository.save(elevatorLog);
    }

    public boolean buildingExists(UUID id) {
        return buildingRepository.findById(id) != null;
    }
}
