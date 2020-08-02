package cn.ggband.loglib.utils;

import java.io.File;
import java.io.FileInputStream;

public class FileUtils {

    /**
     * 将文件转成 字符串
     *
     * @param path 文件路径
     */
    public static String file2Str(String path) {
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new String(buffer, "US-ASCII");
        } catch (Exception exception) {
            return "";
        }
    }
}
