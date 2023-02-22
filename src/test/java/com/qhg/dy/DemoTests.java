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

    private static String replaceT(String text) {
        String reSub = "[^\u4e00-\u9fa5^a-zA-Z0-9#]";
        if (text == null)
            return System.currentTimeMillis() + "";
        if (text.length() > 70)
            text = text.substring(0, 70);
        return text.replaceAll(reSub, "_");
    }
}
