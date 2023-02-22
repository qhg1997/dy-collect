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

    public AwemeAction(String secUid) {
        this.secUid = secUid;
        http = HTTP.builder()
                .config(b -> b.addInterceptor(new Interceptor() {
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
                                .writeTimeout(1, TimeUnit.MINUTES)

//                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("n550.kdltps.com", 15818)))
                )
                .baseUrl("https://www.douyin.com")
                .addPreprocessor(p -> {
                    HttpTask<?> httpTask = p.getTask()

                            .addHeader("referer", "https://www.douyin.com/")
                            .addHeader("Cookie", "" +
                            "passport_csrf_token=49617b052a4df029314bc4362b058ff9; passport_csrf_token_default=49617b052a4df029314bc4362b058ff9; ttcid=7fd2fe6c151f4981b1b987a073fd111555; ttwid=1|ft2fcPizD0IsQJlWd2X7bAP-g2Kf37-OddUoHoUGt48|1676991527|44c9ea18c70545742deb9d1426c78d9419732a92a23da9aa80b2a6af0fd9b830; strategyABtestKey=\"1677027778.322\"; s_v_web_id=verify_leez1fq7_rfS58zSx_Capm_4xGk_93hs_uUOzvVTqVgt2; n_mh=i2a1GWUt2fNfatSZl3luM-aVn3l5TOs2nf2JV1Pe-1o; store-region=cn-sn; store-region-src=uid; download_guide=\"3/20230222\"; FOLLOW_RED_POINT_INFO=\"1\"; SEARCH_RESULT_LIST_TYPE=\"single\"; live_can_add_dy_2_desktop=\"0\"; my_rd=1; sso_uid_tt=6d6195bace92562f31d12185e51d7e28; sso_uid_tt_ss=6d6195bace92562f31d12185e51d7e28; toutiao_sso_user=e780fc0ff2196f1aeb855e6fb346817f; toutiao_sso_user_ss=e780fc0ff2196f1aeb855e6fb346817f; passport_auth_status=279d91e20ead38ee70bced1ee65cdc03,bcc3fb6e97bd8d9476fec19f4331caa8; passport_auth_status_ss=279d91e20ead38ee70bced1ee65cdc03,bcc3fb6e97bd8d9476fec19f4331caa8; uid_tt=9d5969b631820462cdba15307f326a61; uid_tt_ss=9d5969b631820462cdba15307f326a61; sid_tt=6bc3cc5242c5e992733ea9aaafc59ad9; sessionid=6bc3cc5242c5e992733ea9aaafc59ad9; sessionid_ss=6bc3cc5242c5e992733ea9aaafc59ad9; odin_tt=168938720a787e34831d691cb2443270a29b55ddec5ef3e07ff78b683acff62d50e77b3817a51a4592c424b13744c71a8b2c3cc1e379333e7cd6f1ce37e37d1e; passport_assist_user=CkGky2QNNkAovsrsb_fqUD8kbXb4NQ83vEDWS1-SexCLkoQNaG2-ghWESHWTpQk6b1gjH1vRlJKbS2JlMVMFv42IKRpICjwK1EoQRO1Eymq84JgW4FWXIrJFTHB1VFXc0L-VinLdkKi8UTZ9vyynxVLbtwsdADKMQEVQph6ZSqmO9XwQt_upDRiJr9ZUIgEDgmonHA==; sid_ucp_sso_v1=1.0.0-KDdjYTFhYjU0MTYxMDE0NzgwNjc3ZTA0MzE2MDI2OGI4OTQwOTA3ZDIKHwjw8-DSh_TTAxCN59afBhjvMSAMMPj2nvgFOAZA9AcaAmxmIiBlNzgwZmMwZmYyMTk2ZjFhZWI4NTVlNmZiMzQ2ODE3Zg; ssid_ucp_sso_v1=1.0.0-KDdjYTFhYjU0MTYxMDE0NzgwNjc3ZTA0MzE2MDI2OGI4OTQwOTA3ZDIKHwjw8-DSh_TTAxCN59afBhjvMSAMMPj2nvgFOAZA9AcaAmxmIiBlNzgwZmMwZmYyMTk2ZjFhZWI4NTVlNmZiMzQ2ODE3Zg; LOGIN_STATUS=1; sid_guard=6bc3cc5242c5e992733ea9aaafc59ad9|1677046673|5183994|Sun,+23-Apr-2023+06:17:47+GMT; sid_ucp_v1=1.0.0-KDg2NWFhNWZkODBlZWRhMDU5NTc0ZGRmOTM3ZGI5ZjQ4Yzc0YWI1NDcKGwjw8-DSh_TTAxCR59afBhjvMSAMOAZA9AdIBBoCaGwiIDZiYzNjYzUyNDJjNWU5OTI3MzNlYTlhYWFmYzU5YWQ5; ssid_ucp_v1=1.0.0-KDg2NWFhNWZkODBlZWRhMDU5NTc0ZGRmOTM3ZGI5ZjQ4Yzc0YWI1NDcKGwjw8-DSh_TTAxCR59afBhjvMSAMOAZA9AdIBBoCaGwiIDZiYzNjYzUyNDJjNWU5OTI3MzNlYTlhYWFmYzU5YWQ5; FOLLOW_NUMBER_YELLOW_POINT_INFO=\"MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m/1677081600000/1677047583547/1677047416180/0\"; douyin.com; VIDEO_FILTER_MEMO_SELECT={\"expireTime\":1677652721503,\"type\":1}; csrf_session_id=47e1faa641aff7941216ad551a560f38; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWNsaWVudC1jZXJ0IjoiLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tXG5NSUlDRlRDQ0FidWdBd0lCQWdJVkFOenFhUUhHYU9zMlFNUkp0eGRGLytZN3UvaGhNQW9HQ0NxR1NNNDlCQU1DXG5NREV4Q3pBSkJnTlZCQVlUQWtOT01TSXdJQVlEVlFRRERCbDBhV05yWlhSZlozVmhjbVJmWTJGZlpXTmtjMkZmXG5NalUyTUI0WERUSXpNREl5TWpBeE1ETXlNRm9YRFRNek1ESXlNakE1TURNeU1Gb3dKekVMTUFrR0ExVUVCaE1DXG5RMDR4R0RBV0JnTlZCQU1NRDJKa1gzUnBZMnRsZEY5bmRXRnlaREJaTUJNR0J5cUdTTTQ5QWdFR0NDcUdTTTQ5XG5Bd0VIQTBJQUJFOFErRG5UaG1FWVJWaTdMRlVWbnB6R2I5TmhxYU43d2dFNGNlLzBoMWNkM3NZcENneDdjSDc5XG5mM2wzNFJhalJHZ3A4dEkxSGxaaEpYT2Y4dm1Sa0cyamdia3dnYll3RGdZRFZSMFBBUUgvQkFRREFnV2dNREVHXG5BMVVkSlFRcU1DZ0dDQ3NHQVFVRkJ3TUJCZ2dyQmdFRkJRY0RBZ1lJS3dZQkJRVUhBd01HQ0NzR0FRVUZCd01FXG5NQ2tHQTFVZERnUWlCQ0ROcEU0RlI3S1AvNFVSUUpkZVlsUUxuRzh1Q0JMYTBzSjhTeUoyZlhRemRUQXJCZ05WXG5IU01FSkRBaWdDQXlwV2Zxam1SSUVvM01UazFBZTNNVW0wZHRVM3FrMFlEWGVaU1hleUpIZ3pBWkJnTlZIUkVFXG5FakFRZ2c1M2QzY3VaRzkxZVdsdUxtTnZiVEFLQmdncWhrak9QUVFEQWdOSUFEQkZBaUVBb21UbjhWNU05STBwXG4zMEdNMVYvWlpxTU9lOHN3UUZiSE9YZjM4ZVZqdCt3Q0lGSU1FdklYeUVlNkxFRHM3bTlibkZMRGVEZnpFNm5mXG5laitSTDFGZmxtUTRcbi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS1cbiJ9; passport_fe_beating_status=true; msToken=Qry7U8kainGtKpJ945Rp0idYB90PPVb15Ql9eVxBnz1ZIzkSZkRPGrG-VysI4PeFBACnSM2hGlwKaU5aVAeo9AjoU37r3kO4v6xsMK3b7PrPQynb9ek--bzPY0WjDd4=; __ac_nonce=063f5b8e9002b93a45cb4; __ac_signature=_02B4Z6wo00f01JqRXHwAAIDBis-5RwO6bpSasVjAAEVWI8nbKpzNOiV8JM3Kwqiy19rCqZ9Nycqx4iup9f9EDMN29pQ2e-aBvzW.LOiDsIujvHGKT-CBfMXCt3Vcrsd-0sC3ND-E829n8FUs76; FOLLOW_LIVE_POINT_INFO=\"MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m/1677081600000/0/1677048044519/0\"; home_can_add_dy_2_desktop=\"1\"; msToken=DdssctqyIkoXmgE9sHzpkMXaw99WOmkV-039sRkH8K8ML2FGDT7tOIm9kL6RhqfetYjyFA7yrngneCv9tysMBXKSuUiFgc5kRFOES45RcMMctfHwfHQcV8APtUMp8zo=; tt_scid=K7LEkJWPa7N0FY56GxNXVIdcmU0.9HBC-URqal2AjeOngrRPYgzuWnz6jTIaJuvs7deb"
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
                                HttpResult.Body body = http0.async("http://47.115.200.238/xg/path")
                                        .addUrlPara("url", url)
                                        .post()
                                        .getResult()
                                        .getBody()
                                        .cache();
                                JSONObject data = JSONObject.parseObject(body.toString());
                                if (data != null && "200".equals(data.getString("status_code"))) {
                                    JSONObject result = data.getJSONArray("result").getJSONObject(0);
                                    JSONObject params = result.getJSONObject("params");
                                    for (String key : params.keySet()) {
                                        if (key.equals("X-Bogus")) {
                                            httpTask.addUrlPara(key, params.getString(key));
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

    public static void main(String[] args) {
        try {
            List<Aweme> awemeList = new AwemeAction("MS4wLjABAAAAigFjTEOAwKibGXwx9X5mWfu1uOUJWfeoHpvaXqzzRc0")
                    .getAllAwemes((list) -> {
                        //save;


                        return null;
                    });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Aweme> getAllAwemes() throws InterruptedException {
        return getAllAwemes(null);
    }

    public List<Aweme> getAllAwemes(Function<List<Aweme>, Integer> function) throws InterruptedException {
        if (!beforeActionStatus)
            Optional.ofNullable(beforeAction).ifPresent(Runnable::run);
        List<Aweme> awemes = new ArrayList<>();
        String max_cursor = "0";
        while (true) {
            try {
                SHttpTask httpTask = http.sync("/aweme/v1/web/aweme/post/")
                        .addUrlPara("sec_user_id", secUid)
                        .addUrlPara("max_cursor", max_cursor);
                HttpResult.Body body = httpTask
                        .get().getBody().cache();
                JSONObject object = JSONObject.parseObject(body.toString());
                if (object == null) {
                    System.out.println("https://www.douyin.com/user/" + secUid);
//                    System.err.println(httpTask.getUrl() + "?" + httpTask.getUrlParas().entrySet().stream().map(Objects::toString).collect(Collectors.joining("&")));
                    TimeUnit.SECONDS.sleep(5);
                    continue;
//                return awemes;
                }
                JSONArray awemeList = object.getJSONArray("aweme_list");
                if (awemeList != null && awemeList.size() == 0 && onErrorAction != null) {
                    onErrorAction.accept(0);
                    return awemes;
                }
                if (awemeList != null && awemeList.size() > 0) {
                    if (!beginActionStatus)
                        Optional.ofNullable(beforeAction).ifPresent(Runnable::run);
                    List<Aweme> list = Aweme.list(awemeList, (a) -> a.setSecUid(secUid));
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
