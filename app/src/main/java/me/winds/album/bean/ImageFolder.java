package me.winds.album.bean;

import java.io.Serializable;

/**
 * Author by Winds on 2016/11/11 0011.
 * Email heardown@163.com.
 */

public class ImageFolder implements Serializable {

    private String dir; //图片的文件夹路径

    private String firstImagePath; //第一张图片的路径

    private String name; //文件夹的名称

    private int count; //图片的数量

    private int type;  //类型 1 图片 2 视频  3 上位机模式

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImageFolder{" +
                "dir='" + dir + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}