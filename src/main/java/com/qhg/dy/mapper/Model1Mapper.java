package com.qhg.dy.mapper;

import com.qhg.dy.model.Model1;

import java.util.List;

public interface Model1Mapper {
    Model1 selectOne(int id);

    List<Model1> selectAll();

    int update(Model1 model1);

    int insert(Model1 model1);

    int insertId(Model1 model1);

    int delete(int id);
}
