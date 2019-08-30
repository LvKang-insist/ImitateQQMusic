package com.admin.core.ui.recycler;


import androidx.annotation.ColorInt;

import com.choices.divider.DividerItemDecoration;

/**
 * Copyright (C)
 *
 * @file: BaseDecoration
 * @author: 345
 * @Time: 2019/4/28 19:46
 * @description: RecyclerView 分割线
 */
public class BaseDecoration extends DividerItemDecoration {

    private BaseDecoration(@ColorInt int color, int size) {
        setDividerLookup(new DividerLookupImpl(color,size));
    }

    public static BaseDecoration create(@ColorInt int color ,int size){
       return new BaseDecoration(color,size);
    }
}
