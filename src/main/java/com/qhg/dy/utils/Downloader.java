package com.qhg.dy.utils;

import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.model.Aweme;
import com.qhg.dy.model.AwemeResource;
import lombok.Data;
import okhttp3.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Data
public class Downloader {

    public static File baseFolder = new File("D:\\好用的软件\\HTTP Debugger Pro  注册机\\dyDowned");
    Integer mode = 1;

    public ArrayBlockingQueue<AwemeResource> arrayBlockingQueue = new ArrayBlockingQueue<>(50000);

    static {
        if (!baseFolder.exists()) {
            boolean mkdirs = baseFolder.mkdirs();
            System.out.println("初始化操作; 基础文件夹创建:" + (mkdirs ? "成功" : "失败"));
        }
    }


    private final List<AwemeResource> resource;
    private final HTTP http = HTTP.builder()
            .config(b ->
                    b.addInterceptor(new IInterceptor())
                            .connectTimeout(10, TimeUnit.MINUTES)
                            .readTimeout(10, TimeUnit.MINUTES)
                            .writeTimeout(10, TimeUnit.MINUTES)
                            .connectionPool(new ConnectionPool(100, 5, TimeUnit.MINUTES))
            )
            .addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    private final HTTP http0 = HTTP.builder()
            .config(b ->
                    b.addInterceptor(new IInterceptor())
                            .connectTimeout(10, TimeUnit.MINUTES)
                            .readTimeout(10, TimeUnit.MINUTES)
                            .writeTimeout(10, TimeUnit.MINUTES)
                            .connectionPool(new ConnectionPool(100, 5, TimeUnit.MINUTES))
                            .followRedirects(false)
            )
            .addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    private Consumer<AwemeResource> finishAction;
    private Consumer<AwemeResource> failAction;

    public Downloader(List<AwemeResource> resources) {
        this.resource = resources;
    }

    public void download() throws InterruptedException {
        resource.forEach(awemeResource -> {
            File file;
            if (awemeResource.getType() == 1) {
                if (mode == 0)
                    file = new File(baseFolder, awemeResource.getAuthorName() + "\\image\\" + awemeResource.getTitle() + "\\" + awemeResource.getSafeFileName());
                else
                    file = new File(baseFolder, "image\\" + awemeResource.getAuthorName() + "\\" + awemeResource.getTitle() + "\\" + awemeResource.getSafeFileName());
                if (file.exists()) {
                    System.out.println(file.getAbsolutePath() + " : 文件存在");
                    Optional.ofNullable(finishAction).ifPresent(fun -> fun.accept(awemeResource));
                    return;
                }
                if (!file.getParentFile().exists()) {
                    boolean mkdirs = file.getParentFile().mkdirs();
                    System.out.println(file.getParent() + " 文件夹创建: " + (mkdirs ? "成功" : "失败"));
                }

                HttpResult.Body body = http.sync(awemeResource.getUri())
                        .get()                           // 使用 GET 方法（其它方法也可以，看服务器支持）
                        .getBody(); // 得到报文体
                body.toFile(file)// 下载到指定的文件路径
                        .setOnComplete(status -> body.close())
                        .setOnSuccess(f -> {
                            Optional.ofNullable(finishAction).ifPresent(fun -> fun.accept(awemeResource));
                            System.out.println(f.getAbsolutePath() + " 文件下载: " + (f.exists() ? "成功" : "失败"));
                        }).setOnFailure(failure -> {
                    failure.getException().printStackTrace();
                    System.err.println(failure.getFile().getAbsolutePath() + " 文件下载: 失败");
                })
                        .start();// 启动下载

            } else {
                if (mode == 0)
                    file = new File(baseFolder, awemeResource.getAuthorName() + "\\video\\" + awemeResource.getId() + "_" + awemeResource.getSafeFileName());
                else
                    file = new File(baseFolder, "video\\" + awemeResource.getAuthorName() + "\\" + awemeResource.getId() + "_" + awemeResource.getSafeFileName());

                if (file.exists()) {
                    Optional.ofNullable(finishAction).ifPresent(fun -> fun.accept(awemeResource));
                    System.err.println("已存在： " + file.getAbsolutePath());
                    return;
                }
                if (!file.getParentFile().exists()) {
                    boolean mkdirs = file.getParentFile().mkdirs();
                    System.out.println(file.getParent() + " 文件夹创建: " + (mkdirs ? "成功" : "失败"));
                }

                try {
                    System.err.println(awemeResource.getUri());
                    HttpResult.Body body = http.sync(awemeResource.getUri())
                            .get()                           // 使用 GET 方法（其它方法也可以，看服务器支持）
                            .getBody(); // 得到报文体
                    Download.Ctrl ctrl = body.toFile(file)// 下载到指定的文件路径
                            .setOnComplete(status -> {
                                body.close();
                            })
                            .setOnSuccess(f -> {
                                Optional.ofNullable(finishAction).ifPresent(fun -> fun.accept(awemeResource));
                                System.out.println(Thread.currentThread().getName() + " : " + f.getAbsolutePath() + " 文件下载: " + (f.exists() ? "成功" : "失败"));
                            })
                            .setOnFailure(failure -> {
                                failure.getException().printStackTrace();
                                System.err.println(failure.getFile().getAbsolutePath() + " 文件下载: 失败");
                            }).start();// 启动下载
//                        while (true) {
//                            if (ctrl.status().equals(Download.Status.DONE)) {
//                                break;
//                            }
//                        }
                } catch (Exception e) {
                    Optional.ofNullable(failAction).ifPresent(fun -> fun.accept(awemeResource));
                }
            }
        });
    }


}
