package com.PDFTool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 删除PDF文件中指定页面工具类
 *
 * @author ART
 * @since 2024-04-16
 */
public class DeletePDF {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\DELL\\Desktop\\1.pdf";
        String outputFilePath = "C:\\Users\\DELL\\Desktop\\completeDelete.pdf";
        // 页码从1开始计算，数组中表示删除第x页
        int[] pagesToDelete = {1, 2, 5};

        try {
            removePages(inputFilePath, outputFilePath, pagesToDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removePages(String inputFilePath, String outputFilePath, int[] pagesToDelete) throws IOException {
        int initialPageCount = getPageCount(inputFilePath);
        System.out.println("执行前PDF页数为: " + initialPageCount);

        // Load the PDF document
        try (PDDocument document = PDDocument.load(new File(inputFilePath))) {
            // Get the page tree
            PDPageTree pages = document.getPages();

            // 逆序排序指定页码数组
            Arrays.sort(pagesToDelete);
            // 从最后一页开始删除，避免由于删除前面的页码而引起的页码位置变化删除非指定页面
            for (int i = pagesToDelete.length - 1; i >= 0; i--) {
                int pageNumber = pagesToDelete[i];
                if (pageNumber > 0 && pageNumber <= document.getNumberOfPages()) {
                    pages.remove(pageNumber - 1);
                    System.out.println("删除第" + pageNumber + "页成功");
                } else {
                    System.out.println("删除的指定页码第 " + pageNumber + " 页不存在，请检查！");
                }
            }

            // Save the modified document
            document.save(outputFilePath);
        }

        int finalPageCount = getPageCount(outputFilePath);
        System.out.println("执行完PDF页数为: " + finalPageCount);

        System.out.println("PDF删除全部指定页面成功！");
    }

    private static int getPageCount(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            return document.getNumberOfPages();
        }
    }
}
