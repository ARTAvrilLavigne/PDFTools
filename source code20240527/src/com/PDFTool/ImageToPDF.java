package com.PDFTool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 实现单个或者多个图片转换为PDF后并合并为一个PDF文件
 *
 * @author ART
 * @since 2024-04-28
 */
public class ImageToPDF {
    public static void main(String[] args) {
        // 图片文件路径
        String[] imagePaths = {
                "C:\\Users\\DELL\\Desktop\\1.jpg",
                "C:\\Users\\DELL\\Desktop\\2.jpg",
                "C:\\Users\\DELL\\Desktop\\3.jpg",
                "C:\\Users\\DELL\\Desktop\\4.jpg",
                "C:\\Users\\DELL\\Desktop\\5.jpg",
                "C:\\Users\\DELL\\Desktop\\6.jpg",
                "C:\\Users\\DELL\\Desktop\\7.jpg",
                "C:\\Users\\DELL\\Desktop\\8.jpg",
                "C:\\Users\\DELL\\Desktop\\9.jpg",
        };

        // 输出PDF文件路径
        String outputPath = "C:\\Users\\DELL\\Desktop\\output.pdf";

        try {
            mergeImagesToPDF(imagePaths, outputPath);
            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }

    private static void mergeImagesToPDF(String[] imagePaths, String outputPath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            for (String imagePath : imagePaths) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true)) {
                    BufferedImage bImage = ImageIO.read(new File(imagePath));
                    PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

                    float imageWidth = bImage.getWidth();
                    float imageHeight = bImage.getHeight();

                    // 计算缩放比例
                    float scale = Math.min(page.getMediaBox().getWidth() / imageWidth, page.getMediaBox().getHeight() / imageHeight);

                    // 计算居中位置
                    float x = (page.getMediaBox().getWidth() - imageWidth * scale) / 2;
                    float y = (page.getMediaBox().getHeight() - imageHeight * scale) / 2;

                    contentStream.drawImage(image, x, y, imageWidth * scale, imageHeight * scale);
                }
            }
            document.save(outputPath);
        }
    }
}