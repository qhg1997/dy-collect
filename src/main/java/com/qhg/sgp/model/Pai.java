package com.qhg.sgp.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pai")
public class Pai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String subtitle;
    @JSONField(name = "library_id")
    private Integer libraryId;
    @JSONField(name = "date_number")
    private Integer dateNumber;
    private Integer type;//1视频 2专栏
    @JSONField(name = "created_at")
    private Date createdAt;
    @JSONField(name = "show_time")
    private String showTime;
    @JSONField(name = "show_time_origin")
    private Date showTimeOrigin;
    @JSONField(name = "img_url")
    private String imgUrl;
    private String tagStr;
    @Transient
    private List<Tag> tags;
    @Transient
    private List<Columnist> columnist;

    public static void main(String[] args) {
        JSONObject object = JSON.parseObject("{\"code\":1,\"msg\":\"\\u8bf7\\u6c42\\u6210\\u529f\",\"data\":{\"banner\":[],\"currentPage\":86,\"totalPage\":86,\"hasNextPage\":0,\"list\":[{\"library_id\":47,\"title\":\"\\u4e0d\\u8981\\u8fdb\\u53bb\\uff01\\u4f1a\\u53d8\\u6210\\u6deb\\u9b54\\u7684\\uff01~\\u5c9b\\u56fd\\u8111\\u6d1e\\u5927\\u5f00\\u7684\\u6deb\\u5c4b\\u7ed3\\u754c\\uff01\",\"subtitle\":\"SDDE-592 \\u6a1e\\u6728\\u8475 \\u3001 \\u4e09\\u539f\\u7a42\\u9999\\u3001\\u5009\\u591a\\u771f\\u592e+\\u96c6\\u4f53\\u88ab\\u50ac\\u7720\\u51cc\\u8fb1\\uff01\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-13-39-10-5e56047ed5004.png\",\"date_number\":9,\"show_time\":\"01\\u670820\\u65e5 05:05\",\"created_at\":\"2020-02-26 13:41:26\",\"tags\":[{\"tag_id\":64,\"tag_title\":\"\\u4e09\\u539f\\u7a57\\u9999\"},{\"tag_id\":74,\"tag_title\":\"\\u6deb\\u5c4b\\u7ed3\\u754c\"},{\"tag_id\":199,\"tag_title\":\"\\u4ed3\\u591a\\u771f\\u592e\"},{\"tag_id\":20,\"tag_title\":\"\\u5267\\u60c5\"},{\"tag_id\":84,\"tag_title\":\"\\u50ac\\u7720\"},{\"tag_id\":25,\"tag_title\":\"COSPLAY\"}],\"show_time_origin\":\"2020-01-20 05:05:03\"},{\"library_id\":46,\"title\":\"\\u91cc\\u7f8e\\u5c01\\u795e\\u4e4b\\u4f5c\\uff01\\u6b32\\u62d2\\u8fd8\\u8fce\\u4e3b\\u9898\\u7684\\u5dc5\\u5cf0\\uff01\",\"subtitle\":\"SHKD-744 \\u91cc\\u7f8e\\u5c24\\u5229\\u5a05 \\u4eca\\u5929\\u9e21\\u513f\\u53c8\\u8981\\u52a0\\u73ed\\u4e86\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-13-35-47-5e5603b32a06d.png\",\"date_number\":8,\"show_time\":\"01\\u670819\\u65e5 05:00\",\"created_at\":\"2020-02-26 13:38:34\",\"tags\":[{\"tag_id\":200,\"tag_title\":\"\\u91cc\\u7f8e\\u5c24\\u8389\\u4e9a\"},{\"tag_id\":39,\"tag_title\":\"\\u9ed1\\u4e1d\"},{\"tag_id\":98,\"tag_title\":\"\\u795e\\u4f5c\"},{\"tag_id\":114,\"tag_title\":\"\\u51cc\\u8fb1\"},{\"tag_id\":262,\"tag_title\":\"OL\\u5236\\u670d\"},{\"tag_id\":138,\"tag_title\":\"\\u8fde\\u88e4\\u889c\"}],\"show_time_origin\":\"2020-01-19 05:00:05\"},{\"library_id\":45,\"title\":\"\\u4e3a\\u4e86\\u4fdd\\u62a4\\u751f\\u75c5\\u4e14\\u865a\\u5f31\\u7684\\u59d0\\u59d0\\u4e0d\\u88ab\\u517b\\u7236\\u6b3a\\u51cc\\uff0c\\u5fae\\u4e73\\u7684\\u59b9\\u59b9\\u6210\\u4e86\\u6211\\u7684\\u5973\\u4ec6\\uff01\",\"subtitle\":\"MIAA-172 \\u6c38\\u6fd1\\u552f \\u6fc0\\u53d1\\u6211\\u7684\\u4fdd\\u62a4\\u6b32 \\u786c\\u786cder!\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-13-34-31-5e5603678f3fd.png\",\"date_number\":7,\"show_time\":\"01\\u670816\\u65e5 05:04\",\"created_at\":\"2020-02-26 13:35:00\",\"tags\":[{\"tag_id\":172,\"tag_title\":\"\\u6c38\\u6fd1\\u552f\"},{\"tag_id\":173,\"tag_title\":\"\\u841d\\u8389\"},{\"tag_id\":174,\"tag_title\":\"\\u5c0f\\u53ea\\u9a6c\"},{\"tag_id\":20,\"tag_title\":\"\\u5267\\u60c5\"},{\"tag_id\":38,\"tag_title\":\"\\u5f3a\\u5978\"},{\"tag_id\":146,\"tag_title\":\"\\u5973\\u4ec6\"}],\"show_time_origin\":\"2020-01-16 05:04:05\"},{\"library_id\":44,\"title\":\"\\u6bbf\\u5802\\u7ea7\\u80c1\\u8feb\\u8c03\\u6559\\u7247\\uff01\\u770b\\u5b8c\\u8fd9\\u90e8\\u8fd8\\u80fd\\u5fcd\\u4f4f\\u4e0d\\u4ea4\\u67aa\\u90a3\\u662f\\u771f\\u725b\\u6279\\uff01\",\"subtitle\":\"STARS-171 \\u672c\\u5e84\\u94c3&\\u5c0f\\u4ed3\\u7531\\u83dc \\u5728\\u597d\\u95fa\\u871c\\u9762\\u524d\\u60e8\\u906d\\u8f6e\\u59e6\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-13-30-05-5e56025d70155.png\",\"date_number\":6,\"show_time\":\"01\\u670813\\u65e5 05:03\",\"created_at\":\"2020-02-26 13:32:12\",\"tags\":[{\"tag_id\":201,\"tag_title\":\"\\u5c0f\\u4ed3\\u7531\\u83dc\"},{\"tag_id\":202,\"tag_title\":\"\\u672c\\u5e84\\u94c3\"},{\"tag_id\":20,\"tag_title\":\"\\u5267\\u60c5\"},{\"tag_id\":98,\"tag_title\":\"\\u795e\\u4f5c\"},{\"tag_id\":85,\"tag_title\":\"\\u7fa4P\"},{\"tag_id\":38,\"tag_title\":\"\\u5f3a\\u5978\"},{\"tag_id\":47,\"tag_title\":\"\\u8c03\\u6559\"}],\"show_time_origin\":\"2020-01-13 05:03:02\"},{\"library_id\":43,\"title\":\"\\u88ab\\u5973\\u8131\\u72f1\\u72af\\u6309\\u5728\\u5bb6\\u91cc\\u968f\\u610f\\u6469\\u64e6\\u7684\\u6b7b\\u5b85\",\"subtitle\":\"CJOD-217 \\u4f50\\u85e4\\u7231\\u9732 \\u6deb\\u4e71\\u9738\\u6c14\\u5c55\\u73b0\\uff01\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/2021\\/2\\/5\\/753163efd32c86d3c96410f8d99d6618.jpg\",\"date_number\":5,\"show_time\":\"01\\u670812\\u65e5 05:01\",\"created_at\":\"2020-02-26 13:29:02\",\"tags\":[{\"tag_id\":203,\"tag_title\":\"\\u4f50\\u85e4\\u827e\\u9732\"},{\"tag_id\":204,\"tag_title\":\"\\u957f\\u8eab\\u5973\"},{\"tag_id\":205,\"tag_title\":\"\\u7eb9\\u8eab\"},{\"tag_id\":20,\"tag_title\":\"\\u5267\\u60c5\"}],\"show_time_origin\":\"2020-01-12 05:01:05\"},{\"library_id\":42,\"title\":\"\\u5de8\\u4e73\\u98ce\\u4fd7\\u5a18\\u7684\\u8d85\\u523a\\u6fc0\\u5927\\u5b9d\\u5251\",\"subtitle\":\"BF-557\\t\\u677e\\u4e0b\\u7eb1\\u8363\\u5b50 \\u8d85\\u8272\\u670d\\u88c5 \\u6781\\u4e0a\\u4f8d\\u5949\\uff01\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-13-01-07-5e55fb93b0211.png\",\"date_number\":4,\"show_time\":\"01\\u670809\\u65e5 05:00\",\"created_at\":\"2020-02-26 13:12:31\",\"tags\":[{\"tag_id\":206,\"tag_title\":\"\\u677e\\u4e0b\\u7eb1\\u8363\\u5b50\"},{\"tag_id\":207,\"tag_title\":\"\\u98ce\\u4fd7\\u5e97\"},{\"tag_id\":94,\"tag_title\":\"\\u5de8\\u4e73\"},{\"tag_id\":208,\"tag_title\":\"\\u8f7b\\u719f\\u5973\"}],\"show_time_origin\":\"2020-01-09 05:00:00\"},{\"library_id\":41,\"title\":\"\\u6211\\u88ab\\u8ba8\\u538c\\u7684\\u7537\\u4eba\\u7ed1\\u4f4f\\u641e\\u5230\\u5feb\\u6b7b\\u822c\\u9ad8\\u6f6e\\u2026\",\"subtitle\":\"\\u8089\\u68d2\\u5b9e\\u5728\\u592a\\u7d66\\u529b\\uff01\\u4eba\\u59bb\\u5ddd\\u4e0a\\u5948\\u5948\\u7f8e\\u65e0\\u529b\\u62db\\u67b6\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-12-58-14-5e55fae6c6255.png\",\"date_number\":3,\"show_time\":\"01\\u670806\\u65e5 05:02\",\"created_at\":\"2020-02-26 12:58:24\",\"tags\":[{\"tag_id\":209,\"tag_title\":\"\\u5ddd\\u4e0a\\u5948\\u5948\\u7f8e\"},{\"tag_id\":20,\"tag_title\":\"\\u5267\\u60c5\"},{\"tag_id\":13,\"tag_title\":\"\\u4eba\\u59bb\"},{\"tag_id\":114,\"tag_title\":\"\\u51cc\\u8fb1\"}],\"show_time_origin\":\"2020-01-06 05:02:05\"},{\"library_id\":40,\"title\":\"\\u4eba\\u7f8e\\u6761\\u9753\\u7684\\u5c0f\\u6bcd\\u72d7~\\u5f69\\u7f8e\\u65ec\\u679c\\u7684\\u987a\\u4ece\\u8c03\\u6559\\u7247\\u513f\\u6765\\u4e86!\",\"subtitle\":\"INU-050\\t\\u5f69\\u7f8e\\u65ec\\u679c \\u6c6a\\u6c6a\\u6c6a~\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-12-46-44-5e55f8341ea9c.jpg\",\"date_number\":2,\"show_time\":\"01\\u670804\\u65e5 05:03\",\"created_at\":\"2020-02-26 12:52:47\",\"tags\":[{\"tag_id\":210,\"tag_title\":\"\\u5f69\\u7f8e\\u65ec\\u679c\"},{\"tag_id\":47,\"tag_title\":\"\\u8c03\\u6559\"}],\"show_time_origin\":\"2020-01-04 05:03:01\"},{\"library_id\":39,\"title\":\"\\u7f8e\\u5973\\u4e0e\\u91ce\\u517d\\uff01\\u554a\\u4e0d\\uff01~\\u7f8e\\u5973\\u4e0e\\u732a\\u7321\\uff01\",\"subtitle\":\"SNIS-115 \\u7460\\u5ddd\\u8389\\u5a1c\\u7b80\\u76f4\\u4e27\\u5fc3\\u75c5\\u72c2\",\"img_url\":\"https:\\/\\/img.9a34b7.com\\/article\\/20\\/02\\/26\\/2020-02-26-12-40-13-5e55f6ad5af1c.png\",\"date_number\":1,\"show_time\":\"01\\u670801\\u65e5 10:02\",\"created_at\":\"2020-02-26 12:45:53\",\"tags\":[{\"tag_id\":9,\"tag_title\":\"\\u7f8e\\u5c11\\u5973\"},{\"tag_id\":164,\"tag_title\":\"\\u730e\\u5947\"},{\"tag_id\":211,\"tag_title\":\"\\u7460\\u5ddd\\u8389\\u5a1c\"}],\"show_time_origin\":\"2020-01-01 10:02:15\"}]},\"time\":1679285219}");
        JSONArray list = object.getJSONObject("data").getJSONArray("list");
        List<Pai> pais = JSONArray.parseArray(list.toJSONString(), Pai.class);
        System.out.println(pais);
    }
}

