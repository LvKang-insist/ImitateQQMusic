package com.admin.work.main.player;

import android.util.Log;
import android.widget.Toast;

import com.admin.core.app.Latte;
import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleltemEntityBuilder;
import com.admin.core.ui.view.player.Player;
import com.admin.core.util.BeanCopy;
import com.admin.work.R;
import com.admin.work.main.home.icon_love.LoveSong;
import com.admin.work.main.home.icon_native.ChineseCharComp;
import com.admin.work.main.home.icon_recently.RecentlySong;
import com.admin.work.main.player.nativemusic.Song;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Lv
 * Created at 2019/7/7
 */
public class PlayerConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        return null;
    }

    public ArrayList<MultipleItemEntity> convert(Class table) {
        try {
            if (table == LoveSong.class) {
                List<LoveSong> all = LitePal.findAll(table);
                Log.e("------------", "convert: "+all.size() );
                for (int i = 0; i < all.size(); i++) {
                    Song song = BeanCopy.modelAconvertoB(all.get(i), Song.class);
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(PlayerItemType.PLAYER_LEFT_RIGHT)
                            .setField(MultipleFields.TAG, song)
                            .build();
                    ENTITLES.add(entity);
                }
                return ENTITLES;
            }
            if (table == Song.class) {
                List<Song> all = LitePal.findAll(table);
                Comparator cmp = new ChineseCharComp();
                Collections.sort(all, cmp);
                for (int i = 0; i < all.size(); i++) {
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(PlayerItemType.PLAYER_LEFT_RIGHT)
                            .setField(MultipleFields.TAG, all.get(i))
                            .build();
                    ENTITLES.add(entity);
                }
                return ENTITLES;
            }
            if (table == RecentlySong.class) {
                Toast.makeText(Latte.getApplication(), "最近", Toast.LENGTH_SHORT).show();
                List<RecentlySong> all = LitePal.findAll(table);
                for (int i = 0; i < all.size(); i++) {
                    Song song = BeanCopy.modelAconvertoB(all.get(i), Song.class);
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(PlayerItemType.PLAYER_LEFT_RIGHT)
                            .setField(MultipleFields.TAG, song)
                            .build();
                    ENTITLES.add(entity);
                }
                Collections.reverse(ENTITLES);
                return ENTITLES;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
