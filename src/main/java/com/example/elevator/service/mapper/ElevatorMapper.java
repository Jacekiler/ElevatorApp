package com.example.elevator.service.mapper;

import com.example.elevator.model.Elevator;
import com.example.elevator.model.dto.ElevatorDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ElevatorMapper {

    public ElevatorDTO toDto(Elevator elevator) {
        return ElevatorDTO.builder()
                .id(elevator.getId())
                .floor(elevator.getCurrentFloor())
                .minFloor(elevator.getMinFloor())
                .maxFloor(elevator.getMaxFloor())
                .status(elevator.getElevatorStatus())
                .state(elevator.getElevatorState())
                .requests(getRequests(elevator))
                .build();
    }

    private List<Integer> getRequests(Elevator elevator) {
        return switch (elevator.getElevatorState()) {
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
        if (!elevator.getUpRequests().isEmpty() && !elevator.getDownRequestsDesc().isEmpty()) {
            return shouldMoveUp(elevator)
                    ? concatRequests(elevator.getUpRequests(), elevator.getDownRequestsDesc())
                    : concatRequests(elevator.getDownRequestsDesc(), elevator.getUpRequests());
        } else {
            return concatRequests(elevator.getUpRequests(), elevator.getDownRequestsDesc());
        }
    }

    private static boolean shouldMoveUp(Elevator elevator) {
        return Math.abs(elevator.getUpRequests().get(0) - elevator.getCurrentFloor()) <=
                Math.abs(elevator.getCurrentFloor() - elevator.getDownRequestsDesc().get(0));
    }

}
