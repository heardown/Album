package me.winds.album.tools;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;

import me.winds.album.bean.Folder;

import static android.provider.MediaStore.Video.Thumbnails.MICRO_KIND;

/**
 * Author by Winds on 2016/11/13 0013.
 * Email heardown@163.com.
 */

public class VideoUtils {
    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static void getVideoFile(final List<Folder> list, final File file) {
        final Folder folder = new Folder();
        folder.setDir(file.getAbsolutePath());
        folder.setType(2);

        int length = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (name.endsWith(".mp4") || name.endsWith(".3gp") || name.endsWith(".avi") || name.endsWith(".mov")) {
                    if (folder.getCover() == null) {
                        String path = pathname.getAbsolutePath();
                        Bitmap bitmap = getVideoThumbnail(path, 60, 60, MICRO_KIND);
                        if (bitmap != null) {
                            folder.setCover(bitmap);
                        }
                    }
                    list.add(folder);
                    return true;
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        }).length;

        folder.setSize(length);
    }

    public static void getFile(final File file) {
        file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return false;
            }
        });
    }

}
