package com.qhg.dy.mapper;

import com.qhg.dy.model.WeightData;


public interface WeightDataMapper {
    WeightData selectOne(String id);

    int insert(WeightData data);

    int insertId(WeightData data);

    int delete(String id);
}
