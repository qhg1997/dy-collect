package com.qhg.dy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.qhg.dy.mapper.*;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.AwemeResource;
import com.qhg.dy.model.WeightData;
import com.qhg.dy.utils.Downloader;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
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
        /*图片下载*/
    void test() throws InterruptedException {
        while (true) {
            List<AwemeResource> resources = resourceMapper.find(" downed = 0 and type = 1 limit 10000");
//            Collections.reverse(resources);
            Downloader downloader = new Downloader(resources);
            downloader.setFinishAction((ar) -> jpaMapper.update("update aweme_resource set downed = 1 where id = " + ar.getId()));
            try {
                downloader.download();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resources.isEmpty()) {
                break;
            }
        }

    }

    @Test
        /*图片下载校验*/
    void test3() throws InterruptedException {
        List<AwemeResource> resources = resourceMapper.find("type = 1  and downed = 1");
        resources.forEach((awemeResource) -> {
            File file = new File(Downloader.baseFolder, "image\\" + awemeResource.getAuthorName() + "\\" + awemeResource.getTitle() + "\\" + awemeResource.getSafeFileName());
            if (!file.exists()) {
                System.out.println(file.getAbsolutePath());
//                jpaMapper.update("update aweme_resource set downed = 0 where id = " + awemeResource.getId());
            }
        });
    }


    @Test
    void test0() throws InterruptedException {
        while (true) {
            List<AwemeResource> resources = resourceMapper.find(" downed = 0 and type = 0 limit 10000");
//            Collections.reverse(resources);
            Downloader downloader = new Downloader(resources);
            downloader.setFinishAction((ar) -> jpaMapper.update("update aweme_resource set downed = 1 where id = " + ar.getId()));
            try {
                downloader.download();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resources.isEmpty()) {
                break;
            }
        }
    }

    @Test
    void test1() throws InterruptedException {
        List<AwemeResource> resources = resourceMapper.find(" type = 0");
        for (AwemeResource resource : resources) {
            File file = new File(Downloader.baseFolder, "video\\" + resource.getAuthorName() + "\\" + resource.getId() + "_" + resource.getSafeFileName());
            if (!file.exists() || file.length() == 0) {
                FileUtils.deleteQuietly(file);
                System.out.println(resource.getUri());
                jpaMapper.update("update aweme_resource set downed = 0 where id = " + resource.getId());
            }
        }
    }

    @Test
    void test5() throws InterruptedException {
        List<AwemeResource> resources = resourceMapper.find(" type = 0 and downed = 0");
        for (AwemeResource resource : resources) {
            File file = new File(Downloader.baseFolder, "video\\" + resource.getAuthorName() + "\\" + resource.getId() + "_" + resource.getSafeFileName());
            FileUtils.deleteQuietly(file);
            if (OkHttps.async(resource.getUri()).get().getResult().getStatus() == 404) {
                jpaMapper.update("update aweme_resource set downed = -1 where id = " + resource.getId());
            } else {
                System.out.println(resource.getUri());
            }

        }
    }

}
