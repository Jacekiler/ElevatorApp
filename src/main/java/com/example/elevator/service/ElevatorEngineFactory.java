package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElevatorEngineFactory {

    private final EngineDoorsService engineDoorsService;
    private final EngineTargetService engineTargetService;
    private final EngineMonitoringService engineMonitoringService;
    private final EngineMovementService engineMovementService;

    public ElevatorEngine create(Elevator elevator) {
        return new ElevatorEngine(engineDoorsService, engineTargetService, engineMonitoringService, engineMovementService, elevator);
    }
}
