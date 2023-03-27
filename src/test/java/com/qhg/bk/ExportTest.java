package com.qhg.bk;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.qhg.bk.dao.RealEstateRepository;
import com.qhg.bk.dao.XiaoquBigInfoRepository;
import com.qhg.bk.dao.XiaoquInfoRepository;
import com.qhg.bk.dao.XiaoquRepository;
import com.qhg.bk.model.RealEstate;
import com.qhg.bk.model.Xiaoqu;
import com.qhg.bk.model.XiaoquBigInfo;
import com.qhg.bk.model.XiaoquInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class ExportTest {
    @Resource
    XiaoquRepository xiaoquRepository;
    @Resource
    XiaoquInfoRepository xiaoquInfoRepository;
    @Resource
    XiaoquBigInfoRepository xiaoquBigInfoRepository;
    @Resource
    RealEstateRepository realEstateRepository;

    @Test
    void t1() throws IOException {
        List<XiaoquInfo> all = xiaoquInfoRepository.findAll().stream().filter(u -> u.getStatus() == 1).collect(Collectors.toList());
        for (XiaoquInfo xiaoquInfo : all) {
            RealEstate realEstate = xiaoquInfo.toRe();
            realEstateRepository.save(realEstate);
        }
    }

    @Test
    void t5() throws IOException {
        String k = "c3642d67c64502b885" + "" + "c33c52ea7bc235";
        List<RealEstate> all = realEstateRepository.findAll().stream().filter(u -> u.getAmapPosition() == null).collect(Collectors.toList());
        for (RealEstate estate : all) {
            JSONObject bean = OkHttps.async("https://restapi.amap.com/v3/assistant/coordinate/convert")
                    .addUrlPara("coordsys", "baidu")
                    .addUrlPara("key", k)
                    .addUrlPara("locations", estate.getBaiduPosition())
                    .get().getResult().getBody().toBean(JSONObject.class);
            System.out.println(bean);
            if (bean.getString("status").equals("1")) {
                String locations = bean.getString("locations");
                estate.setAmapPosition(locations);
                estate.setPosition(locations);
                realEstateRepository.save(estate);
            } else {
                k = "0888daa686ee6fa32b" + "" + "bda70ceb161a9e";
                bean = OkHttps.async("https://restapi.amap.com/v3/assistant/coordinate/convert")
                        .addUrlPara("coordsys", "baidu")
                        .addUrlPara("key", k)
                        .addUrlPara("locations", estate.getBaiduPosition())
                        .get().getResult().getBody().toBean(JSONObject.class);
                System.out.println(bean);
                if (bean.getString("status").equals("1")) {
                    String locations = bean.getString("locations");
                    estate.setAmapPosition(locations);
                    estate.setPosition(locations);
                    realEstateRepository.save(estate);
                }
            }
        }

    }

    @Test
    void t3() throws IOException {
        List<XiaoquInfo> all = xiaoquInfoRepository.findAll().stream().filter(u -> u.getRegion() == null).collect(Collectors.toList());
        for (XiaoquInfo xiaoquInfo : all) {
            if (xiaoquInfo.getInfo() == null) {
                continue;
            }
            List<Xiaoqu> byXqid = xiaoquRepository.findByXqid(xiaoquInfo.getXqId());
            List<String> collect = byXqid.stream().map(Xiaoqu::getRegion).distinct().collect(Collectors.toList());
            if (collect.size() == 1) {
                xiaoquInfo.setRegion(collect.get(0));
            }
            xiaoquInfoRepository.save(xiaoquInfo);
        }
    }

    @Test
    void t2() throws IOException {
        List<XiaoquInfo> all = xiaoquInfoRepository.findAll();
        ArrayList<Object> objects = new ArrayList<>();
        for (XiaoquInfo info : all) {
            JSONObject jsonObject = JSONObject.parseObject(info.getInfo());
            JSONArray images = jsonObject.getJSONArray("images");
            objects.addAll(images.stream().distinct().collect(Collectors.toList()));
        }
        System.out.println(objects.size());
    }
}
