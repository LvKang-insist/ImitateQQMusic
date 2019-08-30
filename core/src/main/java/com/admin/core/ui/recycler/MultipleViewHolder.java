package com.admin.core.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Copyright (C)
 *
 * @file: MultipleViewHolder
 * @author: 345
 * @Time: 2019/4/27 17:35
 * @description: RecyclerView 适配器的 holder
 */
public class MultipleViewHolder extends BaseViewHolder {

    private MultipleViewHolder(View view) {
        super(view);
    }

    public static MultipleViewHolder create(View view){
        return new MultipleViewHolder(view);
    }
}
