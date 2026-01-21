package com.example.elevator.service;

import com.example.elevator.model.CallDirection;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.example.elevator.model.ElevatorDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorRequestsQueueServiceTest {

    private final ElevatorRequestsQueueService service = new ElevatorRequestsQueueService();

    @ParameterizedTest
    @MethodSource("upRequestsArguments")
    public void addUpRequestsFrommOutsideCall(Elevator elevator, int request, CallDirection direction) {
        //given
        var upRequestNumber = elevator.getUpRequests().size();
        var downRequestNumber = elevator.getDownRequests().size();
        var expectedUpRequestsNumber = upRequestNumber + 1;

        //when
        service.addCallRequest(elevator, request, direction);

        //then
        assertEquals(downRequestNumber, elevator.getDownRequests().size());
        assertEquals(expectedUpRequestsNumber, elevator.getUpRequests().size());
    }

    static Stream<Arguments> upRequestsArguments() {
        return Stream.of(
                Arguments.of(buildElevator(MOVING_UP, 1,List.of(3), List.of()), 4, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_UP, 1, List.of(3), List.of()), 2, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_UP, 1, List.of(3), List.of()), 4, CallDirection.DOWN),
                Arguments.of(buildElevator(NOT_MOVING, 1, List.of(), List.of()), 4, CallDirection.UP),
                Arguments.of(buildElevator(NOT_MOVING, 1, List.of(), List.of()), 4, CallDirection.DOWN),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), 3, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), 1, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), 3, CallDirection.DOWN)
        );
    }

    @ParameterizedTest
    @MethodSource("downRequestsArguments")
    public void addDownRequestsFrommOutsideCall(Elevator elevator, int request, CallDirection direction) {
        //given
        var upRequestNumber = elevator.getUpRequests().size();
        var downRequestNumber = elevator.getDownRequests().size();
        var expectedDownRequestsNumber = downRequestNumber + 1;

        //when
        service.addCallRequest(elevator, request, direction);

        //then
        assertEquals(expectedDownRequestsNumber, elevator.getDownRequests().size());
        assertEquals(upRequestNumber, elevator.getUpRequests().size());
    }

    static Stream<Arguments> downRequestsArguments() {
        return Stream.of(
                Arguments.of(buildElevator(MOVING_UP, 1,List.of(3), List.of()), 0, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_UP, 1, List.of(3), List.of()), 2, CallDirection.DOWN),
                Arguments.of(buildElevator(MOVING_UP, 1, List.of(3), List.of()), 0, CallDirection.DOWN),
                Arguments.of(buildElevator(NOT_MOVING, 1, List.of(), List.of()), 0, CallDirection.UP),
                Arguments.of(buildElevator(NOT_MOVING, 1, List.of(), List.of()), 0, CallDirection.DOWN),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), -1, CallDirection.UP),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), 1, CallDirection.DOWN),
                Arguments.of(buildElevator(MOVING_DOWN, 2, List.of(), List.of(0)), -1, CallDirection.DOWN)
        );
    }

    private static Elevator buildElevator(ElevatorDirection direction, int currFloor, List<Integer> upQueue, List<Integer> downQueue) {
        var elevator = Elevator.builder()
                .currentFloor(currFloor)
                .elevatorDirection(direction)
                .build();
        upQueue.forEach(elevator::addUpRequest);
        downQueue.forEach(elevator::addDownRequest);
        return elevator;
    }

    @Test
    public void addSelectedUpRequest() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(0)
                .build();
        var request = 3;

        //when
        service.addSelectRequest(elevator, request);

        //then

        assertEquals(0, elevator.getDownRequests().size());
        assertEquals(1, elevator.getUpRequests().size());
        assertEquals(request, elevator.getUpRequests().first());
    }

    @Test
    public void addSelectedDownRequest() {
        //given
        var elevator = Elevator.builder()
                .currentFloor(0)
                .build();
        var request = -1;

        //when
        service.addSelectRequest(elevator, request);

        //then
        assertEquals(0, elevator.getUpRequests().size());
        assertEquals(1, elevator.getDownRequests().size());
        assertEquals(request, elevator.getDownRequests().first());
    }
}