package com.qhg.bk;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Shell {
    static String app_id = "ljwxapp:";
    static String app_key = "6e8566e348447383e16fdd1b233dbb49";

    public static void main(String[] args) {
        String url = "https://wxapp.api.ke.com/openapi/ershouc/xcx/xiaoqu/detail/part1?id=3811061645782&sign=";
        System.out.println(getAuthorization(url));
    }

    public static String getAuthorization(String url) {
        HashMap<String, String> map = url2Map(url);
        Set<String> strings = map.keySet();
        List<String> sortKeys = strings.stream().sorted().collect(Collectors.toList());
        StringBuilder s = new StringBuilder();
        for (String key : sortKeys) {
            s.append(key).append("=").append(map.get(key));
        }
        s.append(app_key);
        String s1 = MD5.create().digestHex(s.toString());
        return Base64.encode(app_id + s1);
    }

    private static HashMap<String, String> url2Map(String url) {
        String of = url.split("\\?")[1];
        HashMap<String, String> map = new HashMap<>();
        String[] split = of.split("&");
        for (String s : split) {
            String[] strings = s.split("=");
            if (strings.length == 2)
                map.put(strings[0], strings[1]);
            else
                map.put(strings[0], "");
        }
        return map;
    }
}
