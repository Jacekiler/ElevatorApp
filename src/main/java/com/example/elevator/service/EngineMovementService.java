package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import com.example.elevator.model.OperationalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.elevator.model.ElevatorEngine.DOOR_OPEN_CLOSE_CYCLES;
import static com.example.elevator.model.ElevatorEngine.ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;

@Slf4j
@Service
public class EngineMovementService {

    public boolean manageMovement(OperationalData data, Elevator elevator) {
        checkEndOfDirection(elevator);
        if (isElevatorMoving(data, elevator)) return true;
        if (shouldProcessMovingOneFloor(data, elevator)) return true;
        if (shouldStartMoving(data, elevator)) return true;
        return false;
    }

    private void checkEndOfDirection(Elevator elevator) {
        if (elevator.isMovingUp() && (elevator.getUpRequests().isEmpty() || elevator.getCurrentFloor().equals(elevator.getMaxFloor()))) {
            elevator.stop();
        }

        if (elevator.isMovingDown() && (elevator.getDownRequests().isEmpty() || elevator.getCurrentFloor().equals(elevator.getMinFloor()))) {
            elevator.stop();
            return;
        }
    }

    private boolean isElevatorMoving(OperationalData data, Elevator elevator) {
        if (data.getMovementTimer() > 0) {
            log.info("Elevator {} - moving", elevator.getId());
            data.setMovementTimer(data.getMovementTimer() - 1);
            return true;
        }
        return false;
    }

    private boolean shouldProcessMovingOneFloor(OperationalData data, Elevator elevator) {
        if (reachedFloor(data, elevator)) {
            updateCurrentFloor(elevator);

            if (reachedGoalFloor(data, elevator)) {
                processGoalFloor(data, elevator);
            } else {
                processIntermediateFloor(data, elevator);
            }
            return true;
        }
        return false;
    }

    private boolean reachedFloor(OperationalData data, Elevator elevator) {
        return data.getMovementTimer() == 0 && elevator.isMoving();
    }

    private void updateCurrentFloor(Elevator elevator) {
        if (elevator.isMovingUp()) {
            elevator.moveOneUp();
        } else {
            elevator.moveOneDown();
        }
        log.info("Current floor: {}", elevator.getCurrentFloor());
    }

    private boolean reachedGoalFloor(OperationalData data, Elevator elevator) {
        return elevator.getCurrentFloor().equals(data.getTargetFloor());
    }

    private void processGoalFloor(OperationalData data, Elevator elevator) {
        if (elevator.isMovingUp()) {
            elevator.removeUpRequest();
        } else {
            elevator.removeDownRequest();
        }
        log.info("Elevator {} - reached floor: {}", elevator.getId(), elevator.getCurrentFloor());
        log.info("Elevator {} - UP queue: {}", elevator.getId(), elevator.getUpRequests());
        log.info("Elevator {} - DOWN queue: {}", elevator.getId(), elevator.getDownRequests());
        data.setTargetFloor(null);
        elevator.startOpening();
        data.setDoorTimer(DOOR_OPEN_CLOSE_CYCLES);
    }

    private void processIntermediateFloor(OperationalData data, Elevator elevator) {
        log.info("Elevator {} - passing floor: {}", elevator.getId(), elevator.getCurrentFloor());
        data.setMovementTimer(ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES);
    }

    private boolean shouldStartMoving(OperationalData data, Elevator elevator) {
        if(data.getTargetFloor() != null && ElevatorDirection.NOT_MOVING == elevator.getElevatorDirection()){
            startMoving(data, elevator);
            return true;
        }
        return false;
    }

    private void startMoving(OperationalData data, Elevator elevator) {
        var nextFloor = data.getTargetFloor();
        log.info("Elevator {} - moving to floor: {}", elevator.getId(), nextFloor);
        if (nextFloor > elevator.getCurrentFloor()) {
            elevator.startMovingUp();
            log.info("Elevator {} start moving up", elevator.getId());
        } else {
            elevator.startMovingDown();
            log.info("Elevator {} start moving down", elevator.getId());
        }
        data.setMovementTimer(ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES);
    }
}
