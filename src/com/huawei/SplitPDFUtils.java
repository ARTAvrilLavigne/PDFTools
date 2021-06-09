package com.huawei;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * �Զ����и�PDFҳ��������
 */
public class SplitPDFUtils {
    /**
     * ִ���Զ���ҳ���ָ�
     *
     * @param originPDFPath ԭʼpdf���·��
     * @param savepath ����·���������ļ���
     * @param from ��ʼ
     * @param end ����
     * @return �ɹ����
     */
    public static boolean splitPDFFile(String originPDFPath, String savepath, int from, int end) {
        Document document;
        PdfCopy copy;
        if(from > end || from < 0){
            System.out.println("����ҳ�������⣬���飡");
            return false;
        }
        try {
            PdfReader reader = new PdfReader(originPDFPath);
            int n = reader.getNumberOfPages();
            if (end == 0 || end > n) {
                end = n;
                System.out.println("�����ȡ���ҳ���������Զ������ҳ����ȡ����");
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
