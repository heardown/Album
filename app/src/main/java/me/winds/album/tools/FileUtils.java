package me.winds.album.tools;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import me.winds.album.GloableValue;

public class FileUtils {

    /**
     * 删除指定文件
     */
    public static void deleteFile(String path) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] subFile = dirFile.list();
            for (int i = 0; i < subFile.length; i++) {
                new File(dirFile, subFile[i]).delete();
            }
        }
        dirFile.delete();
    }

    public static String copyFile(String from, String to) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
            int len = 0;
            while ((len = bis.read()) != -1) {
                bos.write(len);
            }
            bis.close();
            bos.close();
            return to;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 把内容写入文件
     *
     * @param filename 文件路径
     * @param text     内容
     */
    public static void write2File(String filename, String text, boolean append) {
        BufferedWriter bw = null;
        try {
            String path = initPath() + File.separator + filename;
            //1.创建流对象
            bw = new BufferedWriter(new FileWriter(path, append));
            //2.写入文件
            bw.write(text);
            //换行刷新
//            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭流资源
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static File getFileFromBytes(byte[] b) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            String path = initPath() + File.separator + System.currentTimeMillis() + ".txt";
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File getFileFromBytes(byte[] b, String filename) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            String path = initPath() + File.separator + filename;
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 初始化保存路径
     *
     * @return
     */
    public static String initPath() {
        String storagePath = null;
        try {
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "IrSurvey" + File.separator + "files";

        } catch (Exception e) {
            storagePath = "/data/data/com.dlc.irsurvey/files";
            e.printStackTrace();
        } finally {
            File file = new File(storagePath);
            if (!file.exists()) {
                file.mkdir();
            }
        }

        return storagePath;
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param bitmap
     */
    public static void saveBitmap(Bitmap bitmap) {

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + dataTake + ".jpeg";
        try {
            FileOutputStream fos = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param bitmap
     */
    public static void saveBitmap2File(Bitmap bitmap) {

        String path = PathUtils.createDir(GloableValue.FOLDER_TEMP_VIDEO);

        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + dataTake + ".png";
        try {
            FileOutputStream fos = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param bitmap
     */
    public static void saveBitmap2File(Bitmap bitmap, String pathname) {

        String path = PathUtils.createDir(pathname);
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + dataTake + ".png";
        try {
            FileOutputStream fos = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param bitmap
     */
    public static void saveBitmap(Bitmap bitmap, OnSaveCallback callback) {

        String path = initPath();
        String filename = path + File.separator + System.currentTimeMillis() + ".jpeg";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

            callback.afterSaveSuccess(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param bitmap
     */
    public static void saveBitmap(Bitmap bitmap, String sid, OnSaveCallback callback) {

        String path = initPath();
        long current = System.currentTimeMillis();
        String filename = path + "/" + sid + "_" + current + ".jpeg";
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 65, bos);

            bos.flush();
            bos.close();

            callback.afterSaveSuccess(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片保存成功后回调
     *
     * @author Administrator
     */
    public interface OnSaveCallback {
        void afterSaveSuccess(String path);
    }
}
