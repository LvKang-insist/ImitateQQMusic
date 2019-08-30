package com.admin.core.ui.launcher;

import android.view.View;

import com.admin.core.R;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;


/**
 * Copyright (C)
 *
 * @file: LauncherHolderCreator
 * @author: 345
 * @Time: 2019/4/21 15:15
 * @description: ${DESCRIPTION}
 */
public class LauncherHolderCreator implements CBViewHolderCreator{
    /**
     * 加载 布局
     */
    @Override
    public Holder createHolder(View itemView) {
        return new LauncherHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        //设置 加载的布局
        return R.layout.launcher_icon;
    }
}
