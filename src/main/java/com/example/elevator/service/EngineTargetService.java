package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.OperationalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EngineTargetService {

    public void updateTarget(OperationalData data, Elevator elevator) {
        Integer targetFloor;
        if (elevator.isMovingUp()) {
            targetFloor = !elevator.getUpRequests().isEmpty() ? elevator.getUpRequests().first() : null;
        } else if (elevator.isMovingDown()) {
            targetFloor = !elevator.getDownRequests().isEmpty() ? elevator.getDownRequests().first() : null;
        } else {
            targetFloor = decideMove(elevator);
        }
        data.setTargetFloor(targetFloor);
    }

    private Integer decideMove(Elevator elevator) {
        Integer upMove = elevator.getUpRequests().isEmpty() ? null : elevator.getUpRequests().first();
        Integer downMove = elevator.getDownRequests().isEmpty() ? null : elevator.getDownRequests().first();

        if (upMove == null && downMove == null){
            return null;
        }

        if (upMove == null) {
            return downMove;
        }

        if (downMove == null) {
            return upMove;
        }

        int upDistance = Math.abs(elevator.getCurrentFloor() - upMove);
        int downDistance = Math.abs(elevator.getCurrentFloor() - downMove);
        if (upDistance <= downDistance) {
            return upMove;
        } else {
            return downMove;
        }
    }
}
