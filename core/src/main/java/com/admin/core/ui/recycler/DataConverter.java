package com.admin.core.ui.recycler;

import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @file: DataConverter
 * @author: 345
 * @Time: 2019/4/27 14:51
 * @description: ${DESCRIPTION}
 */
public abstract class DataConverter {

    /**
     *  convert 方法解析完数据后 会将结果存进这个集合中
     */
    protected final ArrayList<MultipleItemEntity> ENTITLES = new ArrayList<>();
    private String mJsonData = null;

    /**
     *解析数据
     */
    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String json){
        this.mJsonData = json;
        return this;
    }

    protected String getJsonData(){
        if (mJsonData == null || mJsonData.isEmpty()){
            throw new NullPointerException("DATA IS NULL");
        }
        return mJsonData;
    }
}
