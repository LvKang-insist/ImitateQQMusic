package com.admin.work.main.music_more;

import android.util.Log;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoreRecyclerConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        JSONObject json = JSON.parseObject(getJsonData());
        if (json.getInteger("code") == 200 && json.getString("message").equals("成功!")) {
            JSONArray array = json.getJSONArray("result");
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(MoreItemType.MORE_RECYCLER)
                        .setField(MoreItemFields.NAME, name)
                        .setField(MoreItemFields.TYPE, i)
                        .setField(MoreItemFields.JSON, object.toJSONString())
                        .build();
                ENTITLES.add(entity);
            }
        }
        return ENTITLES;
    }

}
