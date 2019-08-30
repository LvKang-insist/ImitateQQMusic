package com.admin.work.main.discover.tab;

import android.nfc.tech.NfcA;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabRecommendConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        return ENTITLES;
    }

    public ArrayList<MultipleItemEntity> convert(List<BaseMV> list, String songList) {
        if (songList!=null && !songList.equals("timeout")) {
            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscoverItemType.DISCOVER_RECYCLER)
                    .setField(MultipleFields.TITLE, "24小时快讯")
                    .setField(MultipleFields.TEXT, songList)
                    .build();
            ENTITLES.add(entity);
        }
        for (int i = 0; i < list.size(); i++) {
            MultipleItemEntity e = MultipleItemEntity.builder()
                    .setItemType(DiscoverItemType.DISCOVER_VIDEO)
                    .setField(DiscoverItemFields.BASE_MV, list.get(i))
                    .build();
            ENTITLES.add(e);
        }
        return ENTITLES;
    }
}
