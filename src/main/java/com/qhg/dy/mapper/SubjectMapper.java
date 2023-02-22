package com.qhg.dy.mapper;

import com.qhg.dy.model.Subject;


public interface SubjectMapper {

    Subject findById(Integer id);

    Subject findByName(String name);

    int insert(Subject subject);
}
