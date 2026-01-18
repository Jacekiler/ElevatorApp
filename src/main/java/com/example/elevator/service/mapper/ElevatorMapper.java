package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.ElevatorDTO;
import org.springframework.stereotype.Component;

@Component
public class ElevatorMapper {

    public ElevatorDTO toDto(Elevator elevator) {
        return ElevatorDTO.builder()
                .id(elevator.getId())
                .floor(elevator.getCurrentFloor())
                .minFloor(elevator.getMinFloor())
                .maxFloor(elevator.getMaxFloor())
                .status(elevator.getElevatorStatus())
                .state(elevator.getElevatorState())
                .build();
    }
}
