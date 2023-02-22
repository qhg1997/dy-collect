package com.qhg.dy.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class SubUser {
    private Long id;
    private String uid;
    private String shortId;
    private String nickname;
    private String signature;
    private String secUid;
    private String info;
    private Integer subjectId;//主体id
    private Integer subType;//1黑名单 2关注
    private Integer ryAwemeCount;
    private Integer ryAwemeStatus;// 0未解析  1未完成解析 2解析至 ry_aweme_time
    private Date ryAwemeTime;

    public static List<SubUser> list(JSONArray blockList) {
        List<SubUser> subUsers = new ArrayList<>(blockList.size());
        for (int i = 0; i < blockList.size(); i++) {
            JSONObject object = blockList.getJSONObject(i);
            SubUser subUser = new SubUser();
            subUser.setInfo(object.toJSONString());
            subUser.setUid(object.getString("uid"));
            subUser.setShortId(object.getString("short_id"));
            subUser.setNickname(object.getString("nickname"));
            subUser.setSignature(object.getString("signature"));
            subUser.setSecUid(object.getString("sec_uid"));
            subUsers.add(subUser);
        }
        return subUsers;
    }
}
