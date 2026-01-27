package com.example.elevator.controller;

import com.example.elevator.model.dto.CallRequestDTO;
import com.example.elevator.model.dto.ElevatorDTO;
import com.example.elevator.model.dto.SelectRequestDTO;
import com.example.elevator.service.ElevatorOperationService;
import com.example.elevator.service.ElevatorStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elevators")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ElevatorController {

    private final ElevatorStateService elevatorStateService;
    private final ElevatorOperationService elevatorOperationService;

    @GetMapping
    public List<ElevatorDTO> getElevators() {
        return elevatorStateService.getElevators();
    }

    @GetMapping("/{id}")
    public ElevatorDTO getElevator(@PathVariable Integer id) {
        return elevatorStateService.getElevator(id);
    }

    @PostMapping("/{id}/call")
    public void callElevator(@PathVariable Integer id, @RequestBody CallRequestDTO callRequestDTO) {
        elevatorOperationService.call(id, callRequestDTO);
    }

    @PostMapping("/{id}/select")
    public void selectFloor(@PathVariable Integer id, @RequestBody SelectRequestDTO selectRequestDTO) {
        elevatorOperationService.select(id, selectRequestDTO);
    }

    @PostMapping("/{id}/doors/open")
    public void openDoors(@PathVariable Integer id) {
        elevatorOperationService.openDoors(id);
    }

    @PostMapping("/{id}/doors/close")
    public void closeDoors(@PathVariable Integer id) {
        elevatorOperationService.closeDoors(id);
    }
}
