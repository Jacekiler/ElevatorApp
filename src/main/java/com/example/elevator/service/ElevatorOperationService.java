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

    public void call(Integer id, CallRequestDTO callRequestDTO) {
        log.info("Calling elevator on floor: {}", callRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        addCallRequest(elevator, callRequestDTO.getFloor(), callRequestDTO.getDirection());
    }

    public void select(Integer id, SelectRequestDTO selectRequestDTO) {
        log.info("Selected floor: {}", selectRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        addSelectRequest(elevator, selectRequestDTO.getFloor());
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

    public void addCallRequest(Elevator elevator, int request, CallDirection direction) {
        if (shouldAddUpRequest(elevator, request, direction)) {
            elevator.addUpRequest(request);
        } else if(shouldAddDownRequest(elevator, request, direction)){
            elevator.addDownRequest(request);
        }
    }

    private boolean shouldAddUpRequest(Elevator elevator, int request, CallDirection direction) {
        return elevator.isMovingUp() ?
                CallDirection.UP == direction && request > elevator.getCurrentFloor()
                        || CallDirection.DOWN == direction && !elevator.getUpRequests().isEmpty() && request > elevator.getUpRequests().last()
                : elevator.isMovingDown() ?
                CallDirection.UP == direction && !elevator.getDownRequests().isEmpty() && request > elevator.getDownRequests().last()
                        || CallDirection.DOWN == direction && request > elevator.getCurrentFloor()
                : request > elevator.getCurrentFloor();
    }

    private boolean shouldAddDownRequest(Elevator elevator, int request, CallDirection direction) {
        return elevator.isMovingUp() ?
                CallDirection.UP == direction && request < elevator.getCurrentFloor()
                        || CallDirection.DOWN == direction && !elevator.getUpRequests().isEmpty() && request < elevator.getUpRequests().last()
                : elevator.isMovingDown() ?
                CallDirection.UP == direction && !elevator.getDownRequests().isEmpty() && request < elevator.getDownRequests().last()
                        || CallDirection.DOWN == direction && request < elevator.getCurrentFloor()
                : request < elevator.getCurrentFloor();
    }

    public void addSelectRequest(Elevator elevator, int request) {
        if (request > elevator.getCurrentFloor()) {
            elevator.addUpRequest(request);
        } else if (request < elevator.getCurrentFloor()) {
            elevator.addDownRequest(request);
        }
    }
}
