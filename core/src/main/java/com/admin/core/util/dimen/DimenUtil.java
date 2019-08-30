package com.admin.core.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.admin.core.app.Latte;

/**
 * Copyright (C)
 *
 * @file: DimenUtil
 * @author: 345
 * @Time: 2019/4/18 16:57
 * @description: 测量
 */
public class DimenUtil {
    /**
     * @return 可用显示大小的绝对宽度(以像素为单位)
     */
    public static int getScreenWidth(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();

        return dm.widthPixels;
    }
    /**
     * @return 可用显示大小的绝对高度(以像素为单位)
     */
    public static int getScreenHeight(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
