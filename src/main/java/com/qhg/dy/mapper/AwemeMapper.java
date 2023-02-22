package com.qhg.dy.mapper;

import com.qhg.dy.model.Aweme;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AwemeMapper {

    Aweme findById(String id);

    List<Aweme> findByAwemeId(String awemeId);

    List<Aweme> findBySecUid(String secUid);

    int insert(Aweme aweme);

    int countByAwemeId(String awemeId);

    @Select("select * from aweme where aweme_type = -1 limit 200")
    List<Aweme> findAll();
}
