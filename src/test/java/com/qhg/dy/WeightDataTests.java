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

import javax.annotation.Resource;
import java.io.File;
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

    public static void main(String[] args) {

    }
}
