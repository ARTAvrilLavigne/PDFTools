package com.huawei;

import java.io.File;

public class FileUtils {
    /**
     * ��ȡ�ļ����ڵ�Ŀ¼
     *
     * @param filePath �ļ�·��
     * @return Ŀ¼·��
     */
    public static String getBasePath(String filePath){
        File file = new File(filePath);
        return file.getParent() + File.separator;
    }
}
