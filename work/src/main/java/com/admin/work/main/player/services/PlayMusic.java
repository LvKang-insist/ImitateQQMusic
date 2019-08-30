package com.admin.work.main.player.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.admin.core.app.Latte;
import com.admin.work.main.player.PlayerControl;

import java.io.IOException;

/**
 * 音乐播放的基类
 */
public class PlayMusic {
    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static PlayMusic instance;
    private MediaPlayer mMediaPlayer;
    private String mPath;
    private OnMediaPlayerListener onMediaPlayerListener;
    private boolean isPlay = false;

    private PlayMusic(Context context) {
        this.mContext = context;
        mMediaPlayer = new MediaPlayer();
    }

    public static PlayMusic getInstance(Context context) {
        if (instance == null) {
            synchronized (PlayMusic.class) {
                if (instance == null) {
                    instance = new PlayMusic(context);
                }
            }
        }
        return instance;
    }

    public void setOnMediaPlayerListener(OnMediaPlayerListener onMediaPlayerListener) {
        this.onMediaPlayerListener = onMediaPlayerListener;
    }

    /**
     * 需要播放的音乐路径
     */
    public void setPath(String path) {
        //重置音乐播放状态
        mMediaPlayer.reset();
        //设置播放音乐路径
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            //准备播放
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener((mediaPlayer -> {
            if (onMediaPlayerListener != null) {
                onMediaPlayerListener.onPrepared(mediaPlayer);
                onMediaPlayerListener = null;
            }
        }));
    }

    /**
     * @return 返回当前音乐播放的路径
     */
    public String getPath() {
        return mPath;
    }

    /**
     * 播放音乐
     */
    public void start(String path,OnMediaSaveListener listener) {
        this.mPath = path;
        if (mMediaPlayer.isPlaying()) {
            return;
        }
        mMediaPlayer.start();
        isPlay = true;
        //播放结束后调用
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mPath != null && PlayerControl.MUSIC_PATH != null) {
                    if (PlayerControl.MUSIC_PATH.equals(mPath)) {
                        listener.onSave(mediaPlayer);
                    }
                }
            }
        });
    }

    public MediaPlayer getMediaPlay(){
        return mMediaPlayer;
    }

    /**
     * 准备成功的回调
     */
    public interface OnMediaPlayerListener {
        void onPrepared(MediaPlayer mediaPlayer);
    }

    /**
     * 播放完成的回调
     */
    public interface OnMediaSaveListener {
        void onSave(MediaPlayer mediaPlayer);
    }
}
