package com.admin.work.main.music_more;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class ItemRecyclerConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        JSONArray content = JSON.parseObject(getJsonData()).getJSONArray("content");
        for (int i = 0; i < content.size(); i++) {
            JSONObject json = content.getJSONObject(i);
            parseJson(json,MoreItemType.MORE_RECYCLER_ONE);
        }
        return ENTITLES;
    }

    public ArrayList<MultipleItemEntity> convert(int type) {
        JSONArray content = JSON.parseObject(getJsonData()).getJSONArray("content");
        for (int i = 0; i < content.size(); i++) {
            JSONObject json = content.getJSONObject(i);
            parseJson(json,type);
        }
        return ENTITLES;
    }

    private void parseJson(JSONObject json,int type) {
        String title = json.getString("title");
        String pic_url = json.getString("pic_big");
        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(type)
                .setField(MultipleFields.TEXT,title)
                .setField(MultipleFields.IMAGE_URL,pic_url)
                .build();
        ENTITLES.add(entity);
    }
}
