package com.admin.work.main.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.bottom.BottomItemDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.main.home.search.SearchDelegate;
import com.admin.work.main.home.setting.SettingDelegate;
import com.admin.work.main.player.PlayerAdapter;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.nativemusic.LocalMusicUtils;
import com.admin.work.main.player.nativemusic.Song;
import com.admin.core.util.storage.LattePreference;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

import static com.admin.work.main.player.nativemusic.LocalMusicUtils.song;

/**
 * Copyright (C)
 *
 * @file:
 * @author: 345
 * @Time: 2019/4/26 14:28
 * @description: 主页面
 */
@SuppressWarnings("AlibabaAvoidCommentBehindStatement")
public class HomeDelegate extends BottomItemDelegate {

    public interface OnUpdateCountListener {
        void onUpdate();
    }

    @BindView(R2.id.toolbar_textview)
    AppCompatTextView mToolbar_Text;
    @BindView(R2.id.toolbar_icon)
    IconTextView mToolbar_Icon;
    @BindView(R2.id.home_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R2.id.et_search_view)
    AppCompatEditText editText;
    private HomeRecyclerViewAdapter adapter;
    private static final String isMusic = "isMusic";
    private OnUpdateCountListener listener;

    @OnClick(R2.id.toolbar_icon)
    void onSettingClick() {
        getParentDelegate().getSupportDelegate().start(new SettingDelegate());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    /**
     * 扫描本地音乐的个数，并保存在数据库，如果已经保存，则直接初始化当前页面
     */
    private void scanMusic() {
        List<Song> music = LocalMusicUtils.getmusic(Latte.getApplication());
        for (int i = 0; i < music.size(); i++) {
            Song song = music.get(i);
            int index = song.getName().lastIndexOf(".");
            if (index != -1) {
                song.setName(song.getName().substring(0, index));
                if (song.getSinger() != null && song.getSinger().equals("<unknown>")) {
                    song.setSinger("未知歌手");
                }
            }
        }
        if (music.size() > 0) {
            LitePal.deleteAll(Song.class);
            LitePal.saveAll(music);
            //设置音乐源的数量
            if (music.size() == 0) {
                MusicManager.setMusicSize(MusicManager.SourceCount.NATIVE_COUNT, -1);
                MusicManager.setMusicSize(MusicManager.SourceCount.LOVE_COUNT, -1);
                MusicManager.setMusicSize(MusicManager.SourceCount.RECENTLY_COUNT, -1);
            } else {
                MusicManager.setMusicSize(MusicManager.SourceCount.NATIVE_COUNT, music.size());
            }
            //通知播放控制，进行刷新
            PlayerControl.getInstence().start(this);
            initView();
            // 只扫描一次
            LattePreference.setAppFlag(isMusic, true);
        } else {
            PlayerControl.getInstence().start(this);
            initView();
        }
    }

    private void initView() {
        mToolbar_Text.setText("我的");
        mToolbar_Icon.setText(Resource.getString(R.string.home_list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new HomeRecyclerViewAdapter(new HomeConverter().convert(), this);
        mRecyclerView.setAdapter(adapter);
        listener = () -> {
            adapter.notifyItemChanged(1);
        };
        PlayerControl.getInstence().setUpdateListener(listener);
        editText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                //加载BOTTOM动画
                IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
                if (callBack != null) {
                    callBack.executeCallBack(null);
                }
                //搜索页面
                getSupportDelegate().start(new SearchDelegate());
            }
        });
    }

    /**
     * 懒加载时 回调的方法，当前界面显示时，会回调该方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //取消刷新动画
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        // 注册回调，如果下面获取到权限，就会回调这个方法
        CallbackManager.getInstance().addCallback(CallBackType.SCAN_SD, (object) -> scanMusic());
        //获取操作 SD 卡的权限
        startScanMusicCheck();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_home;
    }
}
