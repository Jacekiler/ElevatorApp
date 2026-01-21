package com.example.elevator.service;

import com.example.elevator.model.CallDirection;
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
        addRequest(elevator, callRequestDTO.getFloor(), callRequestDTO.getDirection());
    }

    public void select(Integer id, SelectRequestDTO selectRequestDTO) {
        log.info("Selected floor: {}", selectRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        CallDirection direction = selectRequestDTO.getFloor() > elevator.getCurrentFloor() ? CallDirection.UP : CallDirection.DOWN;
        addRequest(elevator, selectRequestDTO.getFloor(), direction);
    }

    public void openDoors(Integer id) {
        log.info("Trying to open doors in elevator {}", id);
        var elevator = elevatorService.getElevator(id);
        elevator.triggerOpenDoor();
    }

    public void closeDoors(Integer id) {
        log.info("Trying to close doors in elevator {}", id);
        var elevator = elevatorService.getElevator(id);
        elevator.triggerCloseDoor();
    }

    private void addRequest(Elevator elevator, int request, CallDirection direction) {
        if (request > elevator.getMaxFloor() || request < elevator.getMinFloor()){
            throw new IllegalArgumentException("Requested floor is out of elevator's scope");
        }

        if (CallDirection.UP == direction) {
            elevator.addUpRequest(request);
        } else {
            elevator.addDownRequest(request);
        }

//        if (request > elevator.getCurrentFloor() || request == elevator.getCurrentFloor() && elevator.isMovingDown()) {
//            elevator.addUpRequest(request);
//        } else if (request < elevator.getCurrentFloor() || request == elevator.getCurrentFloor() && elevator.isMovingUp()){
//            elevator.addDownRequest(request);
//        }
    }
}
