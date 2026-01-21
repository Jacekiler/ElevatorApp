package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EngineMonitoringService {

    public boolean isElevatorOperating(Elevator elevator) {
        if (elevator.isOperating()) {
            return true;
        }
        log.info("Elevator {} - not in operation", elevator.getId());
        elevator.clearDoorTriggers();
        return false;
    }
}
