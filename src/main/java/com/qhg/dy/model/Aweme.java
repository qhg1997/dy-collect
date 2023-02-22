package com.qhg.dy.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Data
public class Aweme {
    private Long id;
    private String awemeId;
    private String secUid;
    private String desc;
    private String title;
    private String info;
    private Date createTime;
    private Integer awemeType;

    public Aweme() {
    }

    public Aweme(JSONObject object) {
        this.info = (object.toJSONString());
        this.awemeId = (object.getString("aweme_id"));
        this.desc = (object.getString("desc"));
        this.title = (object.getString("preview_title"));
        this.createTime = (new Date(object.getLongValue("create_time") * 1000));
        this.awemeType = object.getInteger("aweme_type");
    }

    public static List<Aweme> list(JSONArray list) {
        return list(list, null);
    }

    public static List<Aweme> list(JSONArray list, Consumer<Aweme> consumer) {
        List<Aweme> awemes = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            JSONObject object = list.getJSONObject(i);
            Aweme aweme = new Aweme(object);
            if (consumer != null) {
                consumer.accept(aweme);
            }
            awemes.add(aweme);
        }
        return awemes;
    }

}
