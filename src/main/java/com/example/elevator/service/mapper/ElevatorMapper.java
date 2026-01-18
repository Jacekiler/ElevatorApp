package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.ElevatorDTO;
import org.springframework.stereotype.Component;

@Component
public class ElevatorMapper {

    public ElevatorDTO toDto(Elevator elevator) {
        return ElevatorDTO.builder()
                .id(elevator.getId())
                .floor(elevator.getFloor())
                .status(elevator.getStatus())
                .state(elevator.getState())
                .build();
    }
}
