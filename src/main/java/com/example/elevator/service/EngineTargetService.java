package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.OperationalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EngineTargetService {

    public void updateTarget(OperationalData data, Elevator elevator) {
        Integer targetFloor = null;
        if (elevator.isMovingUp()) {
            targetFloor = !elevator.getUpRequests().isEmpty() ? elevator.getUpRequests().get(0) : null;
        } else if (elevator.isMovingDown()) {
            targetFloor = !elevator.getDownRequestsDesc().isEmpty() ? elevator.getDownRequestsDesc().get(0) : null;
        } else {
            targetFloor = decideMove(elevator);
        }
        data.setTargetFloor(targetFloor);
    }

    private Integer decideMove(Elevator elevator) {
        Integer upMove = elevator.getUpRequests().isEmpty() ? null : elevator.getUpRequests().get(0);
        Integer downMove = elevator.getDownRequestsDesc().isEmpty() ? null : elevator.getDownRequestsDesc().get(0);

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
