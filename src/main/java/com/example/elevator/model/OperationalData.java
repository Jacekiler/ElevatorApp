package com.example.elevator.model;

import lombok.Data;

@Data
public class OperationalData {
    private int doorTimer = 0;
    private int openDoorTimer = 0;
    private int movementTimer = 0;
    private Integer targetFloor = null;
}
