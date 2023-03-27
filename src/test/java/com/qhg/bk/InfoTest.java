package com.qhg.bk;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.qhg.bk.dao.XiaoquBigInfoRepository;
import com.qhg.bk.dao.XiaoquInfoRepository;
import com.qhg.bk.dao.XiaoquRepository;
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
import javax.persistence.criteria.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class InfoTest {
    @Resource
    XiaoquRepository xiaoquRepository;
    @Resource
    XiaoquInfoRepository xiaoquInfoRepository;
    @Resource
    XiaoquBigInfoRepository xiaoquBigInfoRepository;

//    public static void main(String[] args) {
//        JSONObject jsonObject = new JSONObject() {{
//            put("aaa", "bbb");
//        }};
//        jsonObject.put("aaa", "ccc");
//        System.out.println(jsonObject);
//    }

    @Test
    void t1() throws IOException {
        List<XiaoquInfo> all = xiaoquInfoRepository.findAll();
        doWork(all.stream().filter(i -> i.getStatus() == 5).collect(Collectors.toList()));
    }

    //    public static void main(String[] args) throws IOException {
//        Document document = Jsoup.connect("https://m.ke.com/xa/xiaoqu/3811059220426").get();
//        Elements select = document.select("script[charset=utf-8]");
//        Element last = select.last();
//        String trim = last.data().replace("window.__PRELOADED_STATE__ = ", "").trim();
//        System.out.println(JSON.parseObject(trim.substring(0, trim.length() - 1)));
//    }
    public static void main(String[] args) {
        JSONObject bean = OkHttps.async("https://wxapp.api.ke.com/openapi/ershouc/xcx/xiaoqu/detail/part0")
                .addUrlPara("id", 3811061645782L)
                .addUrlPara("sign", "")
                .addHeader("Authorization", Shell.getAuthorization("{url}?id=" + 3811061645782L + "&sign="))
                .addHeader("Time-Stamp", System.currentTimeMillis() + "")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 MicroMessenger/7.0.4.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF")
                .addHeader("Lianjia-Source", "ljwxapp")
                .get().getResult().getBody().cache().toBean(JSONObject.class);

        if (bean.containsKey("data") && bean.getInteger("errno") == 0) {
            JSONObject data = bean.getJSONObject("data");
            System.out.println(data);
            JSONArray pictureList = data.getJSONObject("headerInfos").getJSONArray("pictureList");
            JSONArray list = data.getJSONArray("list");
            JSONObject jsonObject = pictureList.getJSONObject(0);
            if (jsonObject.getInteger("groupId") == 1) {
                JSONArray imgUrlList = jsonObject.getJSONArray("imgUrlList");
                for (int i = 0; i < imgUrlList.size(); i++) {
                    JSONObject imgUrlListJSONObject = imgUrlList.getJSONObject(i);
                    String url = imgUrlListJSONObject.getString("url");
                    System.out.println(getImageUrl(url));
                }
            }

            for (int i = 0; i < list.size(); i++) {
                JSONObject listJSONObject = list.getJSONObject(i);
                String navKey = listJSONObject.getString("navKey");
                if ("introduceInfo".equals(navKey)) {
                    JSONArray cardList = listJSONObject.getJSONArray("cardList");
                    for (int i1 = 0; i1 < cardList.size(); i1++) {
                        JSONObject cardListJSONObject = cardList.getJSONObject(i1);
                        String cardPoolType = cardListJSONObject.getString("cardPoolType");
                        if ("introduceDetailCard".equals(cardPoolType)) {
                            JSONObject info1 = cardListJSONObject.getJSONObject("info");
                            JSONArray detailInfo = info1.getJSONArray("detailInfo");
                            for (int i2 = 0; i2 < detailInfo.size(); i2++) {
                                JSONObject detailInfoJSONObject = detailInfo.getJSONObject(i2);
                                JSONArray contents = detailInfoJSONObject.getJSONArray("contents");
                                for (int i3 = 0; i3 < contents.size(); i3++) {
                                    JSONObject contentsJSONObject = contents.getJSONObject(i3);
                                    String name1 = contentsJSONObject.getString("name");
                                    String value = contentsJSONObject.getString("value");
                                    System.out.println(name1 + ":" + value);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static String getImageUrl(String link) {
        try {
            URL url = URLUtil.url(link);
            String path = URLUtil.getPath(link);
            String host = URLUtil.getHost(url).getHost();
            int indexOf = path.lastIndexOf(".");
            String substring = path.substring(0, indexOf);
            int lastIndexOf = substring.lastIndexOf(".");
            substring = substring.substring(0, lastIndexOf);
            return "https://" + host + substring;
        } catch (Exception e) {
            return link;
        }
    }

    private void doWork(List<XiaoquInfo> all) throws IOException {
        for (XiaoquInfo xiaoqu : all) {
            if (xiaoqu.getInfo() != null) {
                continue;
            }
            try {
                String xqid = xiaoqu.getXqId();
                JSONObject attr = new JSONObject();
                JSONObject bigInfo = new JSONObject();
                JSONObject info = new JSONObject();
                List<String> urls = new ArrayList<>();

                //途径1 h5爬取
                Document document = Jsoup.connect("https://m.ke.com/xa/xiaoqu/" + xqid).get();
                Elements select = document.select("script[charset=utf-8]");
                Element last = select.last();
                String trim = last.data().replace("window.__PRELOADED_STATE__ = ", "").trim();
                JSONObject object = JSON.parseObject(trim.substring(0, trim.length() - 1));
                JSONObject xiaoquDetail = object.getJSONObject("xiaoquDetail");
                JSONObject positionObj = xiaoquDetail.getJSONObject("position");
                Object survey = xiaoquDetail.get("survey");

                if (survey instanceof JSONArray) {
                    JSONArray surveys = (JSONArray) survey;
                    for (int i = 0; i < surveys.size(); i++) {
                        String name = surveys.getJSONObject(i).getString("name");
                        String value = surveys.getJSONObject(i).getString("value");
                        attr.put(name, value);
                    }
                } else if (survey instanceof JSONObject) {
                    Collection<Object> values = ((JSONObject) survey).values();
                    for (Object o : values) {
                        JSONObject surveyobj = (JSONObject) o;
                        String name = surveyobj.getString("name");
                        String value = surveyobj.getString("value");
                        attr.put(name, value);
                    }
                }


                String pointLat = positionObj.getString("pointLat");
                String pointLng = positionObj.getString("pointLng");
                JSONObject pageData = xiaoquDetail.getJSONObject("pageData");
                JSONArray images = pageData.getJSONArray("images");
                for (int i = 0; i < images.size(); i++) {
                    String imageUrl = images.getJSONObject(i).getString("imageUrl");
                    urls.add(getImageUrl(imageUrl));
                }
                String address = pageData.getString("address");
                String name = pageData.getString("name");
                bigInfo.put("jsoup", xiaoquDetail);
                //途径2 api爬取
                JSONObject bean = OkHttps.async("https://wxapp.api.ke.com/openapi/ershouc/xcx/xiaoqu/detail/part0")
                        .addUrlPara("id", xqid)
                        .addUrlPara("sign", "")
                        .addHeader("Authorization", Shell.getAuthorization("{url}?id=" + xqid + "&sign="))
                        .addHeader("Time-Stamp", System.currentTimeMillis() + "")
                        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 MicroMessenger/7.0.4.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF")
                        .addHeader("Lianjia-Source", "ljwxapp")
                        .get().getResult().getBody().cache().toBean(JSONObject.class);

                if (bean.containsKey("data") && bean.getInteger("errno") == 0) {
                    JSONObject data = bean.getJSONObject("data");
                    JSONArray pictureList = data.getJSONObject("headerInfos").getJSONArray("pictureList");
                    JSONArray list = data.getJSONArray("list");
                    JSONObject jsonObject = pictureList.getJSONObject(0);
                    JSONObject object1 = new JSONObject();
                    object1.put("pictureList", pictureList);
                    JSONArray imgUrlList = jsonObject.getJSONArray("imgUrlList");
                    for (int i = 0; i < imgUrlList.size(); i++) {
                        JSONObject imgUrlListJSONObject = imgUrlList.getJSONObject(i);
                        String url = imgUrlListJSONObject.getString("url");
                        urls.add(getImageUrl(url));
                    }

                    for (int i = 0; i < list.size(); i++) {
                        JSONObject listJSONObject = list.getJSONObject(i);
                        String navKey = listJSONObject.getString("navKey");
                        if ("introduceInfo".equals(navKey)) {
                            JSONArray cardList = listJSONObject.getJSONArray("cardList");
                            for (int i1 = 0; i1 < cardList.size(); i1++) {
                                JSONObject cardListJSONObject = cardList.getJSONObject(i1);
                                String cardPoolType = cardListJSONObject.getString("cardPoolType");
                                if ("introduceDetailCard".equals(cardPoolType)) {
                                    JSONObject info1 = cardListJSONObject.getJSONObject("info");
                                    JSONArray detailInfo = info1.getJSONArray("detailInfo");
                                    object1.put("detailInfo", detailInfo);
                                    for (int i2 = 0; i2 < detailInfo.size(); i2++) {
                                        JSONObject detailInfoJSONObject = detailInfo.getJSONObject(i2);
                                        JSONArray contents = detailInfoJSONObject.getJSONArray("contents");
                                        for (int i3 = 0; i3 < contents.size(); i3++) {
                                            JSONObject contentsJSONObject = contents.getJSONObject(i3);
                                            String name1 = contentsJSONObject.getString("name");
                                            String value = contentsJSONObject.getString("value");
                                            attr.put(name1, value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    bigInfo.put("api", object1);
                }


                info.put("attr", attr);
                info.put("pointLat", pointLat);
                info.put("pointLng", pointLng);
                info.put("address", address);
                info.put("name", name);
                info.put("images", urls);
                xiaoqu.setInfo(info.toJSONString());
                XiaoquBigInfo xiaoquBigInfo = new XiaoquBigInfo();
                xiaoquBigInfo.setId(xiaoqu.getId());
                xiaoquBigInfo.setBigInfo(bigInfo.toJSONString());
                xiaoquInfoRepository.save(xiaoqu);
                xiaoquBigInfoRepository.save(xiaoquBigInfo);
                System.out.println("保存成功: " + xiaoqu.getId() + " - " + name);
            } catch (Exception e) {

            }

        }
    }

}
