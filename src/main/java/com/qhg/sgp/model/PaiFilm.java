package com.qhg.sgp.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pai_film")
public class PaiFilm {/*原片*/
    @Id
    private Integer id;
    @Transient
    @JSONField(name = "actor_new")
    private List<Actor> actorNew;
    @JSONField(name = "actor")
    private String actor;
    @JSONField(name = "download_url")
    private String downloadUrl;
    @JSONField(name = "encryption_url")
    private String encryptionUrl;
    @JSONField(name = "file_name")
    private String fileName;
    @JSONField(name = "img_url")
    private String imgUrl;
    @JSONField(name = "introduction")
    private String introduction;
    private String title;
    @JSONField(name = "publish_at")
    private String publishAt;
    @JSONField(name = "love_number")
    private String loveNumber;
    @JSONField(name = "score")
    private BigDecimal score;
    @Transient
    @JSONField(name = "tags")
    private List<Tag> tags;
    private String tagStr;
    /**/
    @JSONField(name = "movie_time")
    private String movieTime;
    @JSONField(name = "play_count")
    private Integer playCount;
    private String url;

}

