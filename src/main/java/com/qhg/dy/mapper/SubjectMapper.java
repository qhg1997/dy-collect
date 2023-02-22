package com.qhg.dy.mapper;

import com.qhg.dy.model.Model1;
import com.qhg.dy.model.Subject;

import java.util.List;

public interface SubjectMapper {

    Subject findById(Integer id);

    Subject findByName(String name);

    int insert(Subject subject);
}
