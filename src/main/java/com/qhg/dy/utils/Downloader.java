package com.qhg.dy.utils;

import cn.hutool.script.ScriptUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.AwemeResource;
import lombok.Data;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Downloader {

    public static File baseFolder = new File("D:\\dyDowned");

    static {
        if (!baseFolder.exists()) {
            boolean mkdirs = baseFolder.mkdirs();
            System.out.println("初始化操作; 基础文件夹创建:" + (mkdirs ? "成功" : "失败"));
        }
    }


    private final List<AwemeResource> resource;
    private final Aweme aweme;
    private static final HTTP http = HTTP.builder()
            .config(b ->
                    b.addInterceptor(new IInterceptor())
                            .connectTimeout(1, TimeUnit.MINUTES)
                            .readTimeout(1, TimeUnit.MINUTES)
                            .writeTimeout(1, TimeUnit.MINUTES)
            )
            .addMsgConvertor(new FastjsonMsgConvertor())
            .build();
    private static final HTTP http0 = HTTP.builder()
            .config(b ->
                    b.addInterceptor(new IInterceptor())
                            .connectTimeout(1, TimeUnit.MINUTES)
                            .readTimeout(1, TimeUnit.MINUTES)
                            .writeTimeout(1, TimeUnit.MINUTES)
                            .followRedirects(false))
            .addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    private Consumer<AwemeResource> finishAction;
    private Consumer<Aweme> completeAction;

    public Downloader(Aweme aweme) {
        this.aweme = aweme;
        this.resource = aweme.getResources();
    }

    public boolean download() {
        for (AwemeResource awemeResource : resource) {
            File file;
            if (awemeResource.getType() == 1) {
                file = new File(baseFolder, awemeResource.getAuthorName() + "\\image\\" + awemeResource.getTitle() + "\\" + awemeResource.getSafeFileName());
                if (file.exists()) {
                    continue;
                }
                if (!file.getParentFile().exists()) {
                    boolean mkdirs = file.getParentFile().mkdirs();
                    System.out.println(file.getParent() + " 文件夹创建: " + (mkdirs ? "成功" : "失败"));
                }

                http.sync(awemeResource.getUri())
                        .get()                           // 使用 GET 方法（其它方法也可以，看服务器支持）
                        .getBody()                       // 得到报文体
                        .toFile(file)  // 下载到指定的文件路径
                        .start();                        // 启动下载

            } else {
                file = new File(baseFolder, awemeResource.getAuthorName() + "\\video\\" + awemeResource.getSafeFileName());
                if (file.exists()) {
                    continue;
                }
                if (!file.getParentFile().exists()) {
                    boolean mkdirs = file.getParentFile().mkdirs();
                    System.out.println(file.getParent() + " 文件夹创建: " + (mkdirs ? "成功" : "失败"));
                }

                String location = http0.async(awemeResource.getUri())
                        .get().getResult()
                        .getHeader("location");
                http.sync(location)
                        .get()                           // 使用 GET 方法（其它方法也可以，看服务器支持）
                        .getBody()                       // 得到报文体
                        .toFile(file)  // 下载到指定的文件路径
                        .start();                        // 启动下载

            }
            System.out.println(file.getAbsolutePath() + " 文件下载: " + (file.exists() ? "成功" : "失败"));
            Optional.ofNullable(finishAction).ifPresent(fun -> fun.accept(awemeResource));
        }
        Optional.ofNullable(completeAction).ifPresent(fun -> fun.accept(this.aweme));
        return true;
    }

}
