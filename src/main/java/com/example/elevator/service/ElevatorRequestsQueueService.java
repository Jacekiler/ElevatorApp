package com.example.elevator.service;

import com.example.elevator.model.CallDirection;
import com.example.elevator.model.Elevator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElevatorRequestsQueueService {

    public void addCallRequest(Elevator elevator, int request, CallDirection direction) {
        if (shouldAddUpRequest(elevator, request, direction)) {
            log.info("Elevator {} - adding called floor {} to up queue", elevator.getId(), request);
            elevator.addUpRequest(request);
        } else if(shouldAddDownRequest(elevator, request, direction)){
            log.info("Elevator {} - adding called floor {} to down queue", elevator.getId(), request);
            elevator.addDownRequest(request);
        }
        log.info("Elevator {} - UP queue: {}", elevator.getId(), elevator.getUpRequests());
        log.info("Elevator {} - DOWN queue: {}", elevator.getId(), elevator.getDownRequests());
    }

    private boolean shouldAddUpRequest(Elevator elevator, int request, CallDirection direction) {
        return elevator.isMovingUp() ?
                CallDirection.UP == direction && request > elevator.getCurrentFloor()
                        || CallDirection.DOWN == direction && !elevator.getUpRequests().isEmpty() && request > elevator.getUpRequests().last()
                : elevator.isMovingDown() ?
                CallDirection.UP == direction && !elevator.getDownRequests().isEmpty() && request > elevator.getDownRequests().last()
                        || CallDirection.DOWN == direction && request >= elevator.getCurrentFloor()
                : request > elevator.getCurrentFloor();
    }

    private boolean shouldAddDownRequest(Elevator elevator, int request, CallDirection direction) {
        return elevator.isMovingUp() ?
                CallDirection.UP == direction && request <= elevator.getCurrentFloor()
                        || CallDirection.DOWN == direction && !elevator.getUpRequests().isEmpty() && request < elevator.getUpRequests().last()
                : elevator.isMovingDown() ?
                CallDirection.UP == direction && !elevator.getDownRequests().isEmpty() && request < elevator.getDownRequests().last()
                        || CallDirection.DOWN == direction && request < elevator.getCurrentFloor()
                : request < elevator.getCurrentFloor();
    }

    public void addSelectRequest(Elevator elevator, int request) {
        if (request > elevator.getCurrentFloor()) {
            log.info("Elevator {} - adding selected floor {} to up queue", elevator.getId(), request);
            elevator.addUpRequest(request);
        } else if (request < elevator.getCurrentFloor()) {
            log.info("Elevator {} - adding selected floor {} to down queue", elevator.getId(), request);
            elevator.addDownRequest(request);
        }
        log.info("Elevator {} - UP queue: {}", elevator.getId(), elevator.getUpRequests());
        log.info("Elevator {} - DOWN queue: {}", elevator.getId(), elevator.getDownRequests());
    }
}
