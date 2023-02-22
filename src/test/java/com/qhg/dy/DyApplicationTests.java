package com.qhg.dy;

import com.qhg.dy.model.Model1;
import com.qhg.dy.mapper.Model1Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DyApplicationTests {
    @Resource
    Model1Mapper model1Mapper;

    @Test
    void t1() {
        Model1 one = model1Mapper.selectOne(1);
        System.out.println(one);
    }

    @Test
    void t2() {
        List<Model1> model1s = model1Mapper.selectAll();
        System.out.println(model1s);
    }

    @Test
    void t3() {
        Model1 model1 = new Model1();
        model1.setName("qhg");
        model1.setValue("zxt");
        int i = model1Mapper.insert(model1);
        System.out.println(i);
    }

    @Test
    void t4() {
        Model1 model1 = new Model1();
        model1.setId(2);
        model1.setName("qhg");
        model1.setValue("zxt");
        int i = model1Mapper.update(model1);
        System.out.println(i);
    }

    @Test
    void t5() {
        Model1 model1 = new Model1();
        model1.setName("qhg");
        model1.setValue("zxt");
        int i = model1Mapper.insertId(model1);
        System.out.println(i);
        System.out.println(model1);
    }

    @Test
    void t6() {
        int i = model1Mapper.delete(5);
        System.out.println(i);
        i = model1Mapper.delete(6);
        System.out.println(i);
    }

}
