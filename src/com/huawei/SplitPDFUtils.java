package com.huawei;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义切割PDF页数并生成
 */
public class SplitPDFUtils {
    /**
     * 执行自定义页数分割
     *
     * @param originPDFPath 原始pdf存放路径
     * @param savepath 生成路径，包括文件名
     * @param from 起始
     * @param end 结束
     * @return 成功与否
     */
    public static boolean splitPDFFile(String originPDFPath, String savepath, int from, int end) {
        Document document;
        PdfCopy copy;
        if(from > end || from < 0){
            System.out.println("输入页码有问题，请检查！");
            return false;
        }
        try {
            PdfReader reader = new PdfReader(originPDFPath);
            int n = reader.getNumberOfPages();
            if (end == 0 || end > n) {
                end = n;
                System.out.println("输入截取最大页数有误，已自动用最大页数截取处理！");
            }
            List<String> savepaths = new ArrayList<>();
            savepaths.add(savepath);
            document = new Document(reader.getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(savepaths.get(0)));
            document.open();
            for (int j = from; j <= end; j++) {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }
            document.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }



}
