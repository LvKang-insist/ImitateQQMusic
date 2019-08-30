package com.admin.core.ui.launcher;

import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.admin.core.R;
import com.bigkoo.convenientbanner.holder.Holder;


/**
 * Copyright (C)
 *
 * @file: LauncherHolder
 * @author: 345
 * @Time: 2019/4/21 15:15
 * @description: ${DESCRIPTION}
 */
public class LauncherHolder extends Holder<Integer> {

    private AppCompatImageView mImageView ;

    public LauncherHolder(View itemView) {
        super(itemView);
    }

    /**
     * 加载布局
     */
    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.launcher_image);
    }
    /**
     * 绑定数据
     */
    @Override
    public void updateUI(Integer data) {
        mImageView.setBackgroundResource(data);
    }
}
