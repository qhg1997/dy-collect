
package com.qhg.bk.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class OSSUtil {

    private static final OSSClient client;
    public static final String addr_file = "https://file.zxyjia.com";
    private static final String bucketName = "zxyj-file-public";
    private static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI4GBXM6qDJraKsatDATHC";
    private static final String ACCESS_KEY_SECRET = "I3Jc0Tq9i8WGAFWeMhOvsBXtUXRT8Q";

    static {
        client = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    public static final String imagePath = "public/images/";
    public static final String pdfPath = "public/pdf/";
    public static final String videoPath = "public/video/";
    public static final String otherPath = "public/others/";
    public static final String apkPath = "public/apk/";
    public static final String tempPath = "public/temp/";
    public static final String roleArn = "acs:ram::1362496506820887:role/zxyj-temp";
    public static final String pdfIco = "/public/brace/file_type/pdf.png";
    public static final String regionId = "cn-beijing";


    public static String upload(InputStream is, String file_type) throws Exception {
        String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + file_type;
        return upload0(is, file_type, file_name);
    }

    public static String upload0(InputStream is, String file_type, String file_name) throws Exception {
        String filePath = getFilePath(file_type) + file_name;
        int fileSize = is.available();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentDisposition("filename/filesize=" + file_name + "/" + fileSize + "Byte.");
        client.putObject(bucketName, filePath, is, metadata);
        return "/" + filePath;
    }


    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param file 上传文件
     * @return 文件在OSS上的实际地址
     */
    public static String upload(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        String file_type = getExt(file.getName());
        return upload(is, file_type);
    }

    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param file 上传文件
     * @return 文件在OSS上的实际地址
     */
    public static String upload0(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        String file_type = getExt(file.getName());
        return upload0(is, file_type, file.getName());
    }

    public static String getExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos == -1)
            return "";

        return fileName.substring(pos + 1).toLowerCase();
    }

    public static String getFilePath(String ext) {
        if ("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "gif".equals(ext) || "bmp".equals(ext) || "jfif".equals(ext))
            return imagePath;
        else if ("pdf".equals(ext))
            return pdfPath;
        else if ("mp4".equals(ext) || "3gp".equals(ext) || "rmvb".equals(ext) || "avi".equals(ext) || "mov".equals(ext) || "mkv".equals(ext) || "flv".equals(ext) || "f4v".equals(ext))
            return videoPath;
        else if ("apk".equals(ext))
            return apkPath;
        else
            return otherPath;
    }

    public static Integer getFileNum(String file_path) {
        String ext = getExt(file_path);
        switch (ext) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "jfif":
                return 1;
            case "pdf":
                return 2;
            case "mp4":
            case "3gp":
            case "rmvb":
            case "avi":
            case "mov":
            case "mkv":
            case "flv":
            case "f4v":
                return 3;
            default:
                return 0;
        }
    }

    public static String getFileIco(String file_path) {
        String ext = getExt(file_path);
        if ("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "gif".equals(ext) || "bmp".equals(ext))
            return file_path;
        else if ("pdf".equals(ext))
            return addr_file + pdfIco;
        else
            return null;
    }

    /**
     * 上传临时文件
     *
     * @param file 上传文件
     * @return 文件在OSS上的实际地址
     */
    public static String tempUpload(File file) throws Exception {
        String file_type = getExt(file.getName());
        file_type = file_type.equals("") ? "png" : file_type;
        String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + file_type;
        String filePath = tempPath + file_name;
        long fileSize = file.length();

        InputStream is = new FileInputStream(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(is.available());
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentDisposition("filename/filesize=" + file_name + "/" + fileSize + "Byte.");
        client.putObject(bucketName, filePath, is, metadata);
        is.close();
        return "/" + filePath;
    }

    public static Boolean isExist(String link) {
        try {
            return client.doesObjectExist(bucketName, link.replaceFirst("/", ""));
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isNotExist(String link) {
        return !isExist(link);
    }

    public static String saveByLink(String url) {
        File file = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(url));
            String file_name = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
            file = new File("tmp", file_name);
            if (!file.exists()) //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, getExt(file.getName()), file);
            return upload(file);
        } catch (Exception e) {
            return null;
        } finally {
            if (file != null) //noinspection ResultOfMethodCallIgnored
                file.delete();
        }
    }
}
