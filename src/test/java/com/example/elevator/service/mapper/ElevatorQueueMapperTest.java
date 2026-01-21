package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorQueueMapperTest {

    private final ElevatorQueueMapper mapper = new ElevatorQueueMapper();

    @Test
    public void movingUp() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(1)
                .elevatorDirection(ElevatorDirection.MOVING_UP)
                .build();
        elevator.addUpRequest(4);
        elevator.addUpRequest(3);
        elevator.addDownRequest(0);
        elevator.addDownRequest(-1);

        //when
        var result = mapper.getRequests(elevator);

        //then
        assertEquals(4, result.size());
        assertEquals(3, result.get(0));
        assertEquals(4, result.get(1));
        assertEquals(0, result.get(2));
        assertEquals(-1, result.get(3));
    }

    @Test
    public void movingDown() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(2)
                .elevatorDirection(ElevatorDirection.MOVING_DOWN)
                .build();
        elevator.addUpRequest(4);
        elevator.addUpRequest(3);
        elevator.addDownRequest(0);
        elevator.addDownRequest(-1);

        //when
        var result = mapper.getRequests(elevator);

        //then
        assertEquals(4, result.size());
        assertEquals(0, result.get(0));
        assertEquals(-1, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(4, result.get(3));
    }

    @Test
    public void notMovingPickUpFirst() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(2)
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(4);
        elevator.addUpRequest(3);
        elevator.addDownRequest(1);
        elevator.addDownRequest(-1);

        //when
        var result = mapper.getRequests(elevator);

        //then
        assertEquals(4, result.size());
        assertEquals(3, result.get(0));
        assertEquals(4, result.get(1));
        assertEquals(1, result.get(2));
        assertEquals(-1, result.get(3));
    }

    @Test
    public void notMovingPickDownFirst() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(1)
                .elevatorDirection(ElevatorDirection.NOT_MOVING)
                .build();
        elevator.addUpRequest(4);
        elevator.addUpRequest(3);
        elevator.addDownRequest(0);
        elevator.addDownRequest(-1);

        //when
        var result = mapper.getRequests(elevator);

        //then
        assertEquals(4, result.size());
        assertEquals(0, result.get(0));
        assertEquals(-1, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(4, result.get(3));
    }
}