package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorDirection;
import com.example.elevator.model.ElevatorStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElevatorMapperTest {

    private static final Integer ID = 1;
    private static final AtomicInteger FLOOR = new AtomicInteger(2);
    private static final Integer MIN_FLOOR = -1;
    private static final Integer MAX_FLOOR = 3;
    private static final ElevatorStatus STATUS = ElevatorStatus.OPERATING;
    private static final ElevatorDirection STATE = ElevatorDirection.NOT_MOVING;


    @Mock
    private ElevatorQueueMapper elevatorQueueMapper;

    @InjectMocks
    private ElevatorMapper elevatorMapper;

    @Test
    public void map() {
        //given
        var elevator = Elevator.builder()
                .id(ID)
                .currentFloor(FLOOR)
                .minFloor(MIN_FLOOR)
                .maxFloor(MAX_FLOOR)
                .elevatorStatus(STATUS)
                .elevatorDirection(STATE)
                .upRequests(new ConcurrentSkipListSet<>(List.of(2,3)))
                .downRequests(new ConcurrentSkipListSet<>(List.of(-1)))
                .build();
        when(elevatorQueueMapper.getRequests(elevator)).thenReturn(List.of(-1,2,3));

        //when
        var result = elevatorMapper.toDto(elevator);

        //then
        assertAll(
                () -> assertEquals(ID, result.getId()),
                () -> assertEquals(FLOOR.get(), result.getCurrentFloor()),
                () -> assertEquals(MIN_FLOOR, result.getMinFloor()),
                () -> assertEquals(MAX_FLOOR, result.getMaxFloor()),
                () -> assertEquals(STATUS, result.getStatus()),
                () -> assertEquals(STATE, result.getDirection()),
                () -> assertEquals(3, result.getRequests().size()),
                () -> assertEquals(-1, result.getRequests().get(0)),
                () -> assertEquals(2, result.getRequests().get(1)),
                () -> assertEquals(3, result.getRequests().get(2))
        );
    }
}