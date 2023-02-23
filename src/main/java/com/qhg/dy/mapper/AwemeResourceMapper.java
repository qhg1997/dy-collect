package com.qhg.dy.mapper;

import com.qhg.dy.model.AwemeResource;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface AwemeResourceMapper {

    AwemeResource selectOne(String id);

    @Select("select * from aweme_resource where ${condition}")
    List<AwemeResource> find(String condition);

    int insert(AwemeResource data);

    int delete(String id);
}
