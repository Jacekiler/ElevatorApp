package com.example.elevator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Builder
public class Elevator {
    private Integer id;
    private Integer minFloor;
    private Integer maxFloor;

    @Builder.Default
    private AtomicInteger currentFloor = new AtomicInteger(0);
    @Builder.Default
    private ElevatorStatus elevatorStatus = ElevatorStatus.OPERATING;
    @Builder.Default
    private volatile ElevatorDirection elevatorDirection = ElevatorDirection.NOT_MOVING;
    @Builder.Default
    private DoorState doorState = DoorState.CLOSED;
    @Builder.Default
    private NavigableSet<Integer> upRequests = new ConcurrentSkipListSet<>();
    @Builder.Default
    private NavigableSet<Integer> downRequests = new ConcurrentSkipListSet<>(Comparator.reverseOrder());

    @Builder.Default
    private AtomicBoolean openDoorTrigger = new AtomicBoolean(false);
    @Builder.Default
    private AtomicBoolean closeDoorTrigger = new AtomicBoolean(false);

    public Integer getCurrentFloor() {
        return currentFloor.get();
    }

    public boolean consumeOpenDoorTrigger() {
        return openDoorTrigger.getAndSet(false);
    }

    public boolean consumeCloseDoorTrigger() {
        return closeDoorTrigger.getAndSet(false);
    }

    public void startOpening() {
        if (canStartOpening()) {
            doorState = DoorState.OPENING;
        }
    }

    public boolean canStartOpening() {
        return isOperating()
                && ElevatorDirection.NOT_MOVING == elevatorDirection
                && (DoorState.CLOSED == doorState || DoorState.CLOSING == doorState);
    }

    public void openDoors() {
        if(canOpen()) {
            doorState = DoorState.OPENED;
        }
    }

    public boolean canOpen() {
        return isOperating()
                && DoorState.OPENING == doorState;
    }

    public void startClosing() {
        if (canStartClosing()) {
            doorState = DoorState.CLOSING;
        }
    }

    public boolean canStartClosing() {
        return isOperating()
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
            elevatorDirection = ElevatorDirection.MOVING_UP;
        }
    }

    public boolean canStartMoving() {
        return isOperating()
                && DoorState.CLOSED == doorState;
    }

    public void startMovingDown() {
        if (canStartMoving()) {
            elevatorDirection = ElevatorDirection.MOVING_DOWN;
        }
    }

    public void stop() {
        elevatorDirection = ElevatorDirection.NOT_MOVING;
    }

    public void moveOneUp() {
        if (canMoveUp()) {
            currentFloor.incrementAndGet();
        }
    }

    public boolean canMoveUp() {
        return isOperating()
                && ElevatorDirection.MOVING_UP == elevatorDirection
                && currentFloor.get() < maxFloor;
    }

    public void moveOneDown() {
        if(canMoveDown()) {
            currentFloor.decrementAndGet();
        }
    }

    public boolean canMoveDown() {
        return isOperating()
                && ElevatorDirection.MOVING_DOWN == elevatorDirection
                && currentFloor.get() > minFloor;
    }

    public boolean isOperating() {
        return ElevatorStatus.OPERATING == elevatorStatus;
    }

    public boolean isMovingUp() {
        return ElevatorDirection.MOVING_UP == elevatorDirection;
    }

    public boolean isMovingDown() {
        return ElevatorDirection.MOVING_DOWN == elevatorDirection;
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
        downRequests.pollFirst();
    }

    public void triggerOpenDoor() {
        openDoorTrigger.set(true);
    }

    public void triggerCloseDoor() {
        closeDoorTrigger.set(true);
    }

    public void clearDoorTriggers() {
        openDoorTrigger.getAndSet(false);
        closeDoorTrigger.getAndSet(false);
    }
}
