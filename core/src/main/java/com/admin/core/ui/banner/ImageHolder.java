package com.admin.core.ui.banner;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.admin.core.R;
import com.admin.core.app.Latte;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * Copyright (C)
 *
 * @file: ImageHolder
 * @author: 345
 * @Time: 2019/4/27 19:42
 * @description: ${DESCRIPTION}
 */
public class ImageHolder extends Holder<String> {

    private AppCompatImageView mImageView ;

    public ImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.index_photo);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(Latte.getApplication())
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
//                .fitCenter()
                .into(mImageView);
    }
}
