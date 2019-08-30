package com.admin.core.ui.loader;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;
import com.admin.core.R;
import com.admin.core.app.Latte;
import com.admin.core.util.dimen.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @file: LatteLoader
 * @author: 345
 * @Time: 2019/4/18 16:47
 * @description:
 */
public class LatteLoader {

    //缩放比，让load根据屏幕的大小 来调整大小。
    private static final int LOADER_SIZE_SCALE = 8;

    //屏幕的偏移量
    private static final int LOADER_OFFSET_SCALT = 10;

    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();

    private static final String DEFULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        Latte.getHandler().post(()->{
            showLoading(context, type.name());
        });
    }

    public static void showLoading(Context context, String type) {
        // 使用 dialog 来承载 Loading .
        // 尽量 使用 v7包下的东西，这样兼容性比较好
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        //通过creawte方法 设置 样式 并返回对象
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.creawte(type, context);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            //设置 dialog 的属性
           final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.alpha = 0.4f;
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            //偏移量，会将上面的 height 个给覆盖掉
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALT;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context) {
        //自定义 一个默认的 加载动画
        showLoading(context, DEFULT_LOADER);
    }

    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    //  dismiss() 只是单纯的消失掉 dialog 而 cancel 会有一些回调，所以使用cancel
                    dialog.cancel();
                }
            }
        }
    }
}
