package com.admin.work.main.home.icon_native;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.value.Resource;
import com.admin.work.main.home.home_dailog.HomeDialog;
import com.admin.work.main.home.home_dailog.HomeMvDelegate;
import com.admin.work.main.player.nativemusic.Song;
import com.admin.work.R;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.player.PlayerControl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class NativeRecyclerAdapter extends MultipleRecyclerAdapter {

    public int mPrePostion = 0;
    private NativeDelegate delegate;

    protected NativeRecyclerAdapter(List<MultipleItemEntity> data, NativeDelegate delegate) {
        super(data);
        addItemType(HomeItemType.HOME_NATIVE_SONG, R.layout.item_song);
        this.delegate = delegate;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_NATIVE_SONG:
                View view = holder.getView(R.id.item_home_love_view);
                AppCompatTextView songName = holder.getView(R.id.item_song_name);
                AppCompatTextView songAuthor = holder.getView(R.id.item_song_author);
                if (entity.getField(MultipleFields.TAG)) {
                    view.setBackgroundColor(
                            Latte.getApplication().getResources().getColor(R.color.app_music_green));
                    songName.setTextColor(
                            Latte.getApplication().getResources().getColor(R.color.app_music_green));
                    songAuthor.setTextColor(
                            Latte.getApplication().getResources().getColor(R.color.app_music_green));
                } else {
                    view.setBackgroundColor(
                            Latte.getApplication().getResources().getColor(R.color.smssdk_white));
                    songName.setTextColor(
                            Latte.getApplication().getResources().getColor(R.color.smssdk_black));
                    songAuthor.setTextColor(
                            Latte.getApplication().getResources().getColor(R.color.smssdk_black));
                }
                Song song = entity.getField(HomeItemFields.SONG);
                holder.getView(R.id.item_song).setOnClickListener(v -> {
                    //获取点击的条目
                    int currentPostion = holder.getAdapterPosition();
                    setItem(currentPostion);
                    //播放音乐
                    PlayerControl.getInstence().palayMusicSource(MusicManager.MusicSource.ICON_NATIVE, song);
                    PlayerControl.getInstence().addRecentlyData(song);
                });
                holder.getView(R.id.item_song_video).setOnClickListener((v -> {
                    //获取mv
                    PlayerControl.getInstence().requestSongMessage(song, netWorkQQSong -> {
                        if (netWorkQQSong != null) {
                            RxRequest.onGetRx(delegate.getContext(),Resource.getString(R.string.music_details), new String[]{"id"},
                                    new Object[]{netWorkQQSong.getSongid()}, (flag, result) -> {
                                        if (flag) {
                                            JSONObject json = JSON.parseObject(result);
                                            if (json.getInteger("code") == 200) {
                                                json = json.getJSONArray("data").getJSONObject(0);
                                                JSONObject mv = json.getJSONObject("mv");
                                                String mvId = mv.getString("vid");
                                                requestMv(mvId);
                                            }
                                        } else {
                                            Toast.makeText(Latte.getApplication(), "没有找到视频", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }));
                holder.getView(R.id.item_song_more).setOnClickListener(v -> {
                    HomeDialog dialog = new HomeDialog(delegate);
                    dialog.beginMoreDialog(song);
                });
                songName.setText(song.name.trim() + " - " + song.getSinger());
                songAuthor.setText(song.singer);
                break;
            default:
        }
    }

    private void requestMv(String mvId) {
        //获取mv 信息
        RxRequest.onGetRx(delegate.getContext(),Resource.getString(R.string.mv_qq_message), new String[]{"id"},
                new Object[]{mvId}, (flag, result) -> {
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        if (json.getInteger("code") == 200) {
                            JSONObject data = json.getJSONObject("data");
                            JSONObject mv = data.getJSONObject(mvId);
                            if (mv != null) {
                                String pic = mv.getString("cover_pic");
                                String mvUrl = Resource.getString(R.string.mv_qq_url) + "?id=" + mvId;
                                delegate.getParentDelegate().getSupportDelegate()
                                        .start(HomeMvDelegate.getInstence(pic, mvUrl));
                            }
                        }
                    } else {
                        Toast.makeText(Latte.getApplication(), "没有找到视频", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setItem(int position) {
        getData().get(mPrePostion).setField(MultipleFields.TAG, false);
        notifyItemChanged(mPrePostion);
        //保存选中的 item
        getData().get(position).setField(MultipleFields.TAG, true);
        notifyItemChanged(position);
        //记录当前选中的 item
        mPrePostion = position;

    }

}
