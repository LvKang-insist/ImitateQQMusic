package com.admin.work.main.home.icon_native;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.nativemusic.LocalMusicUtils;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;

public class NativeDelegate extends LatteDelegate {

    @BindView(R2.id.toolbar_first_back)
    IconTextView mIconBack = null;
    @BindView(R2.id.toolbar_first_textview)
    AppCompatTextView mTextName = null;
    @BindView(R2.id.toolbar_first_more)
    IconTextView mIconMore = null;
    @BindView(R2.id.delegate_first_toolbar)
    Toolbar mToolbar = null;

    @BindView(R2.id.dele_navite_recycler)
    RecyclerView mRecyclerView = null;
    private NativeRecyclerAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_native;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mToolbar.inflateMenu(R.menu.native_menu);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initToolbar();
        initRecycler();
        //回调，当音乐状态发送变化时调用，
        CallbackManager.getInstance().addCallback(MusicManager.MusicSource.ICON_NATIVE, (this::setConstomMusicPos));
        //音乐删除后 重新进行扫描
        CallbackManager.getInstance().addCallback(CallBackType.DELETE_MUSIC, args -> {
            Song song = (Song) args;
            LitePal.deleteAll(Song.class, "path=?", song.path);
            int pos = mAdapter.mPrePostion;
            mAdapter.getData().clear();
            mAdapter.setNewData(new NativeConverter().convert());
            PlayerControl.getInstence().setNewRecyclerData();
            mAdapter.setItem(pos);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.native_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NativeRecyclerAdapter(new NativeConverter().convert(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        mTextName.setText("本地歌曲");
        mIconBack.setOnClickListener((view -> {
            setBreak();
        }));
        mIconMore.setVisibility(View.GONE);
        mToolbar.setOnMenuItemClickListener(item -> {
            MusicManager.updateMedia(getContext());
            if (item.getItemId() == R.id.scan_music) {
                scanMusic();
            }
            return false;
        });
    }

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
                MusicManager.setMusicSize(MusicManager.SourceCount.RECENTLY_COUNT, -1);
            } else {
                MusicManager.setMusicSize(MusicManager.SourceCount.NATIVE_COUNT, music.size());
                Toast.makeText(getContext(), "" + music.size(), Toast.LENGTH_SHORT).show();
            }
        }
        if (mAdapter != null) {
            mAdapter.getData().clear();
            mAdapter.setNewData(new NativeConverter().convert());
        }
    }

    /**
     * 退出时 调用接口，加载关闭动画
     */
    private void setBreak() {
        IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
        if (callBack != null) {
            callBack.executeCallBack(null);
        }
        getSupportDelegate().pop();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //界面可见的时候刷新播放的位置
        if (MusicManager.getSource() == MusicManager.MusicSource.ICON_NATIVE) {
            int position = MusicManager.getPosition();
            if (mAdapter != null && position > 0) {
                mAdapter.setItem(position);
                mRecyclerView.scrollToPosition(position);
            }
        }
    }

    /**
     * 此方法会读取当前播放的歌曲信息，并更新item
     */
    private void setConstomMusicPos(Object args) {
        //获取当前播放的 音乐
        Song song = (Song) args;
        //判断指定位置上的 对象和保存的是否一致
        final int count = mAdapter.getData().size();
        for (int i = 0; i < count; i++) {
            Song s = mAdapter.getData().get(i).getField(HomeItemFields.SONG);
            if (s.path.equals(song.path)) {
                mAdapter.setItem(i);
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        setBreak();
        return true;
    }
}
