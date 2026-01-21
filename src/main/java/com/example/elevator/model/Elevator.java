package com.example.elevator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

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
    @Builder.Default
    private NavigableSet<Integer> upRequests = new ConcurrentSkipListSet<>();
    @Builder.Default
    private NavigableSet<Integer> downRequests = new ConcurrentSkipListSet<>();

    private volatile boolean openDoorTrigger = false;
    private volatile boolean closeDoorTrigger = false;

    public void startOpening() {
        if (canStartOpening()) {
            doorState = DoorState.OPENING;
        }
    }

    public boolean canStartOpening() {
        return isOperating()
                && ElevatorState.NOT_MOVING == elevatorState
                && (DoorState.CLOSED == doorState || DoorState.CLOSING == doorState);
    }

    public void openDoors() {
        if(canOpen()) {
            doorState = DoorState.OPENED;
        }
    }

    public boolean canOpen() {
        return isOperating()
                && ElevatorState.NOT_MOVING == elevatorState
                && DoorState.OPENING == doorState;
    }

    public void startClosing() {
        if (canStartClosing()) {
            doorState = DoorState.CLOSING;
        }
    }

    public boolean canStartClosing() {
        return isOperating()
                && ElevatorState.NOT_MOVING == elevatorState
                && DoorState.OPENED == doorState;
    }

    public void closeDoors() {
        if(canClose()) {
            doorState = DoorState.CLOSED;
        }
    }

    public boolean canClose() {
        return isOperating() && DoorState.CLOSING == doorState;
    }

    public void startMovingUp() {
        if (canStartMoving()) {
            elevatorState = ElevatorState.MOVING_UP;
        }
    }

    public boolean canStartMoving() {
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

    public boolean canMoveUp() {
        return isOperating()
                && ElevatorState.MOVING_UP == elevatorState
                && currentFloor < maxFloor;
    }

    public void moveOneDown() {
        if(canMoveDown()) {
            currentFloor--;
        }
    }

    public boolean canMoveDown() {
        return isOperating()
                && ElevatorState.MOVING_DOWN == elevatorState
                && currentFloor > minFloor;
    }

    public boolean isOperating() {
        return ElevatorStatus.OPERATING == elevatorStatus;
    }

    public boolean isMovingUp() {
        return ElevatorState.MOVING_UP == elevatorState;
    }

    public boolean isMovingDown() {
        return ElevatorState.MOVING_DOWN == elevatorState;
    }

    public boolean isMoving() {
        return isMovingUp() || isMovingDown();
    }

    public void addUpRequest(int request) {
        upRequests.add(request);
    }

    public void removeUpRequest() {
        upRequests.pollFirst();
    }

    public void addDownRequest(int request) {
        downRequests.add(request);
    }

    public void removeDownRequest() {
        downRequests.pollLast();
    }

    public List<Integer> getUpRequests() {
        return upRequests.stream().toList();
    }

    public List<Integer> getDownRequestsDesc() {
        return downRequests.stream().sorted(Comparator.reverseOrder()).toList();
    }

    public void triggerOpenDoor() {
        openDoorTrigger = true;
    }

    public void triggerCloseDoor() {
        closeDoorTrigger = true;
    }

    public void clearDoorTriggers() {
        openDoorTrigger = false;
        closeDoorTrigger = false;
    }
}
