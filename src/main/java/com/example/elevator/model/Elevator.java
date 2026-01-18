package com.example.elevator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Elevator {
    private Integer id;
    private Integer minFloor;
    private Integer maxFloor;
    @Builder.Default
    private Integer currentFloor = 0;
    @Builder.Default
    private ElevatorStatus elevatorStatus = ElevatorStatus.OPERATING;
    @Builder.Default
    private ElevatorState elevatorState = ElevatorState.NOT_MOVING;
    @Builder.Default
    private DoorState doorState = DoorState.CLOSED;

    public void startOpening() {
        if (canStartOpening()) {
            doorState = DoorState.OPENING;
        }
    }

    private boolean canStartOpening() {
        return isOperating()
                && (DoorState.CLOSED == doorState || DoorState.CLOSING == doorState);
    }

    public void openDoors() {
        if(canOpen()) {
            doorState = DoorState.OPENED;
        }
    }

    private boolean canOpen() {
        return isOperating() && DoorState.OPENING == doorState;
    }

    public void startClosing() {
        if (canStartClosing()) {
            doorState = DoorState.CLOSING;
        }
    }

    private boolean canStartClosing() {
        return isOperating() && DoorState.OPENED == doorState;
    }

    public void closeDoors() {
        if(canClose()) {
            doorState = DoorState.CLOSED;
        }
    }

    private boolean canClose() {
        return isOperating() && DoorState.CLOSING == doorState;
    }

    public void startMovingUp() {
        if (canStartMoving()) {
            elevatorState = ElevatorState.MOVING_UP;
        }
    }

    private boolean canStartMoving() {
        return isOperating()
                && ElevatorState.NOT_MOVING == elevatorState
                && DoorState.CLOSED == doorState;
    }

    public void startMovingDown() {
        if (canStartMoving()) {
            elevatorState = ElevatorState.MOVING_DOWN;
        }
    }

    public void stop() {
        elevatorState = ElevatorState.NOT_MOVING;
    }

    public void moveOneUp() {
        if (canMoveUp()) {
            currentFloor++;
        }
    }

    private boolean canMoveUp() {
        return isOperating()
                && ElevatorState.MOVING_UP == elevatorState
                && currentFloor < maxFloor;
    }

    public void moveOneDown() {
        if(canMoveDown()) {
            currentFloor--;
        }
    }

    private boolean canMoveDown() {
        return isOperating()
                && ElevatorState.MOVING_DOWN == elevatorState
                && currentFloor > minFloor;
    }

    private boolean isOperating() {
        return ElevatorStatus.OPERATING == elevatorStatus;
    }
}
