package com.admin.work.main.home.icon_recently;


import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.util.BeanCopy;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.player.nativemusic.Song;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecentlyConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        //获取本地音乐
        List<RecentlySong> list = LitePal.findAll(RecentlySong.class);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Song song = BeanCopy.modelAconvertoB(list.get(i),Song.class);
            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(HomeItemType.HOME_RECENTLY_SONG)
                    .setField(HomeItemFields.SONG,song)
                    .setField(MultipleFields.TAG,false)
                    .build();
            ENTITLES.add(entity);
        }
        Collections.reverse(ENTITLES);//倒序
        return ENTITLES;
    }
}
