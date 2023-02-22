package com.qhg.dy.mapper;

import com.qhg.dy.model.SubUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubUserMapper {

    Long countByUid(String uid);

    SubUser findById(Long id);

    SubUser findByUid(String uid);

    SubUser findBySecUid(String secUid);

    SubUser findByNickname(String nickname);

    int insert(SubUser subUser);

    @Select("select * from sub_user")
    List<SubUser> findAll();

    @Select("select * from sub_user where ${condition}")
    List<SubUser> find(String condition);
}
