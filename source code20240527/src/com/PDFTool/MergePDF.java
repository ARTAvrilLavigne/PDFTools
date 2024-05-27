package com.PDFTool;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 合并多个PDF文件为一个PDF文件工具类
 *
 * @author ART
 * @since 2024-04-16
 */
public class MergePDF {
    public static void main(String[] args) {
//        mergeFile();
        String[] files = {
                "C:\\Users\\DELL\\Desktop\\1.pdf",
                "C:\\Users\\DELL\\Desktop\\2.pdf",
                "C:\\Users\\DELL\\Desktop\\3.pdf"
        };
        String resultFilePath = "C:\\Users\\DELL\\Desktop\\merged.pdf";
        mergePdfFiles(files, resultFilePath);
    }

    /**
     * 合并PDF
     * @param files 合并的多个文件路径
     * @param resultFilePath 输出合并文件路径
     */
    public static void mergePdfFiles(String[] files, String resultFilePath) {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        for (String file : files) {
            try {
                mergerUtility.addSource(new File(file));
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + file);
                e.printStackTrace();
            }
        }

        mergerUtility.setDestinationFileName(resultFilePath);

        try {
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            System.out.println("PDF文件合并成功！");
            int pageCount = getPageCount(resultFilePath);
            double fileSize = getFileSize(resultFilePath);
            System.out.println("合并后PDF文件有：" + pageCount + " 页");
            System.out.println("合并后PDF文件有：" + fileSize + " MB");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算PDF文件页数
     */
    private static int getPageCount(String filePath) {
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            int pageCount = document.getNumberOfPages();
            document.close();
            return pageCount;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 计算PDF文件大小   XX MB
     */
    private static double getFileSize(String filePath) {
        File file = new File(filePath);
        long byteLength = file.length();
        // Byte转换为MB
        double mbSize = byteLength / (1024.0 * 1024.0);
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(mbSize));
    }
}
