package com.example.elevator.service;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.ElevatorState;
import com.example.elevator.model.ElevatorStatus;
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

    @PostConstruct
    private void fill() {
        for(int i=0; i<elevatorCount; i++) {
            elevators.add(new Elevator(i, 0, ElevatorStatus.OPERATING, ElevatorState.NOT_MOVING));
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
