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
                                    "passport_csrf_token=49617b052a4df029314bc4362b058ff9; passport_csrf_token_default=49617b052a4df029314bc4362b058ff9; ttcid=7fd2fe6c151f4981b1b987a073fd111555; ttwid=1%7Cft2fcPizD0IsQJlWd2X7bAP-g2Kf37-OddUoHoUGt48%7C1676991527%7C44c9ea18c70545742deb9d1426c78d9419732a92a23da9aa80b2a6af0fd9b830; __ac_signature=_02B4Z6wo00f01CB3FPwAAIDBMCnxx749EUAgVxBAAGvvef; strategyABtestKey=%221677027778.322%22; s_v_web_id=verify_leez1fq7_rfS58zSx_Capm_4xGk_93hs_uUOzvVTqVgt2; n_mh=i2a1GWUt2fNfatSZl3luM-aVn3l5TOs2nf2JV1Pe-1o; sso_uid_tt=e898330ba626e82360a9e7e207135476; sso_uid_tt_ss=e898330ba626e82360a9e7e207135476; toutiao_sso_user=1b85517a89caed11528e09cc479bba69; toutiao_sso_user_ss=1b85517a89caed11528e09cc479bba69; passport_assist_user=CkFpKNroaM5ZttQbAtOeeGoerCXnmAnMNqlghIctfnNb8uzhWOjMOP-477GRZK9e15A2G79-p68vwkIrOly-ThfC4RpICjxHRgl0NLK20t_a2FTGi3nKLEHi9R78SVpc14QMTxW7yUDdRO93UTKzPqVkVLAWCAJRKBiekWEayenD5OsQ3PipDRiJr9ZUIgED6ED7Sg%3D%3D; sid_ucp_sso_v1=1.0.0-KDFmZGJhNTNjNTNkOThlZDhkMWQzZTY3OWU4N2QxYTI5ZjI2NmI1NmUKHwjw8-DSh_TTAxDY09WfBhjvMSAMMPj2nvgFOAZA9AcaAmxmIiAxYjg1NTE3YTg5Y2FlZDExNTI4ZTA5Y2M0NzliYmE2OQ; ssid_ucp_sso_v1=1.0.0-KDFmZGJhNTNjNTNkOThlZDhkMWQzZTY3OWU4N2QxYTI5ZjI2NmI1NmUKHwjw8-DSh_TTAxDY09WfBhjvMSAMMPj2nvgFOAZA9AcaAmxmIiAxYjg1NTE3YTg5Y2FlZDExNTI4ZTA5Y2M0NzliYmE2OQ; odin_tt=108883a2cf395fc2ecb137122b3db222d76b2559704750325ba0947d12de43e793ee5bc1b1cc8b98062c54edfa33191e60200312762acd91fba1878aafa01e1b; passport_auth_status=bcc3fb6e97bd8d9476fec19f4331caa8%2C; passport_auth_status_ss=bcc3fb6e97bd8d9476fec19f4331caa8%2C; uid_tt=3df51560e1ab8ddfbbdc36e86779d98d; uid_tt_ss=3df51560e1ab8ddfbbdc36e86779d98d; sid_tt=981ed05215fb520611a0fa3986a8cf74; sessionid=981ed05215fb520611a0fa3986a8cf74; sessionid_ss=981ed05215fb520611a0fa3986a8cf74; LOGIN_STATUS=1; bd_ticket_guard_server_data=; store-region=cn-sn; store-region-src=uid; sid_guard=981ed05215fb520611a0fa3986a8cf74%7C1677027804%7C5183996%7CSun%2C+23-Apr-2023+01%3A03%3A20+GMT; sid_ucp_v1=1.0.0-KDQ5YTE5ZjMyNjMyNDk1MDMzMGFhYTdmNWY2MDI0OGZlZDAzNWRjOTIKGwjw8-DSh_TTAxDc09WfBhjvMSAMOAZA9AdIBBoCbGYiIDk4MWVkMDUyMTVmYjUyMDYxMWEwZmEzOTg2YThjZjc0; ssid_ucp_v1=1.0.0-KDQ5YTE5ZjMyNjMyNDk1MDMzMGFhYTdmNWY2MDI0OGZlZDAzNWRjOTIKGwjw8-DSh_TTAxDc09WfBhjvMSAMOAZA9AdIBBoCbGYiIDk4MWVkMDUyMTVmYjUyMDYxMWEwZmEzOTg2YThjZjc0; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWNsaWVudC1jZXJ0IjoiLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tXG5NSUlDRlRDQ0FidWdBd0lCQWdJVkFOenFhUUhHYU9zMlFNUkp0eGRGLytZN3UvaGhNQW9HQ0NxR1NNNDlCQU1DXG5NREV4Q3pBSkJnTlZCQVlUQWtOT01TSXdJQVlEVlFRRERCbDBhV05yWlhSZlozVmhjbVJmWTJGZlpXTmtjMkZmXG5NalUyTUI0WERUSXpNREl5TWpBeE1ETXlNRm9YRFRNek1ESXlNakE1TURNeU1Gb3dKekVMTUFrR0ExVUVCaE1DXG5RMDR4R0RBV0JnTlZCQU1NRDJKa1gzUnBZMnRsZEY5bmRXRnlaREJaTUJNR0J5cUdTTTQ5QWdFR0NDcUdTTTQ5XG5Bd0VIQTBJQUJFOFErRG5UaG1FWVJWaTdMRlVWbnB6R2I5TmhxYU43d2dFNGNlLzBoMWNkM3NZcENneDdjSDc5XG5mM2wzNFJhalJHZ3A4dEkxSGxaaEpYT2Y4dm1Sa0cyamdia3dnYll3RGdZRFZSMFBBUUgvQkFRREFnV2dNREVHXG5BMVVkSlFRcU1DZ0dDQ3NHQVFVRkJ3TUJCZ2dyQmdFRkJRY0RBZ1lJS3dZQkJRVUhBd01HQ0NzR0FRVUZCd01FXG5NQ2tHQTFVZERnUWlCQ0ROcEU0RlI3S1AvNFVSUUpkZVlsUUxuRzh1Q0JMYTBzSjhTeUoyZlhRemRUQXJCZ05WXG5IU01FSkRBaWdDQXlwV2Zxam1SSUVvM01UazFBZTNNVW0wZHRVM3FrMFlEWGVaU1hleUpIZ3pBWkJnTlZIUkVFXG5FakFRZ2c1M2QzY3VaRzkxZVdsdUxtTnZiVEFLQmdncWhrak9QUVFEQWdOSUFEQkZBaUVBb21UbjhWNU05STBwXG4zMEdNMVYvWlpxTU9lOHN3UUZiSE9YZjM4ZVZqdCt3Q0lGSU1FdklYeUVlNkxFRHM3bTlibkZMRGVEZnpFNm5mXG5laitSTDFGZmxtUTRcbi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS1cbiJ9; douyin.com; csrf_session_id=e6309f0810b3fe47a1d1f91d9e8aeb2e; tt_scid=8NDGMLU-srDFGAOu7uwk4sICVIjRhqkt9nyXA8ORFTBHbClefomlMV7VkmgpbdlM0ef8; download_guide=%223%2F20230222%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1677081600000%2F0%2F0%2F1677030571202%22; __ac_nonce=063f5723200bf728c50c1; VIDEO_FILTER_MEMO_SELECT=%7B%22expireTime%22%3A1677635793528%2C%22type%22%3A1%7D; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAA-z6zy0WFOuhJm6hMKTTHEGGnZWlMAhPI7YX8GQofv9p7Tn2J4mwKY1rZhVtf-k7m%2F1677081600000%2F0%2F1677030993816%2F0%22; passport_fe_beating_status=true; msToken=G9HfuWc911J528vI0Ppv7X0jmrgtidsHe_woFaqIU79GMypc_i-A2nACDhE-a437lADZSKgjNkknAcvTQsx6YWkaX1UyJ4cW7Io4iepeZNngQ04h6DUMEZfMi1N-iA==; home_can_add_dy_2_desktop=%221%22; msToken=56r0cnmUzNS7DKUDQKPAHdPuRkD8kPkFOjU1Au-M8KQ0kA60E0ylU4WORjs60IlxO-wrVjIlbDA3S2AL7nPhlBWctSvb4XP_Zkq1HxbQwZ3qKSPdbdtK6dmJIf5FeA=="
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
                            .addUrlPara("round_trip_time", "250")
                            .addUrlPara("webid", "7202107445362640387");
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
