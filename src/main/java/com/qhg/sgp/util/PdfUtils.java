package com.qhg.sgp.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 项目名：renting
 * 包  名：utils
 * 创建者：乔回国
 * 创建时间：2021/1/13 21:29
 * 描述：
 */
@Slf4j
public class PDFUtils {

    private static final String DEST = new File("target").getAbsolutePath();
    private static final String FONT = new File("C:\\Users\\qiaohuiguo\\IdeaProjects\\dy\\src\\main\\resources\\simsun.ttc").getAbsolutePath();

    public static String html2Pdf(String html, String name) {
        String base = DEST + File.separator + UUID.randomUUID();
        String filePath = base + File.separator + name + ".pdf";
        File destDir = new File(base);
        if (!destDir.exists())  //noinspection ResultOfMethodCallIgnored
            destDir.mkdirs();

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            writer = PdfWriter.getInstance(document, fileOutputStream);
        } catch (DocumentException | FileNotFoundException e) {
            log.error(e.getMessage());
        }
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(FONT);
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(html.getBytes()), null, StandardCharsets.UTF_8, fontImp);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        // step 5
        document.close();

        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return filePath;
    }
}
