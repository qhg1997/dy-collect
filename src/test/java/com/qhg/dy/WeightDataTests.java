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

    @Test
    void t1() {
        String content = IO.readContentAsString(new File("C:\\Users\\qiaohuiguo\\IdeaProjects\\DuyinDownload\\src\\main\\resources\\sharelink"));
        String[] split = content.split("\n");
        for (String s : split) {
            String trim = s.split("https://")[1].trim();
            String replace = trim.replace("www.iesdouyin.com/share/user/", "");
            SubUser secUid = subUserMapper.findBySecUid(replace);
            if (secUid == null) {
                System.out.println("https://www.douyin.com/user/" + replace);

            } else
                System.err.println("已存在 : " + secUid.getNickname());
        }
    }

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        String script = IO.readContentAsString(new File("C:\\Users\\qiaohuiguo\\CodeProjects\\dyXBogus\\dist0.js"));
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        String res = (String) inv.invokeFunction("_getXBogus", "device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAETGTorjMSvmmGJqi5yhTOvhLcm-u1PTsfYycgb91rrc&max_cursor=1669973333000&locate_item_id=7202179238198250811&locate_query=false&show_live_replay_strategy=1&count=20&publish_video_strategy_type=2&pc_client_type=1&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1920&screen_height=1080&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=110.0.1587.50&browser_online=true&engine_name=Blink&engine_version=110.0.0.0&os_name=Windows&os_version=10&cpu_core_num=8&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=150&webid=7202623704786241061");
        System.out.println("res:" + res);
        res = (String) inv.invokeFunction("getXBogus", "device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAETGTorjMSvmmGJqi5yhTOvhLcm-u1PTsfYycgb91rrc&max_cursor=1669973333000&locate_item_id=7202179238198250811&locate_query=false&show_live_replay_strategy=1&count=20&publish_video_strategy_type=2&pc_client_type=1&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1920&screen_height=1080&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=110.0.1587.50&browser_online=true&engine_name=Blink&engine_version=110.0.0.0&os_name=Windows&os_version=10&cpu_core_num=8&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=150&webid=7202623704786241061");
        System.out.println("res:" + res);
    }
}
