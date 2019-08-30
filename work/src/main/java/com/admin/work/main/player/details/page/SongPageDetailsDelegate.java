package com.admin.work.main.player.details.page;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.discover.tab.JzViewOutlineProvider;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SongPageDetailsDelegate extends LatteDelegate {

    @BindView(R2.id.dele_song_page_details_singer)
    AppCompatTextView mSinger = null;
    @BindView(R2.id.dele_song_page_details_albm)
    AppCompatTextView mAlbm = null;
    @BindView(R2.id.dele_song_page_details_time)
    AppCompatTextView mTime = null;
    @BindView(R2.id.dele_song_page_details_jzvdStd)
    JzvdStd mJzvdStd = null;
    JzViewOutlineProvider provider = new JzViewOutlineProvider(20);

    private Song mSong;
    private String songId;
    private String mvId;
    private String pic;
    private String mvUrl;

    private SongPageDetailsDelegate(Song song, String songId) {
        this.mSong = song;
        this.songId = songId;
    }

    public static SongPageDetailsDelegate getInstance(Song s, String songId) {
        return new SongPageDetailsDelegate(s, songId);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_song_page_details;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        init();
    }

    public SongPageDetailsDelegate setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public void init() {
        RxRequest.onGetRx(getContext(),Resource.getString(R.string.music_details), new String[]{"id"},
                new Object[]{songId}, (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        if (json.getInteger("code") == 200) {
                            JSONArray array = json.getJSONArray("data");
                            json = array.getJSONObject(0);
                            JSONArray singer = json.getJSONArray("singer");
                            StringBuilder strSinger = new StringBuilder();
                            for (int i = 0; i < singer.size(); i++) {
                                if (i >= 1) {
                                    strSinger.append("/");
                                }
                                JSONObject jsonObject = singer.getJSONObject(i);
                                strSinger.append(jsonObject.getString("name"));
                            }
                            if (strSinger != null && mSinger != null) {
                                mSinger.setText(strSinger.toString());
                            }

                            JSONObject album = json.getJSONObject("album");
                            if (album != null && mTime != null && mAlbm != null) {
                                mTime.setText(album.getString("time_public"));
                                mAlbm.setText(album.getString("name"));
                            }

                            JSONObject mv = json.getJSONObject("mv");
                            mvId = mv.getString("vid");
                            requestMv();
                        }
                    }
                });
    }

    private void requestMv() {
        //获取mv 信息
        RxRequest.onGetRx(getContext(),Resource.getString(R.string.mv_qq_message), new String[]{"id"},
                new Object[]{mvId}, (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        if (json.getInteger("code") == 200) {
                            JSONObject data = json.getJSONObject("data");
                            JSONObject mv = data.getJSONObject(mvId);
                            if (mv != null) {
                                pic = mv.getString("cover_pic");
                                mvUrl = Resource.getString(R.string.mv_qq_url) + "?id=" + mvId;
                                setMv();
                            }
                        }
                    }
                });
    }

    private void setMv() {
        if (mJzvdStd != null) {
            //设置视频圆角
            mJzvdStd.setOutlineProvider(provider);
            mJzvdStd.setClipToOutline(true);
            mJzvdStd.setUp(mvUrl, "");
            Glide.with(getContext())
                    .load(pic)
                    .into(mJzvdStd.thumbImageView);
            mJzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        Jzvd.releaseAllVideos();
    }
}
