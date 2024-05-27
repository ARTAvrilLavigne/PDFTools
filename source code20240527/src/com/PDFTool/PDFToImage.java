package com.PDFTool;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将PDF文件的每一页转化为图片png格式
 *
 * @author ART
 * @since 2024-04-16
 */
public class PDFToImage {
    public static void main(String[] args) {
        String source = "C:\\Users\\DELL\\Desktop\\2.pdf";
        String desFileName = "testPic";
        String desFilePath = "C:\\Users\\DELL\\Desktop\\";
        String imageType = "png";
        Pair<Boolean, Object> pair = pdfToImage(source, desFilePath, desFileName, imageType);
        System.out.println("PDF文档转化为图片结果：" + pair.getLeft());
        if (!pair.getLeft()) {
            System.out.println("" + pair.getRight());
        } else {
            List<String> fileList = (List<String>) pair.getRight();
            System.out.println("转化的文件的内容：");
            fileList.forEach(System.out::println);
        }
    }


    /**
     * @param source      原文件
     * @param desFilePath 生成图片的路径
     * @param desFileName 生成图片的名称（多页文档时会变成：名称+下划线+从1开始的数字）
     * @param imageType   图片类型
     * @return 转换结果
     */
    public static Pair<Boolean, Object> pdfToImage(String source, String desFilePath, String desFileName, String imageType) {
        //通过给定的源路径名字符串创建一个File实例
        File file = new File(source);
        if (!file.exists()) {
            return Pair.of(false, "文件不存在，无法转化");
        }
        //目录不存在则创建目录
        File destination = new File(desFilePath);
        if (!destination.exists()) {
            boolean flag = destination.mkdirs();
            System.out.println("创建文件夹结果：" + flag);
        }

        PDDocument doc = null;
        try {
            //加载PDF文件
            doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            //获取PDF文档的页数
            int pageCount = doc.getNumberOfPages();
            System.out.println("文档一共" + pageCount + "页");
            System.out.println("正在运行生成中，请等待....");
            List<Object> fileList = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                //只有一页的时候文件名为传入的文件名，大于一页的文件名为：文件名_自增加数字(从1开始)
                String realFileName = pageCount > 1 ? desFileName + "_" + (i + 1) : desFileName;
                /**
                 *  DPI设置得越大像素越高，生成得时候也会越久
                 *  DPI 越高，生成的图片分辨率越大，图像质量也越高，但同时文件大小也会变得更大。
                 */
                int DPI = 300;
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI, ImageType.RGB);//每一页通过分辨率和颜色值进行转化
                String filePath = desFilePath + File.separator + realFileName + "." + imageType;
                //写入文件
                ImageIO.write(bufferedImage, imageType, new File(filePath));
                //文件名存入list
                fileList.add(filePath);
            }
            System.out.println("转换已全部结束，请查看！");
            return Pair.of(true, fileList);
        } catch (IOException e) {
            e.printStackTrace();
            return Pair.of(false, "PDF转化图片异常");
        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (IOException e) {
                System.out.println("关闭文档失败");
                e.printStackTrace();
            }
        }
    }

}
