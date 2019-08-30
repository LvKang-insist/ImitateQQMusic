package com.admin.work.main.home.icon_recently;

import org.litepal.crud.LitePalSupport;

/**
 *  表，保存最近播放的音乐
 */
public class RecentlySong extends LitePalSupport {
    public String name;//歌曲名
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度
    public String path;//歌曲地址
    public long  albumId;//图片id
    public long id;//歌曲id

    public RecentlySong(String name, String singer, long size, int duration, String path, long albumId, long id) {
        this.name = name;
        this.singer = singer;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.albumId = albumId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}