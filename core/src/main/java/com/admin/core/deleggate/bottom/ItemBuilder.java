package com.admin.core.deleggate.bottom;

import java.util.LinkedHashMap;

/**
 * Copyright (C)
 *
 * @file: ItemBuilder
 * @author: 345
 * @Time: 2019/4/25 19:42
 * @description: 工厂类，用来构建 导航栏 和 碎片
 */
public final class ItemBuilder {

    private final LinkedHashMap<BottomTabBean,BottomItemDelegate> ITEMS = new LinkedHashMap<>();

    static ItemBuilder builder(){
        return new ItemBuilder();
    }

    public final ItemBuilder addItem(BottomTabBean bean,BottomItemDelegate delegate){
        ITEMS.put(bean,delegate);
        return this;
    }

    public final ItemBuilder addItem(LinkedHashMap<BottomTabBean,BottomItemDelegate> items){
        ITEMS.putAll(items);
        return this;
    }

    public final LinkedHashMap<BottomTabBean,BottomItemDelegate> build(){
        return ITEMS;
    }
}
