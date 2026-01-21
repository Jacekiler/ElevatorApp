package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;

@Component
public class ElevatorQueueMapper {

    public List<Integer> getRequests(Elevator elevator) {
        return switch (elevator.getElevatorDirection()) {
            case MOVING_UP -> concatRequests(elevator.getUpRequests(), elevator.getDownRequests());
            case MOVING_DOWN -> concatRequests(elevator.getDownRequests(), elevator.getUpRequests());
            default -> concatNotMoving(elevator);
        };
    }

    private List<Integer> concatRequests(NavigableSet<Integer> first, NavigableSet<Integer> second) {
        var requests = new ArrayList<>(first);
        requests.addAll(second);
        return requests;
    }

    private List<Integer> concatNotMoving(Elevator elevator) {
        if (shouldDetermineDirection(elevator)) {
            return shouldMoveUp(elevator)
                    ? concatRequests(elevator.getUpRequests(), elevator.getDownRequests())
                    : concatRequests(elevator.getDownRequests(), elevator.getUpRequests());
        } else {
            return concatRequests(elevator.getUpRequests(), elevator.getDownRequests());
        }
    }

    private boolean shouldDetermineDirection(Elevator elevator) {
        return !elevator.getUpRequests().isEmpty() && !elevator.getDownRequests().isEmpty();
    }

    private boolean shouldMoveUp(Elevator elevator) {
        return Math.abs(elevator.getCurrentFloor() - elevator.getUpRequests().first()) <=
                Math.abs(elevator.getCurrentFloor() - elevator.getDownRequests().first());
    }

}
