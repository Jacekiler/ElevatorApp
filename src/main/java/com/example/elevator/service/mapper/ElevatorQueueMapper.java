package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ElevatorQueueMapper {

    // todo add tests
    public List<Integer> getRequests(Elevator elevator) {
        return switch (elevator.getElevatorDirection()) {
            case MOVING_UP -> concatRequests(elevator.getUpRequests(), elevator.getDownRequestsDesc());
            case MOVING_DOWN -> concatRequests(elevator.getDownRequestsDesc(), elevator.getUpRequests());
            default -> concatNotMoving(elevator);
        };
    }

    private List<Integer> concatRequests(List<Integer> first, List<Integer> second) {
        var requests = new ArrayList<>(first);
        requests.addAll(second);
        return requests;
    }

    private List<Integer> concatNotMoving(Elevator elevator) {
        // todo refactor?
        if (shouldDetermineDirection(elevator)) {
            return shouldMoveUp(elevator)
                    ? concatRequests(elevator.getUpRequests(), elevator.getDownRequestsDesc())
                    : concatRequests(elevator.getDownRequestsDesc(), elevator.getUpRequests());
        } else {
            return concatRequests(elevator.getUpRequests(), elevator.getDownRequestsDesc());
        }
    }

    private boolean shouldDetermineDirection(Elevator elevator) {
        return !elevator.getUpRequests().isEmpty() && !elevator.getDownRequestsDesc().isEmpty();
    }

    private boolean shouldMoveUp(Elevator elevator) {
        return Math.abs(elevator.getUpRequests().get(0) - elevator.getCurrentFloor()) <=
                Math.abs(elevator.getCurrentFloor() - elevator.getDownRequestsDesc().get(0));
    }

}
