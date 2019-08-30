package com.admin.work.main.discover.tab;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JzViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;
    public JzViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }
    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        Rect selfRect = new Rect(0, 0,
                view.getWidth(),view.getHeight());
        outline.setRoundRect(selfRect,mRadius);
    }

}
