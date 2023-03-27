package com.qhg.sgp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.fastjson.FastjsonMsgConvertor;
import com.qhg.dy.utils.IO;
import com.qhg.sgp.model.*;
import com.qhg.sgp.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import java.io.*;
import java.util.*;
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
            .config(b -> b.connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)

            ).addMsgConvertor(new FastjsonMsgConvertor())
            .build();

    final String destDir = "";

    @Test
    void fastCollect() {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        List<Pai> all = paiRepository.findAll((Specification<Pai>) (root, query, criteriaBuilder) -> {
            Path<Integer> type = root.get("type");
            return criteriaBuilder.equal(type, 1);
        });

        for (Pai pai : all) {
        }
    }


    public static void main(String[] args) {
        File file = new File("C:\\Users\\qiaohuiguo\\Desktop\\pai001");

//        String url = "https://kmi.522n.com/20230317/OWEwNjE4Mm/160528/1280/hls/decrypt/index.m3u8";
//        String uri = url.replace("index.m3u8", "");
//        List<String> strings = IO.readLines(http.async(url)
//                .get().getResult().getBody().toByteStream());
//        List<String> collect = strings.stream().filter(l -> !l.startsWith("#")).collect(Collectors.toList());
//        collect.parallelStream().forEach(key -> http.async(uri + key)
//                .get()
//                .getResult()
//                .getBody()
//                .toFile(new File(file, key))
//                .start());
        boolean files = mergeFile2(file, new File(file, "result123.mp4"));
        System.out.println(files);
    }

    public static boolean mergeFiles(File folder, File resultFile) {
        if (folder == null)
            return false;
        final File[] files = folder.listFiles(i -> i.getName()
                .endsWith("ts") && i.exists() && i.isFile());
        if (files == null || files.length < 1)
            return false;
        if (files.length == 1)
            return files[0].renameTo(resultFile);
        final List<File> fileList = Arrays.stream(files)
                .sorted(Comparator.comparing(file -> getNum(file.getName())))
                .collect(Collectors.toList());
        for (File file : fileList) {
            System.out.println(file.getName());
        }
        for (File file : fileList)
            if (file == null || !file.exists() || !file.isFile())
                return false;
        try {
            int bufSize = 1024;
            final FileOutputStream fileOutputStream = new FileOutputStream(resultFile);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
            byte[] buffer = new byte[bufSize];
            for (File file : fileList) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                int readcount;
                while ((readcount = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readcount);
                }
                inputStream.close();
            }
            outputStream.flush();
            outputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        for (File file : fileList) {
            file.delete();
        }
        return true;
    }

    public static boolean mergeFile2(File folder, File resultFile) {
        List<File> videoList = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(i -> i.exists() && i.getName().endsWith("ts")).collect(Collectors.toList());
        //时间作为合并后的视频名
        //所有要合并的视频转换为ts格式存到videoList里
        List<String> command1 = new ArrayList<>();
        command1.add("C:\\Program_Files\\ffmpeg-6.0\\bin\\ffmpeg");
        command1.add("-i");
        StringBuilder buffer = new StringBuilder("\"concat:");
        for (int i = 0; i < videoList.size(); i++) {
            buffer.append(videoList.get(i));
            if (i != videoList.size() - 1) {
                buffer.append("|");
            } else {
                buffer.append("\"");
            }
        }
        command1.add(String.valueOf(buffer));
        command1.add("-c");
        command1.add("copy");
        command1.add(resultFile.getAbsolutePath());
        commandStart(command1);
        return true;
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
            System.out.println(IO.readContentAsString(builder.start().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Long getNum(String args) {
        return Long.parseLong(Pattern.compile("[^0-9]").matcher(args).replaceAll("").trim());
    }
}
