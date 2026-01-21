package com.example.elevator.service;

import com.example.elevator.model.DoorState;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import com.example.elevator.model.OperationalData;
import org.junit.jupiter.api.Test;

import static com.example.elevator.model.ElevatorEngine.DOOR_OPEN_CLOSE_CYCLES;
import static com.example.elevator.model.ElevatorEngine.OPEN_DOOR_CYCLES;
import static org.junit.jupiter.api.Assertions.*;

class EngineDoorsServiceTest {

    private final EngineDoorsService service = new EngineDoorsService();

    @Test
    public void shouldHandleDoorTrigger(){
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .openDoorTrigger(true)
                .doorState(DoorState.CLOSING)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertFalse(elevator.isOpenDoorTrigger());
        assertFalse(elevator.isCloseDoorTrigger());
        assertEquals(DoorState.OPENING, elevator.getDoorState());
        assertEquals(DOOR_OPEN_CLOSE_CYCLES, data.getDoorTimer());
    }

    @Test
    public void areDoorsOpening(){
        //given
        var data = new OperationalData();
        data.setDoorTimer(2);
        var elevator = Elevator.builder()
                .doorState(DoorState.OPENING)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(1, data.getDoorTimer());
    }

    @Test
    public void areDoorsClosing(){
        //given
        var data = new OperationalData();
        data.setDoorTimer(2);
        var elevator = Elevator.builder()
                .doorState(DoorState.CLOSING)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(1, data.getDoorTimer());
    }

    @Test
    public void shouldOpenDoors(){
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .doorState(DoorState.OPENING)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(DoorState.OPENED, elevator.getDoorState());
        assertEquals(OPEN_DOOR_CYCLES, data.getOpenDoorTimer());
    }

    @Test
    public void shouldCloseDoors(){
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .doorState(DoorState.CLOSING)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(DoorState.CLOSED, elevator.getDoorState());
    }

    @Test
    public void areDoorsOpened(){
        //given
        var data = new OperationalData();
        data.setOpenDoorTimer(2);
        var elevator = Elevator.builder()
                .doorState(DoorState.OPENED)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(1, data.getOpenDoorTimer());
    }

    @Test
    public void shouldStartClosingDoors(){
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .doorState(DoorState.OPENED)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertTrue(result);
        assertEquals(DOOR_OPEN_CLOSE_CYCLES, data.getDoorTimer());
        assertEquals(DoorState.CLOSING, elevator.getDoorState());
    }

    @Test
    public void shouldNotDoAnythingWithDoors(){
        //given
        var data = new OperationalData();
        data.setMovementTimer(2);
        var elevator = Elevator.builder()
                .doorState(DoorState.CLOSED)
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();

        //when
        var result = service.manageDoors(data, elevator);

        //then
        assertFalse(result);
        assertEquals(0, data.getDoorTimer());
        assertEquals(0, data.getOpenDoorTimer());
    }
}