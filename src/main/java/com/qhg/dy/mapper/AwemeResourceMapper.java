package com.qhg.dy.mapper;

import com.qhg.dy.model.AwemeResource;


public interface AwemeResourceMapper {

    AwemeResource selectOne(String id);

    int insert(AwemeResource data);

    int delete(String id);
}
