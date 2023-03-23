package com.qhg.sgp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.sgp.model.Columnist;
import com.qhg.sgp.model.Pai;
import com.qhg.sgp.model.Tag;
import com.qhg.sgp.repo.ColumnistRepository;
import com.qhg.sgp.repo.PaiRepository;
import com.qhg.sgp.repo.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class PaiTests {
    @Test
    void fastCollectNews() {
    }

    @Resource
    PaiRepository paiRepository;
    @Resource
    TagRepository tagRepository;
    @Resource
    ColumnistRepository columnistRepository;
    HTTP http = HTTP.builder()
            .config(b -> b.connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)

            ).addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    @Test
    void fastCollect() {
        Integer type = 2;//1视频 2专栏
        Integer page = 1;
        int consecutiveNumbers = 0;
        int maxConsecutiveNumbers = 5;
        while (true) {
            JSONObject body = http.async("https://api.cbbee0.com/v1_2/homePage")
                    .addBodyPara("device_id", "")
                    .addBodyPara("hm", "008-api")
                    .addBodyPara("last_page", 0)
                    .addBodyPara("length", "10")
                    .addBodyPara("ltype", type)//1视频 2专栏
                    .addBodyPara("page", page)
                    .addBodyPara("userToken", "")
                    .bodyType("json")
                    .post().getResult().getBody().toBean(JSONObject.class);
            if (body.getInteger("code") == 1) {
                JSONObject data = body.getJSONObject("data");
                JSONArray list = data.getJSONArray("list");
                List<Pai> pais = JSONArray.parseArray(list.toJSONString(), Pai.class).stream().peek(i -> {
                    i.setType(type);
                    if (i.getTags() != null) {
                        i.setTagStr(i.getTags().stream().map(Tag::getTagTitle).collect(Collectors.joining(",")));
                    }
                }).collect(Collectors.toList());
                List<Tag> tags = new ArrayList<>();
                List<Columnist> columnists = new ArrayList<>();
                for (Pai pai : pais) {
                    List<Columnist> columnist = pai.getColumnist();
                    if (columnist != null && columnist.size() > 0) {
                        columnists.addAll(columnist);
                    }
                    List<Tag> tagList = pai.getTags();
                    if (tagList != null && tagList.size() > 0) {
                        tags.addAll(tagList);
                    }
                }

                for (Tag tag : tags) {
                    if (!tagRepository.findById(tag.getTagId()).isPresent()) {
                        tagRepository.save(tag);
                    }
                }
                for (Columnist columnist : columnists) {
                    if (!columnistRepository.findById(columnist.getColumnistId()).isPresent()) {
                        columnistRepository.save(columnist);
                    }
                }
                for (Pai pai : pais) {
                    Pai libraryIdAndType = paiRepository.findPaiByLibraryIdAndType(pai.getLibraryId(), pai.getType());
                    if (libraryIdAndType == null) {
                        consecutiveNumbers -= consecutiveNumbers;
                        paiRepository.save(pai);
                    } else {
                        consecutiveNumbers++;
                    }
                }

                if (data.getInteger("hasNextPage") != 1 || consecutiveNumbers > maxConsecutiveNumbers) {
                    break;
                }
                page++;
            }
        }
    }
}
