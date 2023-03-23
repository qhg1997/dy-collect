package com.qhg.sgp.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pai_movie")
public class PaiMovie {/*解说*/
    @Id
    private Integer id;
    private String title;
    private String subtitle;
    @JSONField(name = "created_at")
    private String createdAt;
    /**/
    @JSONField(name = "download_url")
    private String downloadUrl;
    @JSONField(name = "encryption_url")
    private String encryptionUrl;
    @JSONField(name = "file_name")
    private String fileName;
    @JSONField(name = "img_url")
    private String imgUrl;
    @JSONField(name = "movie_time")
    private String movieTime;
    @JSONField(name = "play_count")
    private Integer playCount;
    private String url;

}

