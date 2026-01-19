package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.CallRequestDTO;
import com.example.elevator.model.dto.SelectRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElevatorOperationService {

    private final ElevatorService elevatorService;

    //todo logging

    public void call(Integer id, CallRequestDTO callRequestDTO) {
        log.info("Calling elevator on floor: {}", callRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        addRequest(elevator, callRequestDTO.getFloor());
    }

    public void select(Integer id, SelectRequestDTO selectRequestDTO) {
        log.info("Selected floor: {}", selectRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        addRequest(elevator, selectRequestDTO.getFloor());
    }

    public void openDoors(Integer id) {
        // todo
    }

    public void closeDoors(Integer id) {
        // todo
    }

    private void addRequest(Elevator elevator, int request) {
        if (request > elevator.getMaxFloor() || request < elevator.getMinFloor()){
            throw new IllegalArgumentException("Requested floor is out of elevator's scope");
        }

        if (request > elevator.getCurrentFloor() || request == elevator.getCurrentFloor() && elevator.isMovingDown()) {
            elevator.addUpRequest(request);
        } else if (request < elevator.getCurrentFloor() || request == elevator.getCurrentFloor() && elevator.isMovingUp()){
            elevator.addDownRequest(request);
        }
    }
}
