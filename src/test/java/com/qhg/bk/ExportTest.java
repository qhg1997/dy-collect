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
        List<Xiaoqu> all = xiaoquRepository.findAll((Specification<Xiaoqu>) (root, query, criteriaBuilder) -> {
            Path<Long> id = root.get("id");
            return criteriaBuilder.gt(id, 7061);
        });
        for (Xiaoqu xiaoqu : all) {
            Optional<XiaoquInfo> byId = xiaoquInfoRepository.findById(xiaoqu.getId());
            if (byId.isPresent()) {
                XiaoquInfo xiaoquInfo = byId.get();
                RealEstate realEstate = xiaoquInfo.toRe(xiaoqu);
                realEstateRepository.save(realEstate);
            }
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
