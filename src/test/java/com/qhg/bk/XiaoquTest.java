package com.qhg.bk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.qhg.bk.dao.XiaoquRepository;
import com.qhg.bk.model.Xiaoqu;
import com.qhg.dy.mapper.AwemeMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class XiaoquTest {
    @Resource
    XiaoquRepository xiaoquRepository;

    @Test
    void t1() throws IOException {
        String addr = "[\"https://xa.ke.com/xiaoqu/beilin/\", \"https://xa.ke.com/xiaoqu/weiyang/\", \"https://xa.ke.com/xiaoqu/baqiao/\", \"https://xa.ke.com/xiaoqu/xinchengqu/\", \"https://xa.ke.com/xiaoqu/lintong/\", \"https://xa.ke.com/xiaoqu/yanliang/\", \"https://xa.ke.com/xiaoqu/changan7/\", \"https://xa.ke.com/xiaoqu/lianhu/\", \"https://xa.ke.com/xiaoqu/yanta/\", \"https://xa.ke.com/xiaoqu/lantian/\", \"https://xa.ke.com/xiaoqu/huyiqu/\", \"https://xa.ke.com/xiaoqu/zhouzhi/\", \"https://xa.ke.com/xiaoqu/gaoling1/\", \"https://xa.ke.com/xiaoqu/xixianxinquxian/\"]";
        JSONArray addrs = JSON.parseArray(addr);
        for (Object o : addrs) {
            int page = 1;
            while (true) {
                Document document = Jsoup.connect(o + "pg" + page + "/").get();
                Elements region = document.select("#beike > div.xiaoquListPage > div.m-filter > div.position > dl:nth-child(2) > dd > div > div:nth-child(1)");
                String regionName = region.get(0).select("a[class=selected CLICKDATA]").get(0).text();
                Element dataDiv = document.select("div[data-component=list]").get(0);
                Elements select = dataDiv.select("ul[class=listContent]");
                if (select.size() > 0) {//有数据
                    ArrayList<Xiaoqu> objects = new ArrayList<>();
                    for (Element element : select.get(0).children()) {
                        String xqid = element.attr("data-id");
                        String name = element.getElementsByTag("a").get(0).attr("title");
                        Xiaoqu xiaoqu = new Xiaoqu();
                        xiaoqu.setInfo(element.toString());
                        xiaoqu.setRegion(regionName);
                        xiaoqu.setXqid(xqid);
                        xiaoqu.setName(name);
                        objects.add(xiaoqu);
                        xiaoquRepository.saveAll(objects);
                    }
                    log.info("保存成功(" + objects.stream().map(Xiaoqu::getName).collect(Collectors.joining("; ")) + ")");
                } else {
                    select = dataDiv.select("div[class=m-noresult]");
                    if (select.size() > 0) {
                        break;
                    }
                }
                page++;
            }
        }

    }

    @org.junit.jupiter.api.Test
    void t2() throws IOException {
        String addr = "[\"https://xa.ke.com/xiaoqu/beilin/\", \"https://xa.ke.com/xiaoqu/weiyang/\", \"https://xa.ke.com/xiaoqu/baqiao/\", \"https://xa.ke.com/xiaoqu/xinchengqu/\", \"https://xa.ke.com/xiaoqu/lintong/\", \"https://xa.ke.com/xiaoqu/yanliang/\", \"https://xa.ke.com/xiaoqu/changan7/\", \"https://xa.ke.com/xiaoqu/lianhu/\", \"https://xa.ke.com/xiaoqu/yanta/\", \"https://xa.ke.com/xiaoqu/lantian/\", \"https://xa.ke.com/xiaoqu/huyiqu/\", \"https://xa.ke.com/xiaoqu/zhouzhi/\", \"https://xa.ke.com/xiaoqu/gaoling1/\", \"https://xa.ke.com/xiaoqu/xixianxinquxian/\"]";
        JSONArray addrs = JSON.parseArray(addr);
        for (Object o : addrs) {
            int page = 1;
            while (true) {
                JSONObject object = OkHttps.async("https://m.ke.com/liverpool/api/webApiProxy/secondhand/resblock/search?=&=%2Fbeilin%2Fpg3&=3")
                        .addUrlPara("cityId", "610100")
                        .addUrlPara("curPage", page)
                        .addUrlPara("condition", o.toString().replace("https://xa.ke.com/xiaoqu", "") + "pg" + page)
                        .get().getResult().getBody().cache().toBean(JSONObject.class);

                if (object.getInteger("code") == 100000) {//有数据
                    JSONObject data0 = object.getJSONObject("data");
                    if (data0.getInteger("errno") == 0 && data0.containsKey("data")) {
                        ArrayList<Xiaoqu> objects = new ArrayList<>();
                        JSONObject data = data0.getJSONObject("data");
                        int hasMoreData = data.getIntValue("hasMoreData");
                        JSONArray list = data.getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject objecti = list.getJSONObject(i);
                            Xiaoqu xiaoqu = new Xiaoqu();
                            xiaoqu.setInfo(objecti.toJSONString());
                            xiaoqu.setRegion(objecti.getString("districtName"));
                            xiaoqu.setXqid(objecti.getLong("id").toString());
                            xiaoqu.setName(objecti.getString("name"));
                            objects.add(xiaoqu);
                            xiaoquRepository.saveAll(objects);
                        }
                        log.info("保存成功(" + objects.stream().map(Xiaoqu::getName).collect(Collectors.joining("; ")) + ")");
                        if (hasMoreData == 0)
                            break;
                    }
                    page++;
                }

            }
        }

    }
}
