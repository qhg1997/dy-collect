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
"passport_csrf_token=92817afe71510f557af8e6695b30c654; passport_csrf_token_default=92817afe71510f557af8e6695b30c654; s_v_web_id=verify_leph39jp_lajml87X_s7z0_4FGy_8zw0_eiS87tTmlBEO; d_ticket=e77a6e6d9c261b154307d4367c79f829afa97; passport_assist_user=CkGuHmnfmLQRFiam-SrdHpk6MAk4kTqG0mv87lcl_ql69gwaJgfmvOdilkt0Wxs3fXk2Tz8r_IUfzj_PyraCvJMDzxpICjx8g16g9u2bkkimPMkc-h1PJ24K5qfyUDTu9fnMt6nUP0ZkekssG0DYuL3tDNoHM5n2sicb0RzUESPveqUQgs2qDRiJr9ZUIgEDs8H3Qg%3D%3D; n_mh=i2a1GWUt2fNfatSZl3luM-aVn3l5TOs2nf2JV1Pe-1o; sso_auth_status=2ee08043676d312605159ea574ddc4b8; sso_auth_status_ss=2ee08043676d312605159ea574ddc4b8; sso_uid_tt=bd69d49aa7f7af3ae14d8f73e6a065bc; sso_uid_tt_ss=bd69d49aa7f7af3ae14d8f73e6a065bc; toutiao_sso_user=86c4ec583bc238714da749059d75f544; toutiao_sso_user_ss=86c4ec583bc238714da749059d75f544; sid_ucp_sso_v1=1.0.0-KGJmNDRhZjk5M2JkYzNiMmQ1ZTk5NzNjYmNmZTExM2Y4NzNjOGI1YmYKHwjw8-DSh_TTAxDHtPyfBhjvMSAMMPj2nvgFOAJA8QcaAmxmIiA4NmM0ZWM1ODNiYzIzODcxNGRhNzQ5MDU5ZDc1ZjU0NA; ssid_ucp_sso_v1=1.0.0-KGJmNDRhZjk5M2JkYzNiMmQ1ZTk5NzNjYmNmZTExM2Y4NzNjOGI1YmYKHwjw8-DSh_TTAxDHtPyfBhjvMSAMMPj2nvgFOAJA8QcaAmxmIiA4NmM0ZWM1ODNiYzIzODcxNGRhNzQ5MDU5ZDc1ZjU0NA; passport_auth_status=16098dd3cd86f90305904d3a57897ce4%2C11d1d0648cc98de22b10ec684c45e2f4; passport_auth_status_ss=16098dd3cd86f90305904d3a57897ce4%2C11d1d0648cc98de22b10ec684c45e2f4; uid_tt=39e94485f5c3ce11a7c7de852013cad7; uid_tt_ss=39e94485f5c3ce11a7c7de852013cad7; sid_tt=b1c4229f3277cb6b980307303ec8d733; sessionid=b1c4229f3277cb6b980307303ec8d733; sessionid_ss=b1c4229f3277cb6b980307303ec8d733; LOGIN_STATUS=1; store-region=cn-sn; store-region-src=uid; odin_tt=4bc732a9465ce7d633b082d355b04d4be3e7dd02e18b46ee554d48419e6f0cf5b9b2b0df597c732a39607372a7a0e2230db137d6eef61d830bac3d59d08d2f34; sid_guard=b1c4229f3277cb6b980307303ec8d733%7C1678354659%7C5184000%7CMon%2C+08-May-2023+09%3A37%3A39+GMT; sid_ucp_v1=1.0.0-KDllMzNlODcyYTlkZWZhOTE1MWNjNDAyMjg4YTgxZmI5ODFjNTJiMWMKGwjw8-DSh_TTAxDj0aagBhjvMSAMOAJA8QdIBBoCbGYiIGIxYzQyMjlmMzI3N2NiNmI5ODAzMDczMDNlYzhkNzMz; ssid_ucp_v1=1.0.0-KDllMzNlODcyYTlkZWZhOTE1MWNjNDAyMjg4YTgxZmI5ODFjNTJiMWMKGwjw8-DSh_TTAxDj0aagBhjvMSAMOAJA8QdIBBoCbGYiIGIxYzQyMjlmMzI3N2NiNmI5ODAzMDczMDNlYzhkNzMz; SEARCH_RESULT_LIST_TYPE=%22single%22; download_guide=%223%2F20230317%22; publish_badge_show_info=%220%2C0%2C0%2C1679117978639%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1679155200000%2F0%2F1679118246245%2F0%22; ttwid=1%7Cr5HwlYQPwzHrt_x08LcP9hmdE97Js3ZbiDYSh2yFGyM%7C1679118362%7C7b4766d6efd72a1cc840ce3cbb7dd768033f0614cee67c4851a94bff7f6184a3; VIDEO_FILTER_MEMO_SELECT=%7B%22expireTime%22%3A1679980222670%2C%22type%22%3A1%7D; msToken=P1NvUtACGGBe6FM7fvvYs-Yl5SGtRSPE_RVx5N8kLOGs10oDcfE7QjxkNGZ38iybh6Y-I0KFj0I1b_oYSDEQBqkL6F2TON0ioLTLIKAwcfzgwI5fKTEBik-vqq_ypXQ=; __ac_nonce=0641a943600456290eb3d; __ac_signature=_02B4Z6wo00f01pWA9EAAAIDDhd4RecH6w0KVoPDAAMGGJVGYTU3wlRRUQlr-WTFTRrBKVZnX93FiXJUiRNpePkVVGs2wqNpBUa2Imk-r7EPpMchOvCr9HGprvmSPvlhQkWECQ-vqhxb6Vp8q26; _tea_utm_cache_2018=undefined; strategyABtestKey=%221679464205.639%22; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1679500800000%2F0%2F1679464206209%2F0%22; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWNsaWVudC1jZXJ0IjoiLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tXG5NSUlDRXpDQ0FicWdBd0lCQWdJVVNQalVmUmdNUXJDeXJ4aEpGMGhCYTYzMCtmVXdDZ1lJS29aSXpqMEVBd0l3XG5NVEVMTUFrR0ExVUVCaE1DUTA0eElqQWdCZ05WQkFNTUdYUnBZMnRsZEY5bmRXRnlaRjlqWVY5bFkyUnpZVjh5XG5OVFl3SGhjTk1qTXdNekF4TURreU5qTXhXaGNOTXpNd016QXhNVGN5TmpNeFdqQW5NUXN3Q1FZRFZRUUdFd0pEXG5UakVZTUJZR0ExVUVBd3dQWW1SZmRHbGphMlYwWDJkMVlYSmtNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEXG5BUWNEUWdBRU1TaUVKZnlrMnlYZjFhbncwdEd6L0JOWnhYUUtEcW9teEk2Z1c0cmJReWhxYnNWUzJNTW1IK0R6XG5TK0lUVnhhWWFLNDEzNTFWNmd4WWp5NEI0a0NUMDZPQnVUQ0J0akFPQmdOVkhROEJBZjhFQkFNQ0JhQXdNUVlEXG5WUjBsQkNvd0tBWUlLd1lCQlFVSEF3RUdDQ3NHQVFVRkJ3TUNCZ2dyQmdFRkJRY0RBd1lJS3dZQkJRVUhBd1F3XG5LUVlEVlIwT0JDSUVJS3lQRmJyMlVWWFRNblA3VzUvQ1VpZk5PL1F3NGllWUM5cEZva3VpT2VkN01Dc0dBMVVkXG5Jd1FrTUNLQUlES2xaK3FPWkVnU2pjeE9UVUI3Y3hTYlIyMVRlcVRSZ05kNWxKZDdJa2VETUJrR0ExVWRFUVFTXG5NQkNDRG5kM2R5NWtiM1Y1YVc0dVkyOXRNQW9HQ0NxR1NNNDlCQU1DQTBjQU1FUUNJQVdMcFgzR0VlZFJwcUU1XG5tSkh4REt0dzJxeVlmMnl1djZpQlRDRGZYY3VhQWlBRDAraDlNTEJ1TG1ZOEY1TXlUY0xsbFZjUzN3dU9keG5SXG5xVXVEVThPY1Z3PT1cbi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS1cbiJ9; msToken=-0t825neHFcrt-MtBDY7NLvs_-0ykTQ5rNV0CjE6OCrZ_fAetbCgC1u2vD6T8jrnsLkHTUYGeo9YhJw9NaPe-cDDi-cDLDxsvpA-R7_uv1njJAB760wXgQZZ1rlYoOU=; tt_scid=Pzu.2d8WERwYUhZG9xuvp40EMqliXArTbUPH7PN-NKjaSKA5w1Y2H7jAoqdeflP9fee5; passport_fe_beating_status=false; home_can_add_dy_2_desktop=%220%22"
                            )
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
                                if (data != null ) {
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
                JSONObject object = JSONObject.parseObject(body.toString());
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
