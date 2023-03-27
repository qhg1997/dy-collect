package com.qhg.dy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.SubUser;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AwemeAction {

    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    final CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
            cookieStore.put(httpUrl, list);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            return cookieStore.getOrDefault(httpUrl, Collections.emptyList());
        }
    };

    private final HTTP http;
    private final SubUser subUser;
    private final String secUid;
    private final HTTP http0 = HTTP.builder()
            .config(
                    b -> b
//                            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("n550.kdltps.com", 15818)))
                            .addInterceptor(new Interceptor() {
                                public final int maxRetry = 5;//最大重试次数
                                private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    Response response = chain.proceed(request);
                                    while (!response.isSuccessful() && retryNum < maxRetry) {
                                        response.close();
                                        retryNum++;
                                        response = chain.proceed(request);
                                    }
                                    return response;
                                }
                            }).cookieJar(cookieJar)
                            .connectTimeout(1, TimeUnit.MINUTES)
                            .readTimeout(1, TimeUnit.MINUTES)
                            .writeTimeout(1, TimeUnit.MINUTES))
            .addMsgConvertor(new FastjsonMsgConvertor())
            .build();
    private Runnable finishAction;
    private boolean finishActionStatus;//false 未执行 true 已执行
    private Runnable beforeAction;
    private boolean beforeActionStatus;
    private Runnable beginAction;
    private boolean beginActionStatus;
    private Consumer<Integer> onErrorAction;

    public AwemeAction(SubUser subUser) {
        this.subUser = subUser;
        this.secUid = subUser.getSecUid();
        http = HTTP.builder()
                .config(b -> b.addInterceptor(new IInterceptor()).cookieJar(cookieJar)
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(1, TimeUnit.MINUTES)
                )
                .baseUrl("https://www.douyin.com")
                .addPreprocessor(p -> {
                    HttpTask<?> httpTask = p.getTask()

                            .addHeader("referer", "https://www.douyin.com/")
                            .addHeader("Cookie", "" +
"passport_csrf_token=92817afe71510f557af8e6695b30c654; passport_csrf_token_default=92817afe71510f557af8e6695b30c654; s_v_web_id=verify_leph39jp_lajml87X_s7z0_4FGy_8zw0_eiS87tTmlBEO; n_mh=i2a1GWUt2fNfatSZl3luM-aVn3l5TOs2nf2JV1Pe-1o; sso_auth_status=2ee08043676d312605159ea574ddc4b8; sso_auth_status_ss=2ee08043676d312605159ea574ddc4b8; store-region=cn-sn; store-region-src=uid; publish_badge_show_info=%220%2C0%2C0%2C1679117978639%22; ttwid=1%7Cr5HwlYQPwzHrt_x08LcP9hmdE97Js3ZbiDYSh2yFGyM%7C1679118362%7C7b4766d6efd72a1cc840ce3cbb7dd768033f0614cee67c4851a94bff7f6184a3; _tea_utm_cache_2018=undefined; VIDEO_FILTER_MEMO_SELECT=%7B%22expireTime%22%3A1680166377577%2C%22type%22%3A1%7D; __ac_nonce=0641d436f0052bb853b77; __ac_signature=_02B4Z6wo00f01BAIepQAAIDBAFafrsUZxnAQKH4AAGAl5CFEi6hgCAt-p2W.3St46h4CCKBwPkHkeHS4bFWEZGlMQlpkzfTWgru-o4EDt2c4VMhtsLsz7TqIDDVjbfxJRgo.pV9nQCh1c9uOfc; strategyABtestKey=%221679639410.084%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWNsaWVudC1jZXJ0IjoiLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tXG5NSUlDRXpDQ0FicWdBd0lCQWdJVVNQalVmUmdNUXJDeXJ4aEpGMGhCYTYzMCtmVXdDZ1lJS29aSXpqMEVBd0l3XG5NVEVMTUFrR0ExVUVCaE1DUTA0eElqQWdCZ05WQkFNTUdYUnBZMnRsZEY5bmRXRnlaRjlqWVY5bFkyUnpZVjh5XG5OVFl3SGhjTk1qTXdNekF4TURreU5qTXhXaGNOTXpNd016QXhNVGN5TmpNeFdqQW5NUXN3Q1FZRFZRUUdFd0pEXG5UakVZTUJZR0ExVUVBd3dQWW1SZmRHbGphMlYwWDJkMVlYSmtNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEXG5BUWNEUWdBRU1TaUVKZnlrMnlYZjFhbncwdEd6L0JOWnhYUUtEcW9teEk2Z1c0cmJReWhxYnNWUzJNTW1IK0R6XG5TK0lUVnhhWWFLNDEzNTFWNmd4WWp5NEI0a0NUMDZPQnVUQ0J0akFPQmdOVkhROEJBZjhFQkFNQ0JhQXdNUVlEXG5WUjBsQkNvd0tBWUlLd1lCQlFVSEF3RUdDQ3NHQVFVRkJ3TUNCZ2dyQmdFRkJRY0RBd1lJS3dZQkJRVUhBd1F3XG5LUVlEVlIwT0JDSUVJS3lQRmJyMlVWWFRNblA3VzUvQ1VpZk5PL1F3NGllWUM5cEZva3VpT2VkN01Dc0dBMVVkXG5Jd1FrTUNLQUlES2xaK3FPWkVnU2pjeE9UVUI3Y3hTYlIyMVRlcVRSZ05kNWxKZDdJa2VETUJrR0ExVWRFUVFTXG5NQkNDRG5kM2R5NWtiM1Y1YVc0dVkyOXRNQW9HQ0NxR1NNNDlCQU1DQTBjQU1FUUNJQVdMcFgzR0VlZFJwcUU1XG5tSkh4REt0dzJxeVlmMnl1djZpQlRDRGZYY3VhQWlBRDAraDlNTEJ1TG1ZOEY1TXlUY0xsbFZjUzN3dU9keG5SXG5xVXVEVThPY1Z3PT1cbi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS1cbiJ9; csrf_session_id=08a6a95e20634646614033c0b683bf15; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1679673600000%2F0%2F0%2F1679640127357%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1679673600000%2F0%2F1679639527358%2F0%22; download_guide=%223%2F20230324%22; d_ticket=0a51cb29ccb1127040cef22d83391e5cd8fcf; my_rd=1; douyin.com; msToken=6fSmiYcl-mTwaN5wQMzGd6vTdXHWSuLazKoxGkd6otIHhvV9EOB0Q3Q522I4nFeg9bsdr5vZ1IJ8do6Br5EDqUVGXIgDwvI68S6c_3ikEmAWXxoSRmIweJxVgnDg6xs=; tt_scid=wu1azOWzZbHw8tntfG-kad7r1i92RRj9MT7pu9xRGYKZQnE0rNQ5MelHikBlkryh51cb; passport_assist_user=CkEOMkH4kqfJFcHV_0vrybMfvJpsLbUCNKEfgG-EGQq9o4eh6wcxxeJ0LB_esJi0aqKj4UU_0F1hvy_jpHodk-HZbxpICjz0igBrODfylwTQTFdl1RLZRGdGZyIwht_2eLLFd5K5WiGKaURkOXOkWU7Cb9zhqA_dt4r_9eRNnMQvdbcQpc2sDRiJr9ZUIgEDh5R4yQ%3D%3D; sso_uid_tt=8a94b95313ea8a06b0a4c818a997a020; sso_uid_tt_ss=8a94b95313ea8a06b0a4c818a997a020; toutiao_sso_user=fcb614a3259115e5e2dccc3a00e300a2; toutiao_sso_user_ss=fcb614a3259115e5e2dccc3a00e300a2; sid_ucp_sso_v1=1.0.0-KDIzNjQyN2RkYTcwOTViZmI0ZGI2MDMxNWU2Y2YzNjEyNmQ2Zjc5ODAKHwjw8-DSh_TTAxDeivWgBhjvMSAMMPj2nvgFOAJA8QcaAmhsIiBmY2I2MTRhMzI1OTExNWU1ZTJkY2NjM2EwMGUzMDBhMg; ssid_ucp_sso_v1=1.0.0-KDIzNjQyN2RkYTcwOTViZmI0ZGI2MDMxNWU2Y2YzNjEyNmQ2Zjc5ODAKHwjw8-DSh_TTAxDeivWgBhjvMSAMMPj2nvgFOAJA8QcaAmhsIiBmY2I2MTRhMzI1OTExNWU1ZTJkY2NjM2EwMGUzMDBhMg; msToken=Z0k4AvEFf279hAJ26g61pfGcmYpDy1epKJ5P_qypdeiElQoV-URBvz8EFB396RTk2FNkusiwJa8l1cBPs4E-NuhtzzjXtG1aoisWZKP3NslbJBNPqIIUuBgcrZXdEDk=; odin_tt=5d10d5bf0d8470c9d2b1e48b63a3a57e6d5cbfd2aac7be719748d0c68108f4aebc68001c627c0ab2557fd2e3aaa726456fe8714720eb5adbd7cf34c4be8f6e37; passport_auth_status=bd8b4a0b092977de59a677ad85859775%2Ce688d625f5f2ebd665b11b5c38a79144; passport_auth_status_ss=bd8b4a0b092977de59a677ad85859775%2Ce688d625f5f2ebd665b11b5c38a79144; uid_tt=ae06d403de869feb373e40b96c059817; uid_tt_ss=ae06d403de869feb373e40b96c059817; sid_tt=3b9b840e858c831c6626f7553eb74b5b; sessionid=3b9b840e858c831c6626f7553eb74b5b; sessionid_ss=3b9b840e858c831c6626f7553eb74b5b; bd_ticket_guard_server_data=; LOGIN_STATUS=1; home_can_add_dy_2_desktop=%221%22; passport_fe_beating_status=true; sid_guard=3b9b840e858c831c6626f7553eb74b5b%7C1679639906%7C5183996%7CTue%2C+23-May-2023+06%3A38%3A22+GMT; sid_ucp_v1=1.0.0-KDQ2M2M1NmFhYWE5OGI5MzVhZmI4MWU2MzI4OWEwNDYxNDIwMTVjMTgKGwjw8-DSh_TTAxDiivWgBhjvMSAMOAJA8QdIBBoCbHEiIDNiOWI4NDBlODU4YzgzMWM2NjI2Zjc1NTNlYjc0YjVi; ssid_ucp_v1=1.0.0-KDQ2M2M1NmFhYWE5OGI5MzVhZmI4MWU2MzI4OWEwNDYxNDIwMTVjMTgKGwjw8-DSh_TTAxDiivWgBhjvMSAMOAJA8QdIBBoCbHEiIDNiOWI4NDBlODU4YzgzMWM2NjI2Zjc1NTNlYjc0YjVi"                            )
                            .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36 Edg/87.0.664.66")

                            .addUrlPara("device_platform", "webapp")
                            .addUrlPara("aid", "6383")
                            .addUrlPara("channel", "channel_pc_web")
                            .addUrlPara("locate_item_id", "7198094350201507105")
                            .addUrlPara("locate_query", "false")
                            .addUrlPara("show_live_replay_strategy", "1")
                            .addUrlPara("count", "35")
                            .addUrlPara("publish_video_strategy_type", "2")
                            .addUrlPara("pc_client_type", "1")
                            .addUrlPara("version_code", "170400")
                            .addUrlPara("version_name", "17.4.0")
                            .addUrlPara("cookie_enabled", "true")
                            .addUrlPara("screen_width", "1920")
                            .addUrlPara("screen_height", "1080")
                            .addUrlPara("browser_language", "zh-CN")
                            .addUrlPara("browser_platform", "Win32")
                            .addUrlPara("browser_name", "Edge")
                            .addUrlPara("browser_version", "110.0.1587.50")
                            .addUrlPara("browser_online", "true")
                            .addUrlPara("engine_name", "Blink")
                            .addUrlPara("engine_version", "110.0.0.0")
                            .addUrlPara("os_name", "Windows")
                            .addUrlPara("os_version", "10")
                            .addUrlPara("cpu_core_num", "8")
                            .addUrlPara("device_memory", "8")
                            .addUrlPara("platform", "PC")
                            .addUrlPara("downlink", "10")
                            .addUrlPara("effective_type", "4g")
                            .addUrlPara("round_trip_time", "50")
                            .addUrlPara("webid", "7202623704786241061")
//                            .addUrlPara("msToken", "DdssctqyIkoXmgE9sHzpkMXaw99WOmkV-039sRkH8K8ML2FGDT7tOIm9kL6RhqfetYjyFA7yrngneCv9tysMBXKSuUiFgc5kRFOES45RcMMctfHwfHQcV8APtUMp8zo")
//                            .addUrlPara("X-Bogus", "DFSzswVElgUANtqESghtU2oB6lK0")
                            ;
                    while (true) {
                        try {
                            if (p.getTask().getUrl().contains("/aweme/post")) {
                                String url = httpTask.getUrlParas().entrySet().stream().map(Objects::toString).collect(Collectors.joining("&"));
                                HttpResult.Body body = http0.async("http://127.0.0.1:5000/X-Bogus")
                                        .addBodyPara("url", url)
                                        .addBodyPara("user_agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36 Edg/87.0.664.66")
                                        .bodyType("json")
                                        .post()
                                        .getResult()
                                        .getBody()
                                        .cache();
                                JSONObject data = JSONObject.parseObject(body.toString());
                                if (data != null) {
                                    for (String key : data.keySet()) {
                                        if (key.equals("X-Bogus")) {
                                            httpTask.addUrlPara(key, data.getString(key));
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    p.proceed();
                })
                .addMsgConvertor(new FastjsonMsgConvertor())
                .build();
    }

//    public static void main(String[] args) {
//        try {
//            List<Aweme> awemeList = new AwemeAction("MS4wLjABAAAAigFjTEOAwKibGXwx9X5mWfu1uOUJWfeoHpvaXqzzRc0")
//                    .getAllAwemes((list) -> {
//                        //save;
//
//
//                        return null;
//                    });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public List<Aweme> getAllAwemes() throws InterruptedException {
        return getAllAwemes(null);
    }

    public List<Aweme> getAllAwemes(Function<List<Aweme>, Integer> function) throws InterruptedException {
        if (!beforeActionStatus)
            Optional.ofNullable(beforeAction).ifPresent(Runnable::run);
        List<Aweme> awemes = new ArrayList<>();
        String max_cursor = "0";
        int count = 5;
        while (true) {
            try {
                SHttpTask httpTask = http.sync("/aweme/v1/web/aweme/post/")
                        .addUrlPara("sec_user_id", secUid)
                        .addUrlPara("max_cursor", max_cursor);
                HttpResult.Body body = httpTask
                        .get().getBody().cache();
                JSONObject object = null;
                try {
                    object = JSONObject.parseObject(body.toString());
                } catch (Exception e) {
                    System.out.println(body);
                }
                if (object == null) {
//                    System.out.println("https://www.douyin.com/user/" + secUid);
//                    System.err.println(httpTask.getUrl() + "?" + httpTask.getUrlParas().entrySet().stream().map(Objects::toString).collect(Collectors.joining("&")));
                    if (count <= 0) {
                        System.out.println("等待过滑块验证..............");
//                        TimeUnit.SECONDS.sleep(15);
                    }
                    count--;
                    continue;
                }
                count = 5;
                JSONArray awemeList = object.getJSONArray("aweme_list");
                if (awemeList != null && awemeList.size() == 0 && onErrorAction != null) {
                    onErrorAction.accept(0);
                    return awemes;
                }
                if (awemeList != null && awemeList.size() > 0) {
                    if (!beginActionStatus)
                        Optional.ofNullable(beginAction).ifPresent(Runnable::run);
                    List<Aweme> list = Aweme.list(awemeList, (a) -> a.setSecUid(secUid));
                    for (Aweme aweme : list) {
                        if (subUser.getRyAwemeTime() != null && aweme.getCreateTime().before(subUser.getRyAwemeTime())) {
                            System.out.println(subUser.getNickname() + ": 作品已经更新至最新");
                            object.put("has_more", false);
                            break;
                        }
                    }
                    awemes.addAll(list);
                    System.out.println("刚解析出" + list.size() + "条, 已经" + awemes.size() + "条了");
                    if (function != null) {
                        Integer apply = function.apply(list);
                        System.out.println("应用结果: " + apply);
                    }
                    max_cursor = object.getString("max_cursor");
                }
                if (!object.getBooleanValue("has_more")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!finishActionStatus)
            Optional.ofNullable(finishAction).ifPresent(Runnable::run);
        return awemes;
    }

    public AwemeAction setBeforeAction(Runnable runnable) {
        this.beforeAction = runnable;
        return this;
    }

    public AwemeAction setBeginAction(Runnable runnable) {
        this.beginAction = runnable;
        return this;
    }

    public AwemeAction setFinishAction(Runnable runnable) {
        this.finishAction = runnable;
        return this;
    }

    public AwemeAction setonErrorAction(Consumer<Integer> runnable) {
        this.onErrorAction = runnable;
        return this;
    }
}
