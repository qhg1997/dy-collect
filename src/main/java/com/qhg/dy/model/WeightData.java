package com.qhg.dy.model;

import lombok.Data;

@Data
public class WeightData {
    public String id;
    public String value;

    public WeightData(String id, String value) {
        this.id = id;
        this.value = value;
    }
}
