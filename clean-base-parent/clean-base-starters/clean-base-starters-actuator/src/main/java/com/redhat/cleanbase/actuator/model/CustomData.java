package com.redhat.cleanbase.actuator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomData {
    private String dataString;
    private int dataInt;
}