package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineMonitoringServiceTest {

    private final EngineMonitoringService service = new EngineMonitoringService();

    @Test
    public void operating() {
        //given
        var elevator = Elevator.builder().elevatorStatus(ElevatorStatus.OPERATING).build();

        //when
        var result = service.isElevatorOperating(elevator);

        //then
        assertTrue(result);
    }

    @Test
    public void notOperating() {
        //given
        var elevator = Elevator.builder().elevatorStatus(ElevatorStatus.NOT_OPERATING).build();

        //when
        var result = service.isElevatorOperating(elevator);

        //then
        assertFalse(result);
    }

}