package me.winds.album.tools;

import android.os.Environment;

import java.io.File;

/**
 * Author by Winds on 2016/10/28 0028.
 * Email heardown@163.com.
 */

public class PathUtils {
    /**
     * 生成指定目录
     * @param dirpath
     */
    public static String createDir(String dirpath) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirpath;
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 根据绝对路径生成目录
     * @param absolute
     * @return
     */
    public static String buildDir(String absolute) {
        File file = new File(absolute);
        if(!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }


}
