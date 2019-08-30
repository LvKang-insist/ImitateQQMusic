package com.admin.core.deleggate.bottom;

import android.widget.Toast;

import com.admin.core.R;
import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;

/**
 * Copyright (C)
 *
 * @file: BottomItemDelegate
 * @author: 345
 * @Time: 2019/4/25 19:26
 * @description: 导航栏对应的 页面
 */
public abstract class BottomItemDelegate extends LatteDelegate {

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "双击退出" + Latte.getApplication().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
