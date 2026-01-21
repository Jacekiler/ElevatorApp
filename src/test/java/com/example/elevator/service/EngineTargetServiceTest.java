package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import com.example.elevator.model.OperationalData;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EngineTargetServiceTest {

    private final EngineTargetService service = new EngineTargetService();

    @Test
    public void shouldGoUpWhileMovingUp() {
        //given
        var data = new OperationalData();
        var expectedTarget = 2;
        var elevator = Elevator.builder()
                .currentFloor(new AtomicInteger(1))
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addUpRequest(3);
        elevator.addUpRequest(expectedTarget);
        elevator.addDownRequest(1);
        elevator.addDownRequest(2);

        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(expectedTarget, data.getTargetFloor());
    }

    @Test
    public void shouldStopWhileMovingUp() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .currentFloor(new AtomicInteger(1))
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addDownRequest(1);
        elevator.addDownRequest(2);

        //when
        service.updateTarget(data, elevator);

        //then
        assertNull(data.getTargetFloor());
    }

    @Test
    public void shouldGoDownWhileMovingDown() {
        //given
        var data = new OperationalData();
        var expectedTarget = -1;
        var elevator = Elevator.builder()
                .currentFloor(new AtomicInteger(1))
                .elevatorDirection(ElevatorDirection.MOVING_DOWN)
                .build();
        elevator.addUpRequest(2);
        elevator.addUpRequest(3);
        elevator.addDownRequest(expectedTarget);

        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(expectedTarget, data.getTargetFloor());
    }

    @Test
    public void shouldStopWhileMovingDown() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .currentFloor(new AtomicInteger(1))
                .elevatorDirection(ElevatorDirection.MOVING_DOWN)
                .build();
        elevator.addUpRequest(2);
        elevator.addUpRequest(3);

        //when
        service.updateTarget(data, elevator);

        //then
        assertNull(data.getTargetFloor());
    }

    @Test
    public void shouldGoDownFirstWhileStopped() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .maxFloor(5)
                .minFloor(-2)
                .currentFloor(new AtomicInteger(1))
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(3);
        elevator.addUpRequest(4);
        elevator.addDownRequest(0);
        elevator.addDownRequest(-1);


        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(0, data.getTargetFloor());
    }

    @Test
    public void shouldGoUpFirstWhileStopped() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .maxFloor(5)
                .minFloor(-2)
                .currentFloor(new AtomicInteger(2))
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(3);
        elevator.addUpRequest(4);
        elevator.addDownRequest(0);
        elevator.addDownRequest(-1);

        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(3, data.getTargetFloor());
    }

    @Test
    public void shouldStayIfNoRequest() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .maxFloor(5)
                .minFloor(-2)
                .currentFloor(new AtomicInteger(2))
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();

        //when
        service.updateTarget(data, elevator);

        //then
        assertNull(data.getTargetFloor());
    }

    @Test
    public void shouldGoUpIfOnlyUpRequest() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .maxFloor(5)
                .minFloor(-2)
                .currentFloor(new AtomicInteger(2))
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(3);

        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(3, data.getTargetFloor());
    }

    @Test
    public void shouldGoDownIfOnlyDownRequest() {
        //given
        var data = new OperationalData();
        var elevator = Elevator.builder()
                .maxFloor(5)
                .minFloor(-2)
                .currentFloor(new AtomicInteger(2))
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addDownRequest(1);

        //when
        service.updateTarget(data, elevator);

        //then
        assertEquals(1, data.getTargetFloor());
    }
}