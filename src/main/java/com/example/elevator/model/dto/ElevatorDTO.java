package com.example.elevator.model.dto;

import com.example.elevator.model.ElevatorState;
import com.example.elevator.model.ElevatorStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElevatorDTO {
    private Integer id;
    private Integer floor;
    private Integer minFloor;
    private Integer maxFloor;
    private ElevatorStatus status;
    private ElevatorState state;
    private List<Integer> requests;
}
