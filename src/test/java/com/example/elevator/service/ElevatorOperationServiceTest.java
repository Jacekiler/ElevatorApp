package com.example.elevator.service;

import com.example.elevator.model.CallDirection;
import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.CallRequestDTO;
import com.example.elevator.model.dto.SelectRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElevatorOperationServiceTest {

    @Mock
    private ElevatorService elevatorService;

    @Mock ElevatorRequestsQueueService requestsQueueService;

    @InjectMocks
    private ElevatorOperationService service;

    @Test
    public void call() {
        //given
        var id = 5;
        var elevator = Elevator.builder().build();
        when(elevatorService.getElevator(id)).thenReturn(elevator);
        var floor = 1;
        var direction = CallDirection.UP;
        var request = new CallRequestDTO(floor, direction);

        //when
        service.call(id, request);

        //then
        verify(requestsQueueService, times(1)).addCallRequest(elevator, floor, direction);
    }

    @Test
    public void select() {
        //given
        var id = 5;
        var elevator = Elevator.builder().build();
        when(elevatorService.getElevator(id)).thenReturn(elevator);
        var floor = 1;
        var request = new SelectRequestDTO(floor);

        //when
        service.select(id, request);

        //then
        verify(requestsQueueService, times(1)).addSelectRequest(elevator, floor);
    }

    @Test
    public void openDoors() {
        //given
        var id = 5;
        var elevator = Elevator.builder().build();
        when(elevatorService.getElevator(id)).thenReturn(elevator);

        //when
        service.openDoors(id);

        //then
        assertTrue(elevator.isOpenDoorTrigger());
    }

    @Test
    public void closeDoors() {
        //given
        var id = 5;
        var elevator = Elevator.builder().build();
        when(elevatorService.getElevator(id)).thenReturn(elevator);

        //when
        service.closeDoors(id);

        //then
        assertTrue(elevator.isCloseDoorTrigger());
    }
}