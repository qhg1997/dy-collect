package com.qhg.sgp;

import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.utils.IInterceptor;
import com.qhg.dy.utils.IO;
import com.qhg.sgp.model.*;
import com.qhg.sgp.repo.*;
import org.apache.commons.io.FileUtils;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.ExpressionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
class PaiDownloadTests {
    @Test
    void fastCollectNews() {
    }

    @Resource
    PaiRepository paiRepository;
    @Resource
    TagRepository tagRepository;
    @Resource
    PaiFilmRepository paiFilmRepository;
    @Resource
    ActorRepository actorRepository;
    @Resource
    AnchorsRepository anchorsRepository;
    @Resource
    PaiMovieRepository paiMovieRepository;
    @Resource
    PaiLibDetailRepository paiLibDetailRepository;
    @Resource
    ColumnistRepository columnistRepository;
    static HTTP http = HTTP.builder()
            .config(b -> b.connectTimeout(5, TimeUnit.MINUTES)
                            .readTimeout(5, TimeUnit.MINUTES)
                            .writeTimeout(5, TimeUnit.MINUTES)
                            .addInterceptor(new IInterceptor())
//                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
            ).addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    final String tempDirPath = "D:\\sgp\\解说视频TWO";

    @Test
    void fastDownload() throws InterruptedException {
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        List<PaiLibDetail> all = paiLibDetailRepository.findAll((Specification<PaiLibDetail>) (root, query, criteriaBuilder) -> {
            Path<Integer> downed = root.get("downed");
            return criteriaBuilder.isNull(downed);
        });
        for (PaiLibDetail pai : all) {
            CountDownLatch latch = new CountDownLatch(2);
            Integer libraryId = pai.getLibraryId();
            String url = pai.getUrl();
            File tempDirPath = new File(tempDir, "" + libraryId);
            File file = new File(tempDirPath, libraryId + ".mp4");
            boolean m3U8 = downloadM3U8(url, tempDirPath, file, latch);
            if (m3U8) {
                pai.setDowned(1);
                pai.setFilePath(file.getAbsolutePath());
                paiLibDetailRepository.save(pai);
            }
            latch.countDown();
            latch.await();
        }
    }

    @Test
    void fastDownload0() throws InterruptedException {
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        List<PaiLibDetail> all = paiLibDetailRepository.findAll((Specification<PaiLibDetail>) (root, query, criteriaBuilder) -> {
            Path<Integer> downed = root.get("downed");
            return criteriaBuilder.equal(downed, -2);
        });
        for (PaiLibDetail pai : all) {
            CountDownLatch latch = new CountDownLatch(2);

            Integer libraryId = pai.getLibraryId();
            String url = pai.getUrl();
            File tempDirPath = new File(tempDir, "" + libraryId);
            File file = new File(tempDirPath, libraryId + ".mp4");
            boolean m3U8 = downloadM3U8(url, tempDirPath, file, latch);
            if (m3U8) {
                pai.setDowned(1);
                pai.setFilePath(file.getAbsolutePath());
                paiLibDetailRepository.save(pai);
            }
            latch.countDown();
            latch.await();
        }
    }

    private static byte[] decrypt(byte[] sSrc, String sKey) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), "AES");
            //如果m3u8有IV标签，那么IvParameterSpec构造函数就把IV标签后的内容转成字节数组传进去
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return cipher.doFinal(sSrc);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Test
    void fastDownload2() {
        List<PaiLibDetail> all = paiLibDetailRepository.findAll((Specification<PaiLibDetail>) (root, query, criteriaBuilder) -> {
            Path<Integer> downed = root.get("downed");
            return criteriaBuilder.equal(downed, 1);
        });
        for (PaiLibDetail pai : all) {
            if (!new File(pai.getFilePath()).exists()) {
                pai.setDowned(-2);
                pai.setFilePath("");
                paiLibDetailRepository.save(pai);
            }
        }
    }

    boolean downloadM3U8(String url, File folder, File file, CountDownLatch latch) {
        String keyStr = null;
        try {
            String uri = url.replace("index.m3u8", "");
            List<String> strings = IO.readLines(http.async(url)
                    .get().getResult().getBody().toByteStream());
            System.out.println(strings);
            List<String> collect = strings.stream().filter(l -> !l.startsWith("#")).collect(Collectors.toList());
            if (strings.stream().anyMatch(i -> i.contains("key.key"))) {
                keyStr = IO.readContentAsString(http.async(uri + "key.key")
                        .get().getResult().getBody().toByteStream());
            }
            if (collect.size() == 1 && collect.get(0).contains(".m3u8")) {
                String newUrl = uri + collect.get(0);
                uri = newUrl.replace("index.m3u8", "");
                strings = IO.readLines(http.async(newUrl)
                        .get().getResult().getBody().toByteStream());
                if (strings.stream().anyMatch(i -> i.contains("key.key"))) {
                    keyStr = IO.readContentAsString(http.async(uri + "key.key")
                            .get().getResult().getBody().toByteStream());
                }
                System.err.println(strings);
                collect = strings.stream().filter(l -> !l.startsWith("#")).collect(Collectors.toList());
            }

            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < collect.size(); i++) {
                map.put(collect.get(i), i + ".ts");
            }
            CountDownLatch countDownLatch = new CountDownLatch(collect.size());
            String finalUri = uri;
            int count = collect.size();
            String finalKeyStr = keyStr;
            collect.parallelStream()
                    .forEach(key -> http.async(finalUri + key)
                            .get()
                            .getResult()
                            .getBody()
                            .toFile(new File(folder, map.get(key)))
                            .setOnSuccess(f -> {
                                System.out.println(file.getName() + " : " + f.getName() + " : 下载" + (f.isFile() && f.exists() ? "完成" : "失败") + " / 共" + count + "个视频" + ".   大小: " + f.length() / 1000 + "kb");
                                if (finalKeyStr != null) {
                                    System.out.println("解密中.....");
                                    byte[] bytes = IO.readContent(f);
                                    byte[] decrypt = decrypt(bytes, finalKeyStr);
                                    System.out.println("解密完成.....");
                                    IO.write(decrypt, f);
                                }
                                countDownLatch.countDown();
                            }).start());
            countDownLatch.await();
            System.out.println("ts 片段下载完成,准备合并...");
            mergeFile(folder, file);
            System.out.println("ts 合并完成...");
            for (File listFile : Objects.requireNonNull(folder.listFiles())) {
                if ((listFile.getName().endsWith("ts") || listFile.getName().endsWith(".txt")) && listFile.isFile()) {
                    FileUtils.deleteQuietly(listFile);
                }
            }
            latch.countDown();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        File file = new File("D:\\sgp\\解说视频TWO\\148");
        File file1 = new File("D:\\sgp\\解说视频TWO\\148\\148.mp4");
        mergeFile(file, file1);
    }

    public static void mergeFile(File folder, File resultFile) {
        List<File> videoList = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(i -> i.exists() && i.getName().endsWith("ts"))
                .sorted(Comparator.comparingLong(filei -> getNum(filei.getName())))
                .collect(Collectors.toList());
        //时间作为合并后的视频名
        //所有要合并的视频转换为ts格式存到videoList里
        List<String> command = new ArrayList<>();
        command.add("C:\\Program_Files\\ffmpeg-6.0\\bin\\ffmpeg");
        File file = new File(folder, "fileList.txt");
        StringJoiner joiner = new StringJoiner("\n");
        for (File value : videoList) {
            joiner.add("file '" + value + "'");
        }
        IO.writeContent(joiner.toString(), file);
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-i");
        command.add(file.getAbsolutePath());
        command.add("-c");
        command.add("copy");
        command.add(resultFile.getAbsolutePath());
        commandStart(command);
    }


    /**
     * 调用命令行执行
     *
     * @param command 命令行参数
     */
    public static void commandStart(List<String> command) {
        ProcessBuilder builder = new ProcessBuilder();
        //正常信息和错误信息合并输出
        builder.redirectErrorStream(true);
        builder.command(command);
        //开始执行命令
        try {
            System.out.println(IO.readContentAsString(builder.start().getInputStream()).substring(0, 100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Long getNum(String args) {
        return Long.parseLong(Pattern.compile("[^0-9]").matcher(args).replaceAll("").trim());
    }
}
