package com.admin.work.main.player.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.admin.core.app.ConfigType;
import com.admin.core.app.Latte;
import com.admin.core.ui.view.player.Player;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.image.RoundAngle;
import com.admin.core.util.image.UrlToBitmap;
import com.admin.work.R;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.broadcast.BroadcastType;
import com.admin.work.main.player.nativemusic.Song;

/**
 * 播放音乐的 service
 */
public class MusicService extends Service {

    private MediaPlayer mMediaPlayer;
    public RemoteViews mRemoteViews ;
    public NotificationCompat.Builder mNotification;
    private NotificationChannel channel;


    public interface OnPlayListener {
        /**
         * 继续播放
         */
        void onContinue(boolean isContinue);

        /**
         * 新的播放
         *
         * @param musicLength 播放时长
         */
        void onStart(int musicLength);

        /**
         * 播放完成
         */
        void onSave();
    }

    private PlayMusic mPlayMusic;

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        mPlayMusic = PlayMusic.getInstance(this);
        mMediaPlayer = mPlayMusic.getMediaPlay();
    }

    public void startNotific(Bitmap bitmap) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new NotificationCompat.Builder(this,getPackageName());
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.service_notification);
        Song play_song = PlayerControl.play_song;
        Intent intent = new Intent();
        //image
        intent.setAction("notification_image");
        PendingIntent IntentImage = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_image, IntentImage);
        //love
        intent.setAction("notification_love");
        PendingIntent IntentLove = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_love, IntentLove);
        //up
        intent.setAction("notification_up");
        PendingIntent up = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_up, up);
        //pause
        intent.setAction("notification_pause");
        PendingIntent pause = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_pause, pause);
        //below
        intent.setAction("notification_below");
        PendingIntent below = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_below, below);
        //stop
        intent.setAction("notification_stop");
        PendingIntent stop = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_stop, stop);

        mRemoteViews.setTextViewText(R.id.notification_name, play_song.name);
        mRemoteViews.setTextViewText(R.id.notification_singer, play_song.singer);
        if (bitmap != null) {
            mRemoteViews.setImageViewBitmap(R.id.notification_image, bitmap);
        }else {
            mRemoteViews.setImageViewResource(R.id.notification_image, R.mipmap.head_photo);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            channel = new NotificationChannel("001", "my_channel",
                    NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
            mNotification.setChannelId("001");
            mNotification.setCustomContentView(mRemoteViews);
            mNotification.setSmallIcon(R.drawable.logoko);
            mNotification.setPriority(NotificationCompat.PRIORITY_MAX);
            mNotification.setOngoing(true);
            manager.notify(1, mNotification.build());
        } else {
            mNotification.setChannelId(getPackageName());
            mNotification.setContent(mRemoteViews);
            mNotification.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);//取消声音
            mNotification.setPriority(NotificationCompat.PRIORITY_MAX);
            mNotification.setSmallIcon(R.drawable.logoko);
            mNotification.setOngoing(true);
            manager.notify(1, mNotification.build());
        }
        //关闭通知的回调
        CallbackManager.getInstance().addCallback(CallBackType.STOP_NOTIF, args -> {
            manager.cancel(1);
        });
        CallbackManager.getInstance().addCallback(CallBackType.PAUSE_NOTIF, args -> {
            Latte.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    setPlayer();
                }
            });
        });
    }

    private void setPlayer() {
        if (PlayerControl.getInstence().isPlay()) {
            mRemoteViews.setImageViewResource(R.id.notification_pause, R.drawable.pause);
            PlayerControl.getInstence().stopMusic();
        } else {
            mRemoteViews.setImageViewResource(R.id.notification_pause, R.drawable.start);
            PlayerControl.getInstence().startMusic();
        }
    }

    public class MusicBind extends Binder {
        /**
         * 播放音乐
         */
        public void playMusic(String path, OnPlayListener listener) {
            //判断当前音乐是否是正在播放的音乐
            if (mPlayMusic.getPath() != null &&
                    mPlayMusic.getPath().equals(path)) {
                //如果是当前在播放的音乐，执行start,监听播放完成后调用 onSave
                mPlayMusic.start(path, mediaPlayer -> listener.onSave());
                if (listener != null) {
                    //继续播放的回调
                    listener.onContinue(true);
                }
            } else {
                //监听指定的音乐是否准备完成
                mPlayMusic.setOnMediaPlayerListener((mediaPlayer -> {
                    //已经完成，开始播放
                    mPlayMusic.start(path, mp -> {
                        //播放完成
                        if (listener != null) {
                            listener.onSave();
                        }
                    });
                    if (listener != null) {
                        listener.onContinue(false);
                        listener.onStart(mediaPlayer.getDuration());
                    }
                }));
                //如果不是，就传入地址，播放指定的音乐
                mPlayMusic.setPath(path);
            }
        }

        /**
         * 是否播放
         */
        public boolean isPlay() {
            return mMediaPlayer.isPlaying();
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            mMediaPlayer.pause();
        }

        /**
         *  显示通知
         */
        public void startNotif(Bitmap bitmap){
            startNotific(bitmap);
        }


        /**
         * @return 返回当前播放的位置
         */
        public int getCurrentPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        /**
         * 设置当前的位置
         *
         * @param smec 毫秒为单位
         */
        public void seekTo(int smec) {
            mMediaPlayer.seekTo(smec);
        }
    }

    @Override
    public void onDestroy() {
        if (mPlayMusic != null) {
            mMediaPlayer.stop();
        }
        super.onDestroy();
    }
}
