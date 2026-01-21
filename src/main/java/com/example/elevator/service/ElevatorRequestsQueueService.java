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
