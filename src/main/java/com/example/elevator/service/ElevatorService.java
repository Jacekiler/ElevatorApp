package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorEngine;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElevatorService {

    @Getter
    private final List<Elevator> elevators = new ArrayList<>();
    @Getter
    private final List<ElevatorEngine> engines = new ArrayList<>();
    @Value("${elevators.count}")
    private int elevatorCount;
    @Value("${elevators.minFloor}")
    private int elevatorMinFloor;
    @Value("${elevators.maxFloor}")
    private int elevatorMaxFloor;

    @PostConstruct
    private void fill() {
        for(int i=0; i<elevatorCount; i++) {
            var elevator = Elevator.builder()
                    .id(i)
                    .minFloor(elevatorMinFloor)
                    .maxFloor(elevatorMaxFloor)
                    .build();
            elevators.add(elevator);
            engines.add(new ElevatorEngine(elevator));
        }
    }

    public Elevator getElevator(Integer id) {
        return elevators.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Elevator with id %s not found", id)));
    }
}
