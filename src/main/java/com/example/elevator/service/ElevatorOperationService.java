package com.example.elevator.service;

import com.example.elevator.model.dto.CallRequestDTO;
import com.example.elevator.model.dto.SelectRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElevatorOperationService {

    private final ElevatorService elevatorService;
    private final ElevatorRequestsQueueService requestsQueueService;

    public void call(Integer id, CallRequestDTO callRequestDTO) {
        log.info("Calling elevator on floor: {}", callRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        requestsQueueService.addCallRequest(elevator, callRequestDTO.getFloor(), callRequestDTO.getDirection());
    }

    public void select(Integer id, SelectRequestDTO selectRequestDTO) {
        log.info("Selected floor: {}", selectRequestDTO.getFloor());
        var elevator = elevatorService.getElevator(id);
        requestsQueueService.addSelectRequest(elevator, selectRequestDTO.getFloor());
    }

    public void openDoors(Integer id) {
        log.info("Trying to open doors in elevator {}", id);
        var elevator = elevatorService.getElevator(id);
        elevator.triggerOpenDoor();
    }

    public void closeDoors(Integer id) {
        log.info("Trying to close doors in elevator {}", id);
        var elevator = elevatorService.getElevator(id);
        elevator.triggerCloseDoor();
    }
}
