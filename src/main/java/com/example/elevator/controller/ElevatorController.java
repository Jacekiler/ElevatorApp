package com.example.elevator.controller;

import com.example.elevator.model.dto.ElevatorDTO;
import com.example.elevator.service.ElevatorStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/elevators")
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorStateService elevatorStateService;

    @GetMapping
    public List<ElevatorDTO> getElevators() {
        return elevatorStateService.getElevators();
    }

    @GetMapping("/{id}")
    public ElevatorDTO getElevator(@PathVariable Integer id) {
        return elevatorStateService.getElevator(id);
    }
}
