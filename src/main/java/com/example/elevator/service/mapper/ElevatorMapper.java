package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.ElevatorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElevatorMapper {

    private final ElevatorQueueMapper queueMapper;

    public ElevatorDTO toDto(Elevator elevator) {
        return ElevatorDTO.builder()
                .id(elevator.getId())
                .currentFloor(elevator.getCurrentFloor())
                .minFloor(elevator.getMinFloor())
                .maxFloor(elevator.getMaxFloor())
                .status(elevator.getElevatorStatus())
                .direction(elevator.getElevatorDirection())
                .doorState(elevator.getDoorState())
                .requests(queueMapper.getRequests(elevator))
                .build();
    }
}
