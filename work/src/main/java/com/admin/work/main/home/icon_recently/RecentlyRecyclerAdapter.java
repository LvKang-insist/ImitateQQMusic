package com.admin.work.main.home.icon_recently;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.BeanCopy;
import com.admin.work.R;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;

import java.util.List;

public class RecentlyRecyclerAdapter extends MultipleRecyclerAdapter {

    private int mPrePostion = 0;

    protected RecentlyRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(HomeItemType.HOME_RECENTLY_SONG, R.layout.item_song);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_RECENTLY_SONG:
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
                }else {
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
                    PlayerControl.getInstence().palayMusicSource(MusicManager.MusicSource.ICON_RECENTLY,song);
                });
                songName.setText(song.name.trim());
                songAuthor.setText(song.singer);
                break;
            default:
        }
    }
    public void setItem(int position){
        getData().get(mPrePostion).setField(MultipleFields.TAG, false);
        notifyItemChanged(mPrePostion);
        //保存选中的 item
        getData().get(position).setField(MultipleFields.TAG, true);
        notifyItemChanged(position);
        //记录当前选中的 item
        mPrePostion = position;
    }
}
