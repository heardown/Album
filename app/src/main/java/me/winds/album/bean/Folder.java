package me.winds.album.bean;

/**
 * Author by Winds on 2016/11/14 0014.
 * Email heardown@163.com.
 */

public class Folder<T> {
    private String dir;     //目录
    private T cover;
    private String name;    //目录名称
    private int size;       //容量
    private int type;       //类型    1 图片 2 视频  3 上位机模式

    public String getDir() {
        return dir;

    }

    public void setDir(String dir) {
        this.dir = dir;
        int index = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(index + 1);
    }

    public T getCover() {
        return cover;
    }

    public void setCover(T cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "dir='" + dir + '\'' +
                ", cover='" + cover + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", type=" + type +
                '}';
    }
}
