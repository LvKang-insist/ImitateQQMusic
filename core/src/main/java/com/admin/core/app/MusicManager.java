package com.admin.core.app;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.admin.core.net.callback.IFailure;
import com.admin.core.util.storage.LattePreference;

import java.io.File;
import java.security.PublicKey;
import java.util.WeakHashMap;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 音乐管理器
 */
public class MusicManager {

    private static final String PLAY_MODE = "play_mode";

    /**
     * 播放源
     */
    public enum MusicSource {
        /**
         * 喜欢
         */
        ICON_LOVE,
        /**
         * 本地
         */
        ICON_NATIVE,
        /**
         * 最近
         */
        ICON_RECENTLY
    }

    /**
     * 音乐源的音乐数量
     */
    public enum SourceCount {
        LOVE_COUNT,
        NATIVE_COUNT,
        RECENTLY_COUNT
    }

    /**
     * 播放模式，例如随机播放，或者顺序播放
     */
    public enum PlayMode {
        RANDOM_PLAY,
        ORDER_PLAY,
        REPETITION,
    }

    /**
     * 当前状态
     */
    public static MusicSource mDefault;

    private static final String POSITION = "position";

    public static void setSource(MusicSource source) {
        //设置当前状态
        LattePreference.setAppFlag(source.name(), true);
        //取消上一个状态

        LattePreference.setAppFlag(mDefault.name(), false);
        //保存当前状态
        mDefault = source;
    }

    /**
     * @return 返回当前状态
     */
    public static MusicSource getSource() {
        if (mDefault == null) {
            MusicSource[] values = MusicSource.values();
            for (MusicSource value : values) {
                boolean flag = LattePreference.getAppFlag(value.name());
                if (flag) {
                    mDefault = value;
                    return mDefault;
                }
            }
            mDefault = MusicSource.ICON_NATIVE;
            return mDefault;
        } else {
            return mDefault;
        }
    }

    /**
     * 设置音乐源的音乐数量
     *
     * @param source 音乐源
     * @param size   数量
     */
    public static void setMusicSize(SourceCount source, int size) {
        LattePreference.setAppInteger(source.name(), size);
    }

    /**
     * @param source 音乐源
     * @return 返回音乐源对应的数量
     */
    public static int getMusicSize(SourceCount source) {
        return LattePreference.getAppInteger(source.name());
    }

    /**
     * 保存当前播放音乐的位置
     *
     * @param pos 位置
     */
    public static void setPosition(int pos) {
        LattePreference.setAppInteger(POSITION, pos);
    }

    /**
     * @return 获取最后一次播放音乐的位置
     */
    public static int getPosition() {
        return LattePreference.getAppInteger(POSITION);
    }

    /**
     * 设置当前音乐播放的模式
     * @param mode
     */
    public static void setPlayMode(PlayMode mode){
        LattePreference.setAppData(PLAY_MODE,mode.name());
    }

    /**
     * @return 返回当前播放的音乐模式，以字符串为标志，可与PlayMode 进行比较
     */
    public static String getPlayMode(){
        String playmode = LattePreference.getAppData(PLAY_MODE);
        if (playmode == null){
            return PlayMode.ORDER_PLAY.name();
        }
        return playmode;
    }

    /**
     *  刷新媒体库，避免需要重启手机
     * @param context
     */
    public static void updateMedia(final Context context){
        if(SDK_INT >= Build.VERSION_CODES.KITKAT){//当大于等于Android 4.4时
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    context.sendBroadcast(mediaScanIntent);
                }
            });

        }else{//Andrtoid4.4以下版本
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.fromFile((new File(Environment.getExternalStorageDirectory().toString()).getParentFile()))));
        }

    }


    public static class MusicApi {

        WeakHashMap<String, Object> param = new WeakHashMap<>();
        public static final MusicApi instance = new MusicApi();

        public MusicApi getInstance() {
            return instance;
        }

        public WeakHashMap<String, Object> getParam(String name, String type) {
            param.clear();
            param.put("filter", "name");
            param.put("key", "b5554418360b88abc28c");
            param.put("secret", "582c994e023b076f0b86");
            param.put("limit", "10");
            param.put("time", "yes");
            param.put("type", type);
            param.put("input", name);
            return param;
        }
    }

}
