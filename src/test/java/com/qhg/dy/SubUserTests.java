package com.qhg.dy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qhg.dy.mapper.Model1Mapper;
import com.qhg.dy.mapper.SubUserMapper;
import com.qhg.dy.mapper.SubjectMapper;
import com.qhg.dy.model.Model1;
import com.qhg.dy.model.SubUser;
import com.qhg.dy.model.Subject;
import com.qhg.dy.utils.DouYinAction;
import com.qhg.dy.utils.IO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@SpringBootTest
class SubUserTests {

    @Resource
    SubUserMapper subUserMapper;
    @Resource
    SubjectMapper subjectMapper;

    @Test
    void collectBlockUsers() throws InterruptedException {
        Subject subject = subjectMapper.findByName("乔回国");
        DouYinAction action = new DouYinAction(subject.getCookie());
        List<SubUser> blockAllList = action.blockAllList();
        for (SubUser subUser : blockAllList) {
            Long count = subUserMapper.countByUid(subUser.getUid());
            if (count == 0) {
                subUser.setSubType(1);
                subUser.setSubjectId(subject.getId());
                int insert = subUserMapper.insert(subUser);
                System.out.println(subUser.getNickname() + " : 插入" + insert);
            } else {
                System.err.println(subUser.getNickname() + " : 已存在");
            }
            action.block(subUser.getShortId(), subUser.getSecUid(), false);
        }
    }

    public static void main(String[] args) {
    }
}
