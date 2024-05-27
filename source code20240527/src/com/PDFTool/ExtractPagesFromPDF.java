package com.PDFTool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

public class ExtractPagesFromPDF {
    public static void main(String[] args) {
        // 原始PDF文件路径
        String sourceFilePath = "C:\\Users\\DELL\\Desktop\\1.pdf";
        // 生成的PDF文件路径
        String destinationFilePath = "C:\\Users\\DELL\\Desktop\\extracted.pdf";
        // 开始页码
        int startPage = 2;
        // 结束页码
        int endPage = 4;

        extractFromPDF(sourceFilePath, destinationFilePath, startPage, endPage);
    }

    public static void extractFromPDF(String sourceFilePath, String destinationFilePath, int startPage, int endPage) {
        try {
            // 加载原始PDF文档
            PDDocument sourceDocument = PDDocument.load(new File(sourceFilePath));
            // 原始PDF文档最大页码
            int totalPageCount = sourceDocument.getNumberOfPages();

            // 检查页码范围是否有效
            if (startPage < 1 || endPage > totalPageCount || startPage > endPage) {
                System.err.println("输入页码范围无效，请检查重新输入！！");
                sourceDocument.close();
                return;
            }

            // 创建一个新的PDF文档
            PDDocument destinationDocument = new PDDocument();

            // 提取指定页码范围的页面并添加到新的PDF文档中
            for (int i = startPage; i <= endPage; i++) {
                PDPage page = sourceDocument.getPage(i - 1); // 页码从0开始
                destinationDocument.addPage(page);
            }

            // 保存新的PDF文档
            destinationDocument.save(destinationFilePath);

            // 输出新生成PDF的页数
            int extractedPageCount = destinationDocument.getNumberOfPages();
            System.out.println("提取PDF文件成功！");
            System.out.println("提取的PDF页数为: " + extractedPageCount);

            // 关闭文档
            sourceDocument.close();
            destinationDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
