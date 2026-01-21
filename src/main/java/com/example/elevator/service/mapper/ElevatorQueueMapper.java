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
            case MOVING_UP -> concatRequests(elevator.getUpRequestsAsc(), elevator.getDownRequestsDesc());
            case MOVING_DOWN -> concatRequests(elevator.getDownRequestsDesc(), elevator.getUpRequestsAsc());
            default -> concatNotMoving(elevator);
        };
    }

    private List<Integer> concatRequests(List<Integer> first, List<Integer> second) {
        var requests = new ArrayList<>(first);
        requests.addAll(second);
        return requests;
    }

    private List<Integer> concatNotMoving(Elevator elevator) {
        // todo check that against EngineTargetService?
        if (shouldDetermineDirection(elevator)) {
            return shouldMoveUp(elevator)
                    ? concatRequests(elevator.getUpRequestsAsc(), elevator.getDownRequestsDesc())
                    : concatRequests(elevator.getDownRequestsDesc(), elevator.getUpRequestsAsc());
        } else {
            return concatRequests(elevator.getUpRequestsAsc(), elevator.getDownRequestsDesc());
        }
    }

    private boolean shouldDetermineDirection(Elevator elevator) {
        return !elevator.getUpRequestsAsc().isEmpty() && !elevator.getDownRequestsDesc().isEmpty();
    }

    private boolean shouldMoveUp(Elevator elevator) {
        return Math.abs(elevator.getUpRequestsAsc().get(0) - elevator.getCurrentFloor()) <=
                Math.abs(elevator.getCurrentFloor() - elevator.getDownRequestsDesc().get(0));
    }

}
