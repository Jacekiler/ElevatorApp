package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.ElevatorDTO;
import com.example.elevator.service.mapper.ElevatorMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElevatorStateServiceTest {

    @Mock
    private ElevatorService elevatorService;

    @Mock
    private ElevatorMapper elevatorMapper;

    @InjectMocks
    private ElevatorStateService service;

    @Test
    public void get() {
        //given
        int id = 5;
        var elevator = Elevator.builder().build();
        when(elevatorService.getElevator(id)).thenReturn(elevator);
        var elevatorDTO = ElevatorDTO.builder().build();
        when(elevatorMapper.toDto(elevator)).thenReturn(elevatorDTO);

        //when
        var result = service.getElevator(5);

        //then
        assertEquals(result, elevatorDTO);
    }

    @Test
    public void getAll() {
        //given
        var elevator1 = Elevator.builder().build();
        var elevator2 = Elevator.builder().build();
        when(elevatorService.getElevators()).thenReturn(List.of(elevator1, elevator2));
        var elevatorDTO1 = ElevatorDTO.builder().build();
        var elevatorDTO2 = ElevatorDTO.builder().build();
        when(elevatorMapper.toDto(elevator1)).thenReturn(elevatorDTO1);
        when(elevatorMapper.toDto(elevator2)).thenReturn(elevatorDTO2);

        //when
        var result = service.getElevators();

        //then
        assertEquals(2, result.size());
        assertEquals(elevatorDTO1, result.get(0));
        assertEquals(elevatorDTO2, result.get(1));
    }
}