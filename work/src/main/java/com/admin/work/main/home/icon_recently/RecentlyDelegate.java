package com.admin.work.main.home.icon_recently;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.icon_native.NativeRecyclerAdapter;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;

public class RecentlyDelegate extends LatteDelegate {

    @BindView(R2.id.toolbar_first_back)
    IconTextView mIconBack = null;
    @BindView(R2.id.toolbar_first_textview)
    AppCompatTextView mTextName = null;
    @BindView(R2.id.toolbar_first_more)
    IconTextView mIconMore = null;

    @BindView(R2.id.dele_recently_recycler)
    RecyclerView mRecyclerView = null;
    private RecentlyRecyclerAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_recently;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initToolbar();
        initRecycler();
        //回调，当音乐状态发送变化时调用，
        CallbackManager.getInstance().addCallback(MusicManager.MusicSource.ICON_RECENTLY, (this::setConstomMusicPos));
    }

    private void initRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new RecentlyRecyclerAdapter(new RecentlyConverter().convert());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        mTextName.setText("最近播放");
        mIconBack.setOnClickListener((view -> {
            setBreak();
        }));
        mIconMore.setOnClickListener((view -> {
            Toast.makeText(getContext(), "更多", Toast.LENGTH_SHORT).show();
        }));
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
        if (MusicManager.getSource() == MusicManager.MusicSource.ICON_RECENTLY) {
            int position = MusicManager.getPosition();
            if (mAdapter != null && position >0) {
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
