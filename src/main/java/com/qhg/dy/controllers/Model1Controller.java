package com.qhg.dy.controllers;

import com.qhg.dy.model.Model1;
import com.qhg.dy.service.Model1Service;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Model1Controller {
    @Resource
    Model1Service service;

    @GetMapping("/get")
    public Model1 get(@RequestParam(defaultValue = "2") Integer id) {
        return service.get(id);
    }

    @GetMapping("/tx")
    public Model1 testTx(@RequestParam String name,
                         @RequestParam String value) throws Exception {
        return service.insert(name, value);
    }

}
