package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElevatorService {

    private final List<Elevator> elevators = new ArrayList<>();
    @Value("${elevators.count}")
    private int elevatorCount;
    @Value("${elevators.minFloor}")
    private int elevatorMinFloor;
    @Value("${elevators.maxFloor}")
    private int elevatorMaxFloor;

    @PostConstruct
    private void fill() {
        for(int i=0; i<elevatorCount; i++) {
            elevators.add(Elevator.builder()
                    .id(i)
                    .minFloor(elevatorMinFloor)
                    .maxFloor(elevatorMaxFloor)
                    .build());
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public Elevator getElevator(Integer id) {
        return elevators.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Elevator with id %s not found", id)));
    }
}
