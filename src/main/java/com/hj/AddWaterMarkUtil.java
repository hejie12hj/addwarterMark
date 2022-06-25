package com.hj;

import com.hj.core.AddWaterMark;
import com.hj.pdf.PdfAddWaterMark;
import com.hj.word.WordAddWaterMark;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式整合不同类型文件
 */
public class AddWaterMarkUtil implements AddWaterMark {
    private static Map<String , AddWaterMark> addWaterMarkMap = new HashMap<>();

    static {
        //pdf 解析
        addWaterMarkMap.put("pdf",new PdfAddWaterMark());
        //docx 解析
        addWaterMarkMap.put("docx",new WordAddWaterMark());
    }

    public AddWaterMarkUtil() {
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException {
        String[] split = targetPath.split(File.separator);
        String fileName = split[split.length-1];
        String suffix = fileName.split("\\.")[1];
        addWaterMarkMap.get(suffix).transfer(sourcePath,targetPath,waterMarkContent);
    }
}