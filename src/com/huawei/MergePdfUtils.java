package com.huawei;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileOutputStream;
import java.util.List;

/**
 *  �ϲ����PDFΪһ��PDF�ļ�
 */
public class MergePdfUtils {
    /**
     * �ϳ�Ϊ�ļ�
     * @param files �ļ����ڵ�·���б�
     * @param newfile ���ļ���·��
     * @throws Exception �쳣
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
