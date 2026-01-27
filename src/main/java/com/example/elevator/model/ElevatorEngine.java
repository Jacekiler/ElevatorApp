package com.example.elevator.model;

import com.example.elevator.service.EngineDoorsService;
import com.example.elevator.service.EngineMonitoringService;
import com.example.elevator.service.EngineMovementService;
import com.example.elevator.service.EngineTargetService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class ElevatorEngine {

    private final EngineDoorsService engineDoorsService;
    private final EngineTargetService engineTargetService;
    private final EngineMonitoringService engineMonitoringService;
    private final EngineMovementService engineMovementService;

    public static final int DOOR_OPEN_CLOSE_CYCLES = 5;
    public static final int OPEN_DOOR_CYCLES = 4;
    public static final int ONE_FLOOR_UP_DOWN_MOVEMENT_CYCLES = 4;

    private final Elevator elevator;
    private final OperationalData operationalData = new OperationalData();

    public void runCycle() {
        log.debug("Elevator {} running cycle", elevator.getId());
        if (!engineMonitoringService.isElevatorOperating(elevator)) return;
        if (engineDoorsService.manageDoors(operationalData, elevator)) return;
        engineTargetService.updateTarget(operationalData, elevator);
        if (engineMovementService.manageMovement(operationalData, elevator)) return;
    }
}
