package com.qhg.dy.mapper;

import com.qhg.dy.model.SubUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

public interface JPAMapper {
    @Select("${sql}")
    List<HashMap<?, ?>> select(String sql);

    @Update("${sql}")
    Integer update(String sql);

    @Insert("${sql}")
    Integer insert(String sql);

    @Delete("${sql}")
    Integer delete(String sql);

}
