package com.example.elevator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Elevator {
    private Integer id;
    private Integer floor;
    private ElevatorStatus status;
    private ElevatorState state;
}
