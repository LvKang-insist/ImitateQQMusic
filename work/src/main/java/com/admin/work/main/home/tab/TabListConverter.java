package com.admin.work.main.home.tab;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.work.main.discover.tab.DiscoverItemType;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class TabListConverter extends DataConverter {

    private int count;
    private int size;

    public TabListConverter(int count) {
        this.count = count;
    }
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        if (count == 0) {
            JSONObject json = JSON.parseObject(getJsonData());
            if (json.getInteger("code") == 200) {
                json = json.getJSONObject("data");
                JSONArray lis = json.getJSONArray("list");
                size = lis.size();
                for (int i = 0; i < size; i++) {
                    JSONObject creator = lis.getJSONObject(i);
                    String text = creator.getString("dissname");
                    String image = creator.getString("imgurl");
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setItemType(HomeItemType.HOME_TAB_LIST)
                            .setField(MultipleFields.TEXT, text)
                            .setField(MultipleFields.IMAGE_URL, image)
                            .build();
                    ENTITLES.add(entity);
                }
            }
        }
        return ENTITLES;
    }

    public int getDataSize() {
        return size;
    }

}
