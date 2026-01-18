package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorState;
import com.example.elevator.model.ElevatorStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ElevatorMapperTest {

    private static final Integer ID = 1;
    private static final Integer FLOOR = 2;
    private static final ElevatorStatus STATUS = ElevatorStatus.OPERATING;
    private static final ElevatorState STATE = ElevatorState.MOVING_UP;

    private final ElevatorMapper elevatorMapper = new ElevatorMapper();

    @Test
    public void map() {
        //given
        var elevator = Elevator.builder()
                .id(ID)
                .floor(FLOOR)
                .status(STATUS)
                .state(STATE)
                .build();

        //when
        var result = elevatorMapper.toDto(elevator);

        //then
        assertAll(
                () -> assertEquals(ID, result.getId()),
                () -> assertEquals(FLOOR, result.getFloor()),
                () -> assertEquals(STATUS, result.getStatus()),
                () -> assertEquals(STATE, result.getState())
        );
    }
}