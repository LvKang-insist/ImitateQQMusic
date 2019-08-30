package com.admin.work.main.discover.tab;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class ItemRecyclerConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        JSONObject json = JSON.parseObject(getJsonData());
        if(json == null){
            return ENTITLES;
        }
        if (json.getInteger("code") == 200) {
            json = json.getJSONObject("data");
            JSONArray lis = json.getJSONArray("list");
            for (int i = 0; i < lis.size(); i++) {
                JSONObject creator = lis.getJSONObject(i);
                String name = creator.getJSONObject("creator").getString("name");
                String text = creator.getString("dissname");
                String image = creator.getString("imgurl");
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(DiscoverItemType.DISCOVER_RECYCLER_ITEM)
                        .setField(MultipleFields.NAME, name)
                        .setField(MultipleFields.TEXT, text)
                        .setField(MultipleFields.IMAGE_URL, image)
                        .build();
                ENTITLES.add(entity);
            }
        }
        return ENTITLES;
    }
}
