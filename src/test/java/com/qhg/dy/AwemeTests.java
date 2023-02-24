package com.qhg.dy;

import com.alibaba.fastjson.JSONObject;
import com.qhg.dy.mapper.*;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.SubUser;
import com.qhg.dy.model.Subject;
import com.qhg.dy.model.WeightData;
import com.qhg.dy.utils.AwemeAction;
import com.qhg.dy.utils.DouYinAction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class AwemeTests {

    @Resource
    AwemeMapper awemeMapper;
    @Resource
    SubUserMapper subUserMapper;
    @Resource
    JPAMapper jpaMapper;
    @Resource
    WeightDataMapper weightDataMapper;

//    @Test
//    void awemeType() throws InterruptedException {
//        while (true) {
//            List<Aweme> awemes = awemeMapper.findAll();
//            for (Aweme aweme : awemes) {
//                Integer aweme_type = JSONObject.parseObject(aweme.getInfo()).getInteger("aweme_type");
//                jpaMapper.update("update aweme set aweme_type = " + aweme_type + " where aweme_type = -1 and id = " + aweme.getId());
//            }
//            if (awemes.size() < 200) {
//                break;
//            }
//        }
//    }

    @Test
    void collectAwemeList() throws InterruptedException {
        List<SubUser> all = subUserMapper.findAll();
        for (SubUser user : all) {
            if (user.getRyAwemeStatus() == -1)
                continue;
            List<Aweme> awemeList = new AwemeAction(user)
                    .setBeforeAction(() -> {
                        System.out.println("准备开始 : " + user.getNickname() + " 的解析  https://www.douyin.com/user/" + user.getSecUid());
                    })
                    .setonErrorAction((integer) -> {
                        jpaMapper.update("update sub_user set reason = '拉黑或被封',ry_aweme_status = -1 where id = " + user.getId());
                    })
                    .setBeginAction(() -> {
                        jpaMapper.update("update sub_user set ry_aweme_status = 1 where id = " + user.getId());
                    })
                    .setFinishAction(() -> {
                        jpaMapper.update("update sub_user set ry_aweme_status = 2" +
                                ",ry_aweme_time = (select max(create_time) from aweme where sec_uid = '" + user.getSecUid() + "')" +
                                ",ry_aweme_count = (select count(*) from aweme where sec_uid = '" + user.getSecUid() + "' ) where id = " + user.getId());
                    })
                    .getAllAwemes((list) -> {
                        //save;
                        int i = 0;
                        for (Aweme aweme : list) {
                            aweme.setAuthorName(user.getNickname());
                            if (awemeMapper.countByAwemeId(aweme.getAwemeId()) == 0) {
                                i += awemeMapper.insert(aweme);
                                weightDataMapper.insert(new WeightData(aweme.getFullId(), aweme.getInfo()));
                            }
                        }
                        return i;
                    });
        }
    }

    @Test
    void collectAwemeList2() throws InterruptedException {
        List<SubUser> all = subUserMapper.findAll();
        Collections.reverse(all);
        for (SubUser user : all) {
            if (user.getRyAwemeStatus() == -1)
                continue;
            List<Aweme> awemeList = new AwemeAction(user)
                    .setBeforeAction(() -> {
                        System.out.println("准备开始 : " + user.getNickname() + " 的解析  https://www.douyin.com/user/" + user.getSecUid());
                    })
                    .setonErrorAction((integer) -> {
                        jpaMapper.update("update sub_user set reason = '拉黑或被封',ry_aweme_status = -1 where id = " + user.getId());
                    })
                    .setBeginAction(() -> {
                        jpaMapper.update("update sub_user set ry_aweme_status = 1 where id = " + user.getId());
                    })
                    .setFinishAction(() -> {
                        jpaMapper.update("update sub_user set ry_aweme_status = 2" +
                                ",ry_aweme_time = (select max(create_time) from aweme where sec_uid = '" + user.getSecUid() + "')" +
                                ",ry_aweme_count = (select count(*) from aweme where sec_uid = '" + user.getSecUid() + "' ) where id = " + user.getId());
                    })
                    .getAllAwemes((list) -> {
                        //save;
                        int i = 0;
                        for (Aweme aweme : list) {
                            aweme.setAuthorName(user.getNickname());
                            if (awemeMapper.countByAwemeId(aweme.getAwemeId()) == 0) {
                                i += awemeMapper.insert(aweme);
                                weightDataMapper.insert(new WeightData(aweme.getFullId(), aweme.getInfo()));
                            }
                        }
                        return i;
                    });
        }
    }

}
