package com.qhg.dy;

import com.qhg.dy.mapper.SubUserMapper;
import com.qhg.dy.model.Model1;
import com.qhg.dy.model.SubUser;
import com.qhg.dy.utils.IO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;

@SpringBootTest
public class DemoTests {
    @Resource
    SubUserMapper subUserMapper;

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
