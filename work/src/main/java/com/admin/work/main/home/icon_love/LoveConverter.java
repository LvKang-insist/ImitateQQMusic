package com.admin.work.main.home.icon_love;

import android.util.Log;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.util.BeanCopy;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.admin.work.main.player.nativemusic.Song;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class LoveConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        String data = getJsonData();
        List<LoveSong> list = new ArrayList<>();
        if (data != null) {
            JSONObject json = JSON.parseObject(data);
            if (json.getString("result").equals("success")) {
                JSONArray array = json.getJSONArray("song");
                final int count = array.size();
                for (int i = 0; i < count; i++) {
                    json = array.getJSONObject(i);
                    Song song = new Song();
                    song.setName(json.getString("song"));
                    song.setPath(json.getString("path"));
                    song.setSinger(json.getString("singer"));
                    song.setAlbumId(0);
                    song.setDuration(0);
                    song.setId(0);
                    song.setSize(0);
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(HomeItemType.HOME_TAB_lOVE)
                            .setField(HomeItemFields.SONG,song)
                            .setField(MultipleFields.TAG,false)
                            .build();
                    ENTITLES.add(entity);
                    LoveSong love = BeanCopy.modelAconvertoB(song, LoveSong.class);
                    list.add(love);
                }
            }
            LitePal.deleteAll(LoveSong.class);
            LitePal.saveAll(list);
        }
        return ENTITLES;
    }
}
