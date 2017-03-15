package me.winds.album.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.winds.album.bean.Folder;
import me.winds.album.bean.ImageFolder;
import me.winds.album.tools.VideoUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author by Winds on 2016/11/10 0010.
 * Email heardown@163.com.
 */

public class LoaderUtils {

    /**
     * 获取指定路径下的视频路径
     *
     * @param dir
     * @return
     */
    public static Observable<List<Folder>> getVideoFolder(final String dir) {

        Observable<List<Folder>> observable = Observable
                .create(new Observable.OnSubscribe<List<Folder>>() {
                    @Override
                    public void call(Subscriber<? super List<Folder>> subscriber) {
                        if (!subscriber.isUnsubscribed()) {

                            File file = new File(dir);
                            if (file != null && file.isDirectory()) {

                                final List<Folder> list = new ArrayList<>();
                                file.listFiles(new FileFilter() {
                                    @Override
                                    public boolean accept(File f) {
                                        if (f.isDirectory()) {
                                            VideoUtils.getVideoFile(list, f);
                                        }
                                        return false;
                                    }
                                });
                                subscriber.onNext(list);
                                subscriber.onCompleted();
                            }

                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }

    public static Observable<Folder> getImageFolder(final String dir) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(final Subscriber<? super Folder> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    final File file = new File(dir); //IrSurvey/Image

                    if (file != null && file.isDirectory()) {

                        file.listFiles(new FileFilter() { //列出子目录
                            @Override
                            public boolean accept(File f) {

                                if (f.isDirectory()) {   //子目录

                                    final Folder folder = new Folder();
                                    folder.setDir(f.getAbsolutePath()); //设置目录
                                    folder.setType(1);                      //类型

                                    int length = f.list(new FilenameFilter() {
                                        @Override
                                        public boolean accept(File dir, String name) {
                                            if (name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".jpg")) {
                                                if (folder.getCover() == null) {
                                                    folder.setCover(dir.getAbsolutePath() + File.separator + name); //设置封面
                                                }

                                                return true;
                                            }

                                            return false;
                                        }
                                    }).length;
                                    folder.setSize(length);     //设置容量

                                    subscriber.onNext(folder);
                                } else {
                                    //TODO 不是目录
                                }

                                return false;
                            }
                        });

                        subscriber.onCompleted();
                    } else {
                        //TODO 顶级目录为空

                    }

                }
            }
        });
    }


    /**
     * 获取指定路径下的图片
     *
     * @param context
     * @param dir
     * @return
     */
    public static Observable<Folder> getImageFolder(final Context context, final String dir) {

        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    String selection = MediaStore.Images.Media.DATA + " like ?";
                    //设定查询目录
                    //定义selectionArgs：
                    String[] selectionArgs = {dir + "%"};
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA},
                            selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
                    if (cursor != null) {
                        String cover = null;

                        //临时的辅助类，用于防止同一个文件夹的多次扫描
                        HashSet<String> mDirPaths = new HashSet<String>();

                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            if (cover == null) {
                                cover = path;
                            }
                            //获取父文件
                            File parentFile = new File(path).getParentFile();
                            if (parentFile == null) {
                                continue;
                            }
                            //获取父文件夹
                            String dirPath = parentFile.getAbsolutePath();
                            Folder folder = null;
                            //如果父文件夹已添加，跳出
                            if (mDirPaths.contains(dirPath)) {
                                continue;
                            } else {
                                //添加父路径到文件夹集合
                                mDirPaths.add(dirPath);
                                folder = new Folder();
                                folder.setDir(dirPath);
                                folder.setCover(path);
                            }
                            //文件夹下图片多少
                            int picSize = parentFile.list(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {

                                    if (name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")) {
                                        return true;
                                    }
                                    return false;
                                }
                            }).length;

                            //设置文件夹下文件总数
                            folder.setSize(picSize);
                            folder.setType(1);
                            subscriber.onNext(folder);
                        }
                        // 扫描完成，辅助的HashSet也就可以释放内存了
                        mDirPaths.clear();
                        mDirPaths = null;
                        cursor.close();
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(null);
                    }
                }
            }
        });
    }


    /**
     * 获取指定目录下的图片
     *
     * @param context
     * @param dir     绝对目录路径
     */
    public static Observable<List<ImageFolder>> getImagePath(final Context context, final String dir) {


        return Observable.create(new Observable.OnSubscribe<List<ImageFolder>>() {
            @Override
            public void call(Subscriber<? super List<ImageFolder>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    String selection = MediaStore.Images.Media.DATA + " like ?";
                    //设定查询目录
                    //定义selectionArgs：
                    String[] selectionArgs = {dir + "%"};
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                            selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
                    if (cursor != null) {

                        String firstImage = null;

                        //临时的辅助类，用于防止同一个文件夹的多次扫描
                        HashSet<String> mDirPaths = new HashSet<String>();
                        //扫描拿到所有的图片文件夹
                        List<ImageFolder> mImageFloders = new ArrayList<ImageFolder>();

                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            if (firstImage == null) {
                                firstImage = path;
                            }
                            //获取父文件
                            File parentFile = new File(path).getParentFile();
                            if (parentFile == null) {
                                continue;
                            }
                            //获取父文件夹
                            String dirPath = parentFile.getAbsolutePath();
                            ImageFolder imageFolder = null;
                            //如果父文件夹已添加，跳出
                            if (mDirPaths.contains(dirPath)) {
                                continue;
                            } else {
                                //添加父路径到文件夹集合
                                mDirPaths.add(dirPath);
                                imageFolder = new ImageFolder();
                                imageFolder.setDir(dirPath);
                                imageFolder.setFirstImagePath(path);
                            }
                            //文件夹下图片多少
                            int picSize = parentFile.list(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {

                                    if (name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")) {
                                        return true;
                                    }
                                    return false;
                                }
                            }).length;

                            //设置文件夹下文件总数
                            imageFolder.setCount(picSize);
                            mImageFloders.add(imageFolder);
                        }
                        // 扫描完成，辅助的HashSet也就可以释放内存了
                        mDirPaths.clear();
                        mDirPaths = null;

                        cursor.close();

                        subscriber.onNext(mImageFloders);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(null);
                    }
                }
            }
        });
    }


}