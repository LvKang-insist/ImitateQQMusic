package com.admin.work.main.home.icon_love;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.app.Latte;
import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.work.R;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.home.home_dailog.HomeDialog;
import com.admin.work.main.player.nativemusic.Song;
import com.admin.work.main.player.PlayerControl;

import java.util.List;

public class LoveTabRecyclerAdapter extends MultipleRecyclerAdapter {
    public int mPrePostion = 0;
    LatteDelegate delegate ;
    protected LoveTabRecyclerAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        addItemType(HomeItemType.HOME_TAB_lOVE, R.layout.item_song);
       this.delegate = delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_TAB_lOVE:
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
                holder.getView(R.id.item_song).setOnClickListener(vi -> {
                    //获取点击的条目
                    int currentPostion = holder.getAdapterPosition();
                    setItem(currentPostion);
                    PlayerControl.getInstence().palayMusicSource(MusicManager.MusicSource.ICON_LOVE,song);
                });
                holder.getView(R.id.item_song_more).setOnClickListener((view1 -> {
                    HomeDialog dialog = new HomeDialog(delegate);
                    dialog.beginMoreDialog(song);
                }));
                holder.setText(R.id.item_song_name,song.name+" - "+song.getSinger());
                holder.setText(R.id.item_song_author, song.singer);
                break;
            default:
        }
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
