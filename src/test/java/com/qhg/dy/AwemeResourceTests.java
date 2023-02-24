package com.qhg.dy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qhg.dy.mapper.*;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.AwemeResource;
import com.qhg.dy.model.SubUser;
import com.qhg.dy.model.WeightData;
import com.qhg.dy.utils.AwemeAction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AwemeResourceTests {

    @Resource
    AwemeMapper awemeMapper;
    @Resource
    AwemeResourceMapper resourceMapper;
    @Resource
    JPAMapper jpaMapper;
    @Resource
    WeightDataMapper weightDataMapper;

    @Test
    void test0() throws InterruptedException {
        List<Aweme> all = awemeMapper.find(" extract != 1");
        all.parallelStream().forEach(aweme -> {
                    try {
                        WeightData weightData = weightDataMapper.selectOne(aweme.getFullId());
                        String value = weightData.getValue();
                        JSONObject object = JSONObject.parseObject(value);
                        Integer awemeType = aweme.getAwemeType();
                        List<AwemeResource> resources = new ArrayList<>();
                        // # 2023/01/19 aweme_type是作品类型 68：图集 0：视频
                        if (awemeType == 68) {
                            //图集
                            JSONArray images = object.getJSONArray("images");
                            for (int i = 0; i < images.size(); i++) {
                                JSONObject imagesJSONObject = images.getJSONObject(i);
                                AwemeResource awemeResource = new AwemeResource();
                                awemeResource.setType(1);
                                awemeResource.setSecUid(aweme.getSecUid());
                                awemeResource.setAwemeId(aweme.getAwemeId());
                                awemeResource.setSafeFileName(aweme.getAwemeId() + "_" + i + ".jpg");
                                awemeResource.setDowned(0);
                                awemeResource.setAuthorName(replaceT(aweme.getAuthorName()));
                                awemeResource.setDesc(replaceR(aweme.getDesc(), 60));
                                awemeResource.setTitle(replaceR(aweme.getTitle(), 60));
                                awemeResource.setCreateTime(aweme.getCreateTime());
                                try {
                                    String url;
                                    if (imagesJSONObject.size() == 4) {
                                        url = imagesJSONObject.getJSONArray("url_list").getString(3);
                                    } else {
                                        JSONArray url_list = imagesJSONObject.getJSONArray("url_list");
                                        url = url_list.getString(0);
                                        awemeResource.setInfo(url_list.toJSONString());
                                    }
                                    awemeResource.setUri(url);
                                } catch (Exception e) {
                                    awemeResource.setInfo(imagesJSONObject.toJSONString());
                                }
                                resources.add(awemeResource);
                            }
                        } else {
                            //视频
                            JSONObject video = object.getJSONObject("video");
                            AwemeResource awemeResource = new AwemeResource();
                            awemeResource.setType(0);
                            awemeResource.setSecUid(aweme.getSecUid());
                            awemeResource.setAwemeId(aweme.getAwemeId());
                            awemeResource.setSafeFileName(replaceT(aweme.getTitle()) + "_" + ".mp4");
                            awemeResource.setDowned(0);
                            awemeResource.setAuthorName(replaceT(aweme.getAuthorName()));
                            awemeResource.setDesc(replaceR(aweme.getDesc(), 60));
                            awemeResource.setTitle(replaceR(aweme.getTitle(), 60));
                            awemeResource.setCreateTime(aweme.getCreateTime());

                            try {
                                JSONObject playAddr = video.getJSONObject("play_addr");
                                String uri = playAddr.getString("uri");
                                String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=" + uri + "&ratio=1080p&line=0";
                                JSONArray urlList = playAddr.getJSONArray("url_list");
                                awemeResource.setUri(url);
                                awemeResource.setInfo(urlList.toJSONString());
                            } catch (Exception e) {
                                awemeResource.setInfo(video.toJSONString());
                            }
                            resources.add(awemeResource);
                        }
                        saveResource(resources);
                        jpaMapper.update("update aweme set extract = 1 where id = " + aweme.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        jpaMapper.update("update aweme set extract = -1 where id = " + aweme.getId());
                    }
                }
        );

    }

    private void saveResource(List<AwemeResource> resources) {
        for (AwemeResource resource : resources) {
            resourceMapper.insert(resource);
        }
    }

    private static String replaceT(String text) {
        String reSub = "[^\u4e00-\u9fa5^a-zA-Z0-9#]";
        if (text == null)
            return System.currentTimeMillis() + "";
        if (text.length() > 20)
            text = text.substring(0, 20);
        return text.replaceAll(reSub, "_");
    }

    private static String replaceR(String text, int range) {
        String reSub = "[^\u4e00-\u9fa5^a-zA-Z0-9#]";
        if (text == null)
            return System.currentTimeMillis() + "";
        if (text.length() > range)
            text = text.substring(0, range);
        return text.replaceAll(reSub, "_");
    }
}
