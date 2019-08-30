package com.admin.core.ui.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.R;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @file: StarLayout
 * @author: 345
 * @Time: 2019/5/12 11:24
 * @description: ${DESCRIPTION}
 */
public class StarLayout extends LinearLayoutCompat implements View.OnClickListener {

    private static final CharSequence ICON_UN_SELECT = "{fa-star-o}";
    private static final CharSequence ICON_SELECTED = "{fa-star}";

    private static final int STAR_TOTAL_COUNT = 5;
    private static final ArrayList<IconTextView> STARS = new ArrayList<>();

    public StarLayout(Context context) {
        this(context, null);
    }

    public StarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStarIcon();
    }

    /**
     * 初始化 图标
     */
    private void initStarIcon() {
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            final IconTextView start = new IconTextView(getContext());
            start.setGravity(Gravity.CENTER);
            final LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            start.setLayoutParams(lp);
            start.setText(ICON_UN_SELECT);
            start.setTag(R.id.star_count, i);
            start.setTag(R.id.star_is_selected, false);
            start.setOnClickListener(this);
            STARS.add(start);
            //将 Icon布局 传入 当前的布局中中
            this.addView(start);
            start.setId(R.id.star_count);
        }
    }

    private void selectStar(int count) {
        for (int i = 0; i <= count; i++) {
            final IconTextView start = STARS.get(i);
            start.setText(ICON_SELECTED);
            start.setTextColor(Color.RED);
            start.setTag(R.id.star_is_selected, true);
        }
    }
    private void unSelectStar(int count){
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            if (i > count){
                final IconTextView start = STARS.get(i);
                start.setText(ICON_UN_SELECT);
                start.setTextColor(Color.GRAY);
                start.setTag(R.id.star_is_selected, false);
            }
        }
    }

    public int getStarCount(){
        int count = 0;
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            final IconTextView star = STARS.get(i);
            final boolean isSelect = (boolean) star.getTag(R.id.star_is_selected);
            if (isSelect){
                count++;
            }
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        final IconTextView star = (IconTextView) v;
        //获取第几个星星
        final int count = (int) star.getTag(R.id.star_count);
        final boolean isSelect = (boolean) star.getTag(R.id.star_is_selected);
        if (!isSelect) {
            selectStar(count);
        }else {
            unSelectStar(count);
        }
    }
}
