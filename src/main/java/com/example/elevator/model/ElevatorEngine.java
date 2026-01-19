package com.example.elevator.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class ElevatorEngine {

    private static final int DOOR_OPEN_CLOSE_CYCLES = 3;
    private static final int ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES = 5;

    private final Elevator elevator;
    private int doorTimer = 0;
    private int movementTimer = 0;

    public void runCycle() {
        log.debug("Elevator {} running cycle", elevator.getId());
        if (!elevator.isOperating()) {
            log.info("Elevator {} is not operation", elevator.getId());
            return;
        }

        if (doorTimer > 0) {
            log.info("Doors are opening/closing");
            doorTimer--;
            return;
        }

        if (movementTimer > 0) {
            log.info("Elevator is moving");
            movementTimer--;
            return;
        }

        if (movementTimer == 0 && elevator.isMoving()) {
            if (elevator.isMovingUp()) {
                elevator.moveOneUp();
            } else {
                elevator.moveOneDown();
            }

            if (elevator.getCurrentFloor() == decideMove()) {
                if (elevator.isMovingUp()) {
                    elevator.removeUpRequest();
                } else {
                    elevator.removeDownRequest();
                }
                log.info("Elevator reached floor: {}", elevator.getCurrentFloor());
                elevator.stop();
            } else {
                log.info("Elevator is on floor: {}", elevator.getCurrentFloor());
                movementTimer += ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;
            }
            return;
        }

        if(hasRequests()){
            var nextFloor = decideMove();
            startMoving(nextFloor);
            return;
        }
    }

    private boolean hasRequests() {
        return !elevator.getUpRequests().isEmpty() || !elevator.getDownRequests().isEmpty();
    }

    private void startMoving(int nextFloor) {
        log.info("Elevator moving to floor: {}", nextFloor);
        if (nextFloor > elevator.getCurrentFloor()) {
            elevator.startMovingUp();
        } else {
            elevator.startMovingDown();
        }
        movementTimer += ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;
    }

    private int decideMove() {
        Integer upMove = elevator.getUpRequests().isEmpty() ? null : elevator.getUpRequests().get(0);
        Integer downMove = elevator.getDownRequestsDesc().isEmpty() ? null : elevator.getDownRequestsDesc().get(0);

        if (upMove == null && downMove == null){
            throw new RuntimeException("");
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
