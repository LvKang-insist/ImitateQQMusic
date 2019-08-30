package com.admin.work.main.home.search;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.util.storage.LattePreference;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class SearchDataConverter extends DataConverter {

    enum MODE {
        SEARCH_HISTORY,
        SEARCH_RESULT,
        SEARCH_SONG
    }

    public static final String TAG_SEARCH_HISTORY = "search_history";

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        //获取历史记录
        final String jsonStr = LattePreference.getAppData(TAG_SEARCH_HISTORY);
        if (!"".equals(jsonStr) && jsonStr != null) {
            final JSONArray array = JSONArray.parseArray(jsonStr);
            final int size = array.size();
            for (int i = 0; i < size; i++) {
                final String hostoryItemText = array.getString(i);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(HomeItemType.HOME_SEARCH)
                        .setField(MultipleFields.TEXT, hostoryItemText)
                        .build();
                ENTITLES.add(entity);
            }
        }
        return ENTITLES;
    }

    public ArrayList<MultipleItemEntity> convert(MODE mode) {
        switch (mode) {
            case SEARCH_HISTORY:
                //获取历史记录
                final String jsonStr = LattePreference.getAppData(TAG_SEARCH_HISTORY);
                if (!"".equals(jsonStr) && jsonStr != null) {
                    final JSONArray array = JSONArray.parseArray(jsonStr);
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        final String hostoryItemText = array.getString(i);
                        final MultipleItemEntity entity = MultipleItemEntity.builder()
                                .setItemType(HomeItemType.HOME_SEARCH)
                                .setField(MultipleFields.TEXT, hostoryItemText)
                                .build();
                        ENTITLES.add(entity);
                    }
                }
                return ENTITLES;
            case SEARCH_RESULT:
                return parseData(HomeItemType.HOME_SEARCH_RESULT);
            case SEARCH_SONG:
                return parseData(HomeItemType.HOME_NETWORK_SONG);
        }
        return null;
    }

    private ArrayList<MultipleItemEntity> parseData(int type) {
        JSONObject json = JSON.parseObject(getJsonData());
        int code = json.getInteger("code");
        if (code == 200) {
            JSONArray data = JSON.parseArray(json.getString("data"));
            for (int i = 0; i < data.size(); i++) {
                NetWorkQQSong song = data.getJSONObject(i).toJavaObject(NetWorkQQSong.class);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(type)
                        .setField(HomeItemFields.NETWORK_SONG, song)
                        .build();
                ENTITLES.add(entity);
            }
        }
        return ENTITLES;
    }

}
