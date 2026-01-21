package com.example.elevator.model.dto;

import com.example.elevator.model.CallDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallRequestDTO {
    private Integer floor;
    private CallDirection direction;
}
