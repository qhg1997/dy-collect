package com.qhg.dy.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Data
public class AwemeResource {
    private Long id;
    private Integer type;// 0 视频 1 图片
    private String secUid;
    private String awemeId;
    private String safeFileName;
    private String uri;
    private String localStorageAddress;
    private Integer downed;//0 未下载 1 已下载

    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? "TODO" : authorName;
    }

    private String authorName;//作者名

    private String desc;
    private String title;
    private String info;
    private Date createTime;

    public AwemeResource() {
    }

    public AwemeResource(JSONObject object) {
        this.info = (object.toJSONString());
        this.awemeId = (object.getString("aweme_id"));
        this.desc = (object.getString("desc"));
        this.title = (object.getString("preview_title"));
        this.createTime = (new Date(object.getLongValue("create_time") * 1000));
    }

    public static List<AwemeResource> list(JSONArray list) {
        return list(list, null);
    }

    public static List<AwemeResource> list(JSONArray list, Consumer<AwemeResource> consumer) {
        List<AwemeResource> awemes = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            JSONObject object = list.getJSONObject(i);
            AwemeResource aweme = new AwemeResource(object);
            if (consumer != null) {
                consumer.accept(aweme);
            }
            awemes.add(aweme);
        }
        return awemes;
    }

}
