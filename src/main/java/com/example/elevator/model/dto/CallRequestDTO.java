package com.example.elevator.model.dto;

import com.example.elevator.model.CallDirection;
import lombok.Data;

@Data
public class CallRequestDTO {
    private Integer floor;
    private CallDirection direction;
}
