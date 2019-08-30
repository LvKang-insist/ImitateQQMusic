package com.admin.core.ui.recycler;

import java.util.LinkedHashMap;

/**
 * Copyright (C)
 *
 * @file: MultipleltemEntityBuilder
 * @author: 345
 * @Time: 2019/4/27 15:16
 * @description: 构建数据
 */
public class MultipleltemEntityBuilder {
    private static final LinkedHashMap<Object,Object> FIELDS = new LinkedHashMap<>();

     MultipleltemEntityBuilder(){
        // 因为这个map是静态的，所以每次使用时先清除之前的数据
        FIELDS.clear();
    }

    public final MultipleltemEntityBuilder setItemType(int itemType){
        FIELDS.put(MultipleFields.ITEM_TYPE,itemType);
        return this;
    }

    public final MultipleltemEntityBuilder setField(Object key,Object value){
        FIELDS.put(key,value);
        return this;
    }

    public final MultipleltemEntityBuilder setField(LinkedHashMap<?,?> map){
        FIELDS.putAll(map);
        return this;
    }

    public final MultipleItemEntity build(){
        return   new MultipleItemEntity(FIELDS);
    }

}
