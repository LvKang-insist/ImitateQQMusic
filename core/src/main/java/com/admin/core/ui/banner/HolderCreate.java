package com.admin.core.ui.banner;

import android.view.View;

import com.admin.core.R;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Copyright (C)
 *
 * @file: HolderCreate
 * @author: 345
 * @Time: 2019/4/27 19:42
 * @description: ${DESCRIPTION}
 */
public class HolderCreate implements CBViewHolderCreator {
    @Override
    public Holder createHolder(View itemView) {
        return new ImageHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.index_photo;
    }
}
