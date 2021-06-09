package com.huawei;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileOutputStream;
import java.util.List;

/**
 *  合并多个PDF为一个PDF文件
 */
public class MergePdfUtils {
    /**
     * 合成为文件
     * @param files 文件所在的路径列表
     * @param newfile 新文件的路径
     * @throws Exception 异常
     */
    public static void mergePdfFiles(List<String> files, String newfile) throws Exception {
        Document document = new Document(new PdfReader(files.get(0)).getPageSize(1));
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
        document.open();
        for (int i = 0; i < files.size(); i++) {
            PdfReader reader = new PdfReader(files.get(i));
            int n = reader.getNumberOfPages();
            for (int j = 1; j <= n; j++) {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }
        }
        document.close();
    }
}
