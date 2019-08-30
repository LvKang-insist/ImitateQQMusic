package com.admin.work.main.home.icon_native;

import android.util.Log;

import com.admin.core.app.Latte;
import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.work.main.player.nativemusic.LocalMusicUtils;
import com.admin.work.main.player.nativemusic.Song;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;

import org.litepal.LitePal;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class NativeConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        //获取本地音乐
        List<Song> music = LitePal.findAll(Song.class);
        Comparator cmp = new ChineseCharComp();
        Collections.sort(music,cmp);
        int size = music.size();
        for (int i = 0; i < size; i++) {
            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(HomeItemType.HOME_NATIVE_SONG)
                    .setField(HomeItemFields.SONG,music.get(i))
                    .setField(MultipleFields.TAG,false)
                    .build();
            ENTITLES.add(entity);
        }
        return ENTITLES;
    }
}
