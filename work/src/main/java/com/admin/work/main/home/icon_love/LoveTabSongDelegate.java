package com.admin.work.main.home.icon_love;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.AccountManager;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoveTabSongDelegate extends LatteDelegate {

    @BindView(R2.id.dele_love_tab_song_recycler)
    RecyclerView mRecyclerView;
    private int count;
    private LoveTabRecyclerAdapter mAdapter;

    LoveTabSongDelegate(int count) {
        this.count = count;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_love_tab_song;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        if (count == 0) {
            init();
            //回调，当音乐状态发送变化时调用，
            CallbackManager.getInstance().addCallback(MusicManager.MusicSource.ICON_LOVE, (this::setConstomMusicPos));
        }
    }

    private void init() {
        Map<String,String> map = new HashMap<>();
        map.put("name", AccountManager.getSignInNumber());
        RxRequest.onPostRx(getContext(), Resource.getString(R.string.getlovesong),
                JSON.toJSONString(map), (flag, result) -> {
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(manager);
                    LoveConverter loveConverter = new LoveConverter();
                    if (flag){
                        loveConverter.setJsonData(result);
                        mAdapter = new LoveTabRecyclerAdapter(loveConverter.convert(),this);
                        mRecyclerView.setAdapter(mAdapter);
                    }else {
                        loveConverter.setJsonData(null);
                        LoveTabRecyclerAdapter adapter = new LoveTabRecyclerAdapter(loveConverter.convert(),this);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

    }
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
}
