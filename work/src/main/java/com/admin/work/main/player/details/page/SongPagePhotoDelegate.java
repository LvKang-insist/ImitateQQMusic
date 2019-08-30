package com.admin.work.main.player.details.page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.ui.view.player.MusicPhoto;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.nativemusic.Song;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mob.tools.gui.ScaledImageView;

import butterknife.BindView;

import static com.blankj.utilcode.util.CrashUtils.init;

public class SongPagePhotoDelegate extends LatteDelegate {

    @BindView(R2.id.dele_song_page_photo_iamge)
    MusicPhoto mMusicPhoto;
    @BindView(R2.id.dele_song_details_author)
    AppCompatTextView mAuthor;

    private Song mSong;
    private RotateAnimation rotateAnimation;
    private Bitmap mBitmat;

    private SongPagePhotoDelegate(Song song, Bitmap bitmap) {
        this.mSong = song;
        this.mBitmat = bitmap;
    }

    public static SongPagePhotoDelegate getInstance(Song s, Bitmap mBitmap) {
        return new SongPagePhotoDelegate(s, mBitmap);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_song_page_photo;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        init();
    }

    public SongPagePhotoDelegate setSong(Song mSong) {
        this.mSong = mSong;
        return this;
    }

    public SongPagePhotoDelegate setBitmat(Bitmap mBitmat) {
        this.mBitmat = mBitmat;
        return this;
    }

    public void init() {
        mAuthor.setText(mSong.getSinger());
        if (mBitmat != null) {
            mMusicPhoto.setImageBitmap(mBitmat);

        }else {
            mMusicPhoto.setImageResource(R.mipmap.launcher_00);
        }
        if (PlayerControl.getInstence().isPlay()) {
            setImageAnim();
        } else {
            setStopAnim();
        }
    }

    /**
     * 播放动画
     */
    private void setImageAnim() {
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(7200);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        mMusicPhoto.startAnimation(rotateAnimation);
    }
    /**
     * 暂停动画
     */
    private void setStopAnim() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
    }
}
