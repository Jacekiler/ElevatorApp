package com.example.elevator.model;

import com.example.elevator.service.EngineDoorsService;
import com.example.elevator.service.EngineMonitoringService;
import com.example.elevator.service.EngineMovementService;
import com.example.elevator.service.EngineTargetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElevatorEngineTest {

    @Mock
    private EngineDoorsService doorsService;

    @Mock
    private EngineMonitoringService monitoringService;

    @Mock
    private EngineMovementService movementService;

    @Mock
    private EngineTargetService targetService;

    @InjectMocks
    private ElevatorEngine engine;

    private Elevator elevator;

    @BeforeEach
    public void setup() {
        elevator = Elevator.builder().build();
        engine = new ElevatorEngine(doorsService, targetService, monitoringService, movementService, elevator);
    }

    @Test
    public void shouldNotOperate() {
        //given
        when(monitoringService.isElevatorOperating(elevator)).thenReturn(false);

        //when
        engine.runCycle();

        //then
        verify(monitoringService, times(1)).isElevatorOperating(elevator);
        verifyNoInteractions(doorsService, movementService, targetService);
    }

    @Test
    public void shouldManageDoors() {
        //given
        when(monitoringService.isElevatorOperating(elevator)).thenReturn(true);
        when(doorsService.manageDoors(any(), eq(elevator))).thenReturn(true);

        //when
        engine.runCycle();

        //then
        verify(monitoringService, times(1)).isElevatorOperating(elevator);
        verify(doorsService, times(1)).manageDoors(any(), eq(elevator));
        verifyNoInteractions(movementService, targetService);
    }
    @Test
    public void shouldManageMovement() {
        //given
        when(monitoringService.isElevatorOperating(elevator)).thenReturn(true);
        when(doorsService.manageDoors(any(), eq(elevator))).thenReturn(false);

        //when
        engine.runCycle();

        //then
        verify(monitoringService, times(1)).isElevatorOperating(elevator);
        verify(doorsService, times(1)).manageDoors(any(), eq(elevator));
        verify(targetService, times(1)).updateTarget(any(), eq(elevator));
        verify(movementService, times(1)).manageMovement(any(), eq(elevator));
    }
}