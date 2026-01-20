package com.example.elevator.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class ElevatorEngine {

    private static final int DOOR_OPEN_CLOSE_CYCLES = 3;
    private static final int OPEN_DOOR_CYCLES = 4;
    private static final int ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES = 5;

    private final Elevator elevator;
    private int doorTimer = 0;
    private int openDoorTimer = 0;
    private int movementTimer = 0;
    private Integer targetFloor = null;

    public void runCycle() {
        log.debug("Elevator {} running cycle", elevator.getId());
        if (isElevatorNotOperating()) return;
        updateTarget();
        if (areDoorsOpening()) return;
        if (areDoorsClosing()) return;
        if (shouldOpenDoors()) return;
        if (shouldCloseDoors()) return;
        if (areDoorsOpened()) return;
        if (shouldStartClosingDoors()) return;
        if (isElevatorMoving()) return;
        if (shouldProcessMovingOneFloor()) return;
        if (shouldStartMoving()) return;
    }

    private void updateTarget() {
        if (elevator.isMovingUp()) {
            targetFloor = !elevator.getUpRequests().isEmpty() ? elevator.getUpRequests().get(0) : null;
        } else if (elevator.isMovingDown()) {
            targetFloor = !elevator.getDownRequestsDesc().isEmpty() ? elevator.getDownRequestsDesc().get(0) : null;
        } else {
            targetFloor = decideMove();
        }
    }

    private boolean shouldStartMoving() {
        if(targetFloor != null && ElevatorState.NOT_MOVING == elevator.getElevatorState()){
            startMoving(targetFloor);
            return true;
        }
        return false;
    }

    private boolean shouldProcessMovingOneFloor() {
        if (reachedFloor()) {
            updateCurrentFloor();

            if (reachedGoalFloor()) {
                processGoalFloor();
            } else {
                processIntermediateFloor();
            }
            return true;
        }
        return false;
    }

    private boolean shouldOpenDoors() {
        if (doorTimer == 0 && DoorState.OPENING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are open", elevator.getId());
            elevator.openDoors();
            openDoorTimer += OPEN_DOOR_CYCLES;
            return true;
        }
        return false;
    }

    private boolean shouldCloseDoors() {
        if (doorTimer == 0 && DoorState.CLOSING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are closed", elevator.getId());
            elevator.closeDoors();
            return true;
        }
        return false;
    }

    private boolean shouldStartClosingDoors() {
        if (openDoorTimer == 0 && DoorState.OPENED == elevator.getDoorState()) {
            elevator.startClosing();
            doorTimer += DOOR_OPEN_CLOSE_CYCLES;
            return true;
        }
        return false;
    }

    private boolean isElevatorMoving() {
        if (movementTimer > 0) {
            log.info("Elevator {} - moving", elevator.getId());
            movementTimer--;
            return true;
        }
        return false;
    }

    private boolean areDoorsClosing() {
        if (doorTimer > 0 && DoorState.CLOSING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are closing", elevator.getId());
            doorTimer--;
            return true;
        }
        return false;
    }

    private boolean areDoorsOpening() {
        if (doorTimer > 0 && DoorState.OPENING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are opening", elevator.getId());
            doorTimer--;
            return true;
        }
        return false;
    }

    private boolean areDoorsOpened() {
        if (openDoorTimer > 0 && DoorState.OPENED == elevator.getDoorState()){
            log.info("Elevator {} - doors remain open", elevator.getId());
            openDoorTimer--;
            return true;
        }
        return false;
    }

    private boolean isElevatorNotOperating() {
        if (!elevator.isOperating()) {
            log.info("Elevator {} - not in operation", elevator.getId());
            return true;
        }
        return false;
    }

    private boolean reachedGoalFloor() {
        return elevator.getCurrentFloor().equals(targetFloor);
    }

    private void updateCurrentFloor() {
        if (elevator.isMovingUp()) {
            elevator.moveOneUp();
        } else {
            elevator.moveOneDown();
        }
    }

    private boolean reachedFloor() {
        return movementTimer == 0 && elevator.isMoving();
    }

    private void processIntermediateFloor() {
        log.info("Elevator {} - passing floor: {}", elevator.getId(), elevator.getCurrentFloor());
        movementTimer += ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;
    }

    private void processGoalFloor() {
        if (elevator.isMovingUp()) {
            elevator.removeUpRequest();
        } else {
            elevator.removeDownRequest();
        }
        log.info("Elevator {} - reached floor: {}", elevator.getId(), elevator.getCurrentFloor());
        elevator.stop();
        targetFloor = null;
        elevator.startOpening();
        doorTimer += DOOR_OPEN_CLOSE_CYCLES;
    }

    private void startMoving(int nextFloor) {
        log.info("Elevator {} - moving to floor: {}", elevator.getId(), nextFloor);
        if (nextFloor > elevator.getCurrentFloor()) {
            elevator.startMovingUp();
        } else {
            elevator.startMovingDown();
        }
        movementTimer += ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;
    }

    private Integer decideMove() {
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
