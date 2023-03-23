package com.qhg.sgp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.bk.model.Xiaoqu;
import com.qhg.dy.utils.IO;
import com.qhg.sgp.model.*;
import com.qhg.sgp.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class PaiInfoTests {
    @Test
    void fastCollectNews() {
    }

    @Resource
    PaiRepository paiRepository;
    @Resource
    TagRepository tagRepository;
    @Resource
    PaiFilmRepository paiFilmRepository;
    @Resource
    ActorRepository actorRepository;
    @Resource
    AnchorsRepository anchorsRepository;
    @Resource
    PaiMovieRepository paiMovieRepository;
    @Resource
    PaiLibDetailRepository paiLibDetailRepository;
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
        List<Pai> all = paiRepository.findAll((Specification<Pai>) (root, query, criteriaBuilder) -> {
            Path<Integer> type = root.get("type");
            return criteriaBuilder.equal(type, 1);
        });

        for (Pai pai : all) {
            if (pai.getType() == 1) {
                if (paiLibDetailRepository.findByLibraryIdAndLtype(pai.getLibraryId(), pai.getType()) != null) {
                    continue;
                }
                PaiLibDetail libDetail = null;
                JSONObject body = http.async("https://api.cbbee0.com/v1_2/libraryDetail")
                        .addBodyPara("device_id", "")
                        .addBodyPara("hm", "008-api")
                        .addBodyPara("library_id", pai.getLibraryId())
                        .addBodyPara("ltype", 1)//1视频 2专栏
                        .addBodyPara("userToken", "")
                        .bodyType("json")
                        .post().getResult().getBody().toBean(JSONObject.class);
                if (body.getInteger("code") == 1) {
                    JSONObject data = body.getJSONObject("data");
                    JSONArray detail = data.getJSONArray("detail");
                    libDetail = body.getObject("data", PaiLibDetail.class);
                    libDetail.setDetailContent0(detail.getJSONObject(0).getString("content"));
                    libDetail.setDetailInfo1(detail.getJSONObject(1).toJSONString());
                    libDetail.setDetailContent2(detail.getJSONObject(2).getString("content"));
                    String detailInfo1 = libDetail.getDetailInfo1();
                    JSONObject detailInfo = JSONObject.parseObject(detailInfo1);
                    libDetail.setDownloadUrl(detailInfo.getString("download_url"));
                    libDetail.setEncryptionUrl(detailInfo.getString("encryption_url"));
                    libDetail.setImgUrl(detailInfo.getString("img_url"));
                    libDetail.setMovieTime(detailInfo.getString("movie_time"));
                    libDetail.setVideoId(detailInfo.getInteger("video_id"));
                    libDetail.setUrl(detailInfo.getString("url"));
                    libDetail.setAuthor(libDetail.getAnchors().stream().map(Anchors::getAnchorsName).collect(Collectors.joining(",")));
                    paiLibDetailRepository.save(libDetail);
                    for (Anchors anchor : libDetail.getAnchors()) {
                        if (!anchorsRepository.findById(anchor.getAnchorsId()).isPresent()) {
                            anchorsRepository.save(anchor);
                        }
                    }
                }

                if (libDetail == null)
                    continue;
                PaiMovie paiMovie;
                body = http.async("https://api.cbbee0.com/v1_2/movieInfo")
                        .addBodyPara("device_id", "")
                        .addBodyPara("hm", "008-api")
                        .addBodyPara("id", libDetail.getVideoId())
                        .addBodyPara("userToken", "")
                        .bodyType("json")
                        .post().getResult().getBody().toBean(JSONObject.class);
                if (body.getInteger("code") == 1) {
                    paiMovie = body.getObject("data", PaiMovie.class);
                    paiMovieRepository.save(paiMovie);
                }
                if (libDetail.getFilmId() == null || libDetail.getFilmId().trim().equals("")) {
                    continue;
                }
                body = http.async("https://api.cbbee0.com/v1_2/filmInfo")
                        .addBodyPara("device_id", "")
                        .addBodyPara("hm", "008-api")
                        .addBodyPara("film_id", libDetail.getFilmId())
                        .addBodyPara("userToken", "")
                        .bodyType("json")
                        .post().getResult().getBody().toBean(JSONObject.class);
                if (body.getInteger("code") == 1) {
                    JSONArray data = body.getJSONArray("data");
                    List<PaiFilm> paiFilms = JSON.parseArray(data.toJSONString(), PaiFilm.class)
                            .stream().peek(i -> i.setTagStr(i.getTags().stream().map(Tag::getTagTitle).collect(Collectors.joining(",")))).collect(Collectors.toList());
                    for (PaiFilm paiFilm : paiFilms) {
                        List<Actor> actorNew = paiFilm.getActorNew();
                        if (actorNew != null && actorNew.size() > 0) {
                            for (Actor actor : actorNew) {
                                Actor actorInDb = actorRepository.findByName(actor.getName());
                                if (actorInDb == null) {
                                    actorRepository.save(actor);
                                }
                            }
                        }
                        List<Tag> tags = paiFilm.getTags();
                        if (tags != null && tags.size() > 0) {
                            for (Tag tag : tags) {
                                if (!tagRepository.findById(tag.getTagId()).isPresent()) {
                                    tagRepository.save(tag);
                                }
                            }
                        }
                        paiFilmRepository.save(paiFilm);
                    }
                }

            }
        }
    }
}
