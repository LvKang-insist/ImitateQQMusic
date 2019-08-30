package com.admin.core.ui.banner;

import com.admin.core.R;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @file: BannerCreator
 * @author: 345
 * @Time: 2019/4/27 19:42
 * @description: ${DESCRIPTION}
 */
public class BannerCreator {
    public static void setDefault(ConvenientBanner<String> convenientBanner,
                                  ArrayList<String> banners,
                                  OnItemClickListener clickListener){
        convenientBanner
                .setPages(new HolderCreate(),banners)
                .setPageIndicator(new int[]{R.drawable.dot_focus,R.drawable.dot_normal})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .startTurning(3000)
                .setCanLoop(true);
    }
}
