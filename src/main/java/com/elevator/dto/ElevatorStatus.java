package com.elevator.dto;

public record ElevatorStatus(
        String up, String down, String left, String right
) {
}
