package com.example.elevator.service;

import com.example.elevator.model.DoorState;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import com.example.elevator.model.OperationalData;
import org.junit.jupiter.api.Test;

import static com.example.elevator.model.ElevatorEngine.DOOR_OPEN_CLOSE_CYCLES;
import static com.example.elevator.model.ElevatorEngine.ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES;
import static org.junit.jupiter.api.Assertions.*;

class EngineMovementServiceTest {

    private final EngineMovementService service = new EngineMovementService();

    @Test
    public void endOfUpDirection() {
        //given
        var elevator = Elevator.builder()
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        var data = new OperationalData();

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(ElevatorDirection.NOT_MOVING, elevator.getElevatorDirection());
    }

    @Test
    public void endOfDownDirection() {
        //given
        var elevator = Elevator.builder()
                .elevatorDirection(ElevatorDirection.MOVING_DOWN)
                .build();
        var data = new OperationalData();

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(ElevatorDirection.NOT_MOVING, elevator.getElevatorDirection());
    }

    @Test
    public void elevatorIsMoving() {
        //given
        var elevator = Elevator.builder()
                .id(1)
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addUpRequest(3);
        var data = new OperationalData();
        data.setMovementTimer(2);

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(1, data.getMovementTimer());
    }

    @Test
    public void reachedIntermediateFloor() {
        //given
        var elevator = Elevator.builder()
                .id(1)
                .currentFloor(1)
                .maxFloor(5)
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addUpRequest(4);
        var data = new OperationalData();
        data.setTargetFloor(4);

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES, data.getMovementTimer());
        assertEquals(2, elevator.getCurrentFloor());
        assertEquals(4, elevator.getUpRequests().first());
        assertNotNull(data.getTargetFloor());
        assertEquals(DoorState.CLOSED, elevator.getDoorState());
        assertEquals(0, data.getDoorTimer());
    }

    @Test
    public void reachedTargetFloor() {
        //given
        var elevator = Elevator.builder()
                .id(1)
                .currentFloor(3)
                .maxFloor(5)
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addUpRequest(4);
        var data = new OperationalData();
        data.setTargetFloor(4);

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(0, data.getMovementTimer());
        assertEquals(4, elevator.getCurrentFloor());
        assertTrue(elevator.getUpRequests().isEmpty());
        assertNull(data.getTargetFloor());
        assertEquals(DoorState.OPENING, elevator.getDoorState());
        assertEquals(DOOR_OPEN_CLOSE_CYCLES, data.getDoorTimer());
    }

    @Test
    public void shouldStartMoving() {
        //given
        var elevator = Elevator.builder()
                .id(1)
                .currentFloor(3)
                .maxFloor(5)
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(4);
        var data = new OperationalData();
        data.setTargetFloor(4);

        //when
        service.manageMovement(data, elevator);

        //then
        assertEquals(ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES, data.getMovementTimer());
        assertTrue(elevator.isMovingUp());
    }
}