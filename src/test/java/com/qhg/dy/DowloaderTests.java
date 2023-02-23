package com.qhg.dy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qhg.dy.mapper.*;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.AwemeResource;
import com.qhg.dy.model.WeightData;
import com.qhg.dy.utils.Downloader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DowloaderTests {

    @Resource
    SubUserMapper subUserMapper;
    @Resource
    AwemeMapper awemeMapper;
    @Resource
    AwemeResourceMapper resourceMapper;
    @Resource
    JPAMapper jpaMapper;

    @Test
    void test() throws InterruptedException {
        List<Aweme> awemes = awemeMapper.find(" downed = 0 limit 500");
        for (Aweme aweme : awemes) {
            List<AwemeResource> resources = resourceMapper.find(" aweme_id = '" + aweme.getAwemeId() + "'");
            aweme.setResources(resources);
            Downloader downloader = new Downloader(aweme);
            downloader.setFinishAction((ar) -> jpaMapper.update("update aweme_resource set downed = 1 where id = " + ar.getId()));
            downloader.setCompleteAction(a -> jpaMapper.update("update aweme set downed = 1 where id = " + a.getId()));
            System.out.println(downloader.download());
        }
    }
}
