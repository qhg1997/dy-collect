package com.qhg.dy.service;

import com.qhg.dy.mapper.Model1Mapper;
import com.qhg.dy.model.Model1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class Model1Service {
    @Resource
    Model1Mapper mapper;

    public Model1 get(Integer id) {
        return mapper.selectOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Model1 insert(String name, String value) throws Exception {
        Model1 model1 = new Model1();
        model1.setName(name);
        model1.setValue(value);
        int i = mapper.insertId(model1);
        System.out.println("插入返回 :" + i);
        try {
            System.out.println(5 / 0);
        } catch (Exception e) {
            throw new Exception("1111");
        }
        return model1;
    }

}
