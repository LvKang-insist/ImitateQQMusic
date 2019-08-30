package com.admin.work.main.player;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.image.UrlToBitmap;
import com.admin.work.R;
import com.admin.work.main.home.HomeDelegate;
import com.admin.work.main.home.search.NetWorkQQSong;
import com.admin.work.main.player.details.SongDetailsDelegate;
import com.admin.work.main.player.nativemusic.Song;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Lv
 * Created at 2019/7/7
 */
public class PlayerAdapter extends MultipleRecyclerAdapter {

    private HomeDelegate delegate;

    protected PlayerAdapter(List<MultipleItemEntity> data, HomeDelegate delegate) {
        super(data);
        this.delegate = delegate;
        addItemType(PlayerItemType.PLAYER_LEFT_RIGHT, R.layout.player_item_left_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        switch (holder.getItemViewType()) {
            case PlayerItemType.PLAYER_LEFT_RIGHT:
                Song song = entity.getField(MultipleFields.TAG);
                PlayerControl instence = PlayerControl.getInstence();
                instence.requestSongMessage(song, (netWorkQQSong -> {
                    if (netWorkQQSong != null) {
                        UrlToBitmap.urlToBitmap(netWorkQQSong.getPic(), bitmap -> {
                            //将数据保存起来
                            entity.setField(PlayerItemFields.SONG, song);
                            entity.setField(PlayerItemFields.NETWORK_SONG, netWorkQQSong);
                            entity.setField(PlayerItemFields.BITMAP, bitmap);
                            holder.setImageBitmap(R.id.item_player_photo, bitmap);
                        });
                    }
                }));
                holder.getView(R.id.item_paly).setOnClickListener((view -> {
                    NetWorkQQSong netSong = entity.getField(PlayerItemFields.NETWORK_SONG);
                    Bitmap bitmap = entity.getField(PlayerItemFields.BITMAP);
                    delegate.getParentDelegate().getSupportDelegate()
                            .start(new SongDetailsDelegate(song, netSong, bitmap));

                }));
                if (song != null) {
                    holder.setText(R.id.item_player_name, song.name + " - " + song.getSinger()
                    );
                    holder.setImageResource(R.id.item_player_photo, R.mipmap.launcher_04);
                } else {
                    holder.setText(R.id.item_player_name, "天天动听");
                }
                break;
            default:
        }
    }

}
