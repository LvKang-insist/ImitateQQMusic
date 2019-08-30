package com.admin.work.main.discover.tab;

import com.admin.core.ui.recycler.DataConverter;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

public class TabVideoConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        return ENTITLES;
    }
    public ArrayList<MultipleItemEntity> convert(List<BaseMV> list) {
        for (int i = 0; i < list.size(); i++) {
            MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(DiscoverItemType.DISCOVER_VIDEO)
                    .setField(DiscoverItemFields.BASE_MV,list.get(i))
                    .build();
            ENTITLES.add(entity);
        }
        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(DiscoverItemType.DISCOVER_RECYCLER)
                .setField(MultipleFields.NAME,"24小时")
                .build();
        ENTITLES.add(entity);
        return ENTITLES;
    }
}
