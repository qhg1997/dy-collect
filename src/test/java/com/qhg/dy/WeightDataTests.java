package com.qhg.dy;

import com.qhg.dy.mapper.AwemeMapper;
import com.qhg.dy.mapper.JPAMapper;
import com.qhg.dy.mapper.SubUserMapper;
import com.qhg.dy.mapper.WeightDataMapper;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.SubUser;
import com.qhg.dy.model.WeightData;
import com.qhg.dy.utils.IO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import org.springframework.web.util.JavaScriptUtils;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@SpringBootTest
public class WeightDataTests {
    @Resource
    SubUserMapper subUserMapper;
    @Resource
    AwemeMapper awemeMapper;
    @Resource
    JPAMapper jpaMapper;
    @Resource
    WeightDataMapper weightDataMapper;

}
