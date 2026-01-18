package com.example.elevator.service;

import com.example.elevator.model.dto.ElevatorDTO;
import com.example.elevator.service.mapper.ElevatorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElevatorStateService {

    private final ElevatorService elevatorService;
    private final ElevatorMapper elevatorMapper;

    public List<ElevatorDTO> getElevators() {
        return elevatorService.getElevators().stream()
                .map(elevatorMapper::toDto)
                .toList();
    }

    public ElevatorDTO getElevator(Integer id) {
        return elevatorMapper.toDto(elevatorService.getElevator(id));
    }

}
