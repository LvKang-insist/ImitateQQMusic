package com.admin.work.launcher;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.admin.core.ui.launcher.LauncherHolderCreator;
import com.admin.core.ui.launcher.ScrollLauncherTag;
import com.admin.core.util.storage.LattePreference;
import com.admin.work.R;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @file: LauncherScrollDelegate
 * @author: 345
 * @Time: 2019/4/21 15:01
 * @description: 启动时候的轮播图
 */
public class LauncherScrollDelegate extends BaseLauncherDelegate implements OnItemClickListener {

    //泛型为资源文件，也就是图片
    private ConvenientBanner<Integer> mConvenientBanner = null;
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private void initBanner() {
        INTEGERS.add(R.mipmap.l1);
        INTEGERS.add(R.mipmap.l2);
        INTEGERS.add(R.mipmap.l3);
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
                //设置 下面的 圆圈
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                //设置 圆圈的 位置
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                //添加 监听
                .setOnItemClickListener(this)
                //设置 循环 轮播
                .setCanLoop(false);
    }

    @Override
    public Object setLayout() {
        mConvenientBanner = new ConvenientBanner<>(getContext());
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initBanner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INTEGERS.clear();
    }

    @Override
    public void onItemClick(int position) {
        //如果点击的是最后一个
        if (position == INTEGERS.size() - 1) {
            LattePreference.setAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
            //检查的用户是否已经登录
            checkSignIn();
        }
    }
}
