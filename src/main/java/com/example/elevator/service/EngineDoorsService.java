package com.example.elevator.service;

import com.example.elevator.model.DoorState;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.OperationalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.elevator.model.ElevatorEngine.DOOR_OPEN_CLOSE_CYCLES;
import static com.example.elevator.model.ElevatorEngine.OPEN_DOOR_CYCLES;

@Slf4j
@Service
public class EngineDoorsService {
    
    public boolean manageDoors(OperationalData data, Elevator elevator) {
        if (shouldHandleDoorTrigger(data, elevator)) return true;
        if (areDoorsOpening(data, elevator)) return true;
        if (areDoorsClosing(data, elevator)) return true;
        if (shouldOpenDoors(data, elevator)) return true;
        if (shouldCloseDoors(data, elevator)) return true;
        if (areDoorsOpened(data, elevator)) return true;
        if (shouldStartClosingDoors(data, elevator)) return true;
        return false;
    }

    private boolean shouldHandleDoorTrigger(OperationalData data, Elevator elevator) {
        var handled = shouldOpenDoorImmediately(data, elevator) || (shouldCloseDoorImmediately(data, elevator));
        elevator.clearDoorTriggers();
        return handled;
    }

    private boolean shouldOpenDoorImmediately(OperationalData data, Elevator elevator) {
        if (elevator.isOpenDoorTrigger() && elevator.canStartOpening()) {
            elevator.startOpening();
            data.setDoorTimer(DOOR_OPEN_CLOSE_CYCLES - data.getDoorTimer());
            return true;
        }
        return false;
    }

    private boolean shouldCloseDoorImmediately(OperationalData data, Elevator elevator) {
        if (elevator.isCloseDoorTrigger() && elevator.canStartClosing()) {
            elevator.startClosing();
            data.setDoorTimer(DOOR_OPEN_CLOSE_CYCLES);
            data.setOpenDoorTimer(0);
            return true;
        }
        return false;
    }

    private boolean areDoorsOpening(OperationalData data, Elevator elevator) {
        if (data.getDoorTimer() > 0 && DoorState.OPENING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are opening", elevator.getId());
            data.setDoorTimer(data.getDoorTimer() - 1);
            return true;
        }
        return false;
    }
    
    private boolean areDoorsClosing(OperationalData data, Elevator elevator) {
        if (data.getDoorTimer() > 0 && DoorState.CLOSING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are closing", elevator.getId());
            data.setDoorTimer(data.getDoorTimer() - 1);
            return true;
        }
        return false;
    }

  

    private boolean shouldOpenDoors(OperationalData data, Elevator elevator) {
        if (data.getDoorTimer() == 0 && DoorState.OPENING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are open", elevator.getId());
            elevator.openDoors();
            data.setOpenDoorTimer(OPEN_DOOR_CYCLES);
            return true;
        }
        return false;
    }

    private boolean shouldCloseDoors(OperationalData data, Elevator elevator) {
        if (data.getDoorTimer() == 0 && DoorState.CLOSING == elevator.getDoorState()) {
            log.info("Elevator {} - doors are closed", elevator.getId());
            elevator.closeDoors();
            return true;
        }
        return false;
    }

    private boolean areDoorsOpened(OperationalData data, Elevator elevator) {
        if (data.getOpenDoorTimer() > 0 && DoorState.OPENED == elevator.getDoorState()){
            log.info("Elevator {} - doors remain open", elevator.getId());
            data.setOpenDoorTimer(data.getOpenDoorTimer() - 1);
            return true;
        }
        return false;
    }

    private boolean shouldStartClosingDoors(OperationalData data, Elevator elevator) {
        if (data.getOpenDoorTimer() == 0 && DoorState.OPENED == elevator.getDoorState()) {
            elevator.startClosing();
            data.setDoorTimer(DOOR_OPEN_CLOSE_CYCLES);
            return true;
        }
        return false;
    }
}
