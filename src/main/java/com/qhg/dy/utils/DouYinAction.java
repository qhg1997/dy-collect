package com.qhg.dy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.model.SubUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DouYinAction {
    public final String COOKIE;
    public final HTTP http;

    public DouYinAction(String cookie) {
        this.COOKIE = cookie;
        http = HTTP.builder()
                .baseUrl("https://api5-normal-c-lf.amemv.com")
                .addPreprocessor(p -> {
                    p.getTask().addHeader("Cookie", this.COOKIE);
                    p.proceed();
                })
                .addMsgConvertor(new FastjsonMsgConvertor())
                .build();
    }

    /**
     * 提取分享链接
     */
    public static void extract() {
        String link = "https://www.iesdouyin.com/share/user";
        String content = IO.readContentAsString(new File("C:\\Users\\40477\\Desktop\\blockList.json"));
        JSONArray jsonArray = JSONArray.parseArray(content);
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            JSONArray block_list = object.getJSONArray("block_list");
            for (int i1 = 0; i1 < block_list.size(); i1++) {
                JSONObject blockListJSONObject = block_list.getJSONObject(i1);
                String nickname = blockListJSONObject.getString("nickname");
                String sec_uid = blockListJSONObject.getString("sec_uid");
                System.out.println(nickname + "        " + link + sec_uid);
                strings.add(nickname + "        " + link + sec_uid);
            }
        }
        IO.writeContent(String.join("\n", strings), new File("C:\\Users\\40477\\Desktop\\blockList.list"));

    }

    public List<SubUser> blockAllList() throws InterruptedException {
        int index = 0;
        int count = 10;
        HttpCall call;
        List<SubUser> subUsers = new ArrayList<>();
        while (true) {
            call = http.async("/aweme/v1/user/block/list/")
                    .addUrlPara("index", index)
                    .addUrlPara("count", count)
                    .addUrlPara("aid", "1128")
                    .get();
            HttpResult.Body body = call.getResult().getBody().cache();
            JSONObject object = JSONObject.parseObject(body.toString());
            JSONArray block_list = object.getJSONArray("block_list");
            System.out.println(index + "," + count + " status_code: " + object.getInteger("status_code") + " count: " + block_list.size());
            subUsers.addAll(SubUser.list(block_list));
            if (!object.getBoolean("has_more")) {
                break;
            }
            index += count;
            TimeUnit.MILLISECONDS.sleep(1500);
        }
        return subUsers;
    }

    private static void parseUrl() {
        String a = "?device_platform=webapp&aid=6383&channel=channel_pc_web&sec_user_id=MS4wLjABAAAAshepXuFkR2knQLxsDdTXC_2BLn3RZ6jOjEqCMy1gk8k&max_cursor=1670217957000&locate_item_id=7198094350201507105&locate_query=false&show_live_replay_strategy=1&count=20&publish_video_strategy_type=2&pc_client_type=1&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1920&screen_height=1080&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=110.0.1587.50&browser_online=true&engine_name=Blink&engine_version=110.0.0.0&os_name=Windows&os_version=10&cpu_core_num=8&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=250&webid=7202107445362640387&msToken=RTqyqu50hqj0D4iJjHp0vTir-rSqqjctak9EeawmemZxGmbe4UxvfqPr1e3Qc_LM3ImDEzmBKyN3JR1qSN1CsVKW13pZ-LKhUH0SOb8dSprmg1v8Q63ery0x8JIJJQM=&X-Bogus=DFSzswVLFFbANjigSgpaXGoB6lQb";

        String url = "" +
                "/aweme/v1/aweme/post/?publish_video_strategy_type=2&source=0&user_avatar_shrink=96_96&video_cover_shrink=248_330&max_cursor=1676253877000&sec_user_id=MS4wLjABAAAAk869qJaabr65noUOHfR6-LFnUCDOiXZK0oHBRAl27eGLxhFaE9lyJF8awb5Xr_CO&count=12&show_live_replay_strategy=1&is_order_flow=0&page_from=2&location_permission=1&collects_id&familiar_collects=0&post_serial_strategy=0&iid=119187649607335&device_id=2744795635389447&ac=wifi&channel=update&aid=1128&app_name=aweme&version_code=230000&version_name=23.0.0&device_platform=android&os=android&ssmix=a&device_type=HD1910&device_brand=OnePlus&language=zh&os_api=22&os_version=5.1.1&manifest_version_code=230001&resolution=720*1280&dpi=240&update_version_code=23009900&_rticket=1676868670917&package=com.ss.android.ugc.aweme&mcc_mnc=46000&cpu_support64=false&host_abi=armeabi&ts=1676868670&appTheme=light&app_type=normal&need_personal_recommend=1&is_guest_mode=0&minor_status=0&is_android_pad=0&cdid=869fa1ee-ca28-424c-bdaa-dc0d9ad43ea6&md=0" +
                "";
        url = a;
        String[] split = url.split("\\?");
        System.out.println(split[0]);
        String[] strings = split[1].split("&");
        for (String string : strings) {
            String[] strings1 = string.split("=");
            String k = strings1[0];
            String v = strings1[1];
            System.out.println(".addUrlPara(\"" + k + "\",\"" + v + "\")");
        }
    }

    /**
     * 移除黑名单
     */
    public HttpResult.Body block(String shortId, String secUid, boolean action) {
        HttpCall call = http.async("/aweme/v1/user/block/")
                .addUrlPara("aid", "1128")
                .addBodyPara("user_id", shortId)
                .addBodyPara("sec_user_id", secUid)
                .addBodyPara("block_type", action ? "1" : "0")
                .addBodyPara("source", "0")
                .post();
        return call.getResult().getBody().cache();
    }


    /**
     * 拉黑
     */
    public HttpResult.Body block(String shortId, String secUid) {
        return block(shortId, secUid, true);
    }

    /**
     * 关注
     */
    public HttpResult.Body follow(String secUid, boolean action) {
        HttpCall call = http.async("/aweme/v1/commit/follow/user/")
                .addUrlPara("sec_user_id", secUid)

                .addUrlPara("type", action ? "1" : "0")
                .addUrlPara("need_mark_friend", "0")

                .addUrlPara("aid", "1128")
                .addUrlPara("from_pre", "0")
                .addUrlPara("detail_type", "0")
                .addUrlPara("city", "610100")
                .addUrlPara("channel_id", "3")
                .addUrlPara("address_book_access", "0")
                .addUrlPara("from_action", "19001")
                .addUrlPara("from", "19")
                .get();
        return call.getResult().getBody().cache();
    }

    public HttpResult.Body follow(String secUid) {
        return follow(secUid, true);
    }

    public static void main(String[] args) {
        parseUrl();
    }
}
