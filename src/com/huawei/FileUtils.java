package com.huawei;

import java.io.File;

public class FileUtils {
    /**
     * 获取文件所在的目录
     *
     * @param filePath 文件路径
     * @return 目录路径
     */
    public static String getBasePath(String filePath){
        File file = new File(filePath);
        return file.getParent() + File.separator;
    }
}
