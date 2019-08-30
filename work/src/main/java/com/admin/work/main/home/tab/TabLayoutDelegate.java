package com.admin.work.main.home.tab;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.ui.view.ForbidSlideViewPager;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.dimen.StatusBarHeight;
import com.admin.work.R;
import com.admin.work.R2;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabLayoutDelegate extends LatteDelegate {

    @BindView(R2.id.home_tab_layout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.home_table_viewpager)
    ForbidSlideViewPager mViewPager = null;
    @BindView(R2.id.home_tab_linearlayout)
    LinearLayoutCompat mLinearLayout = null;

    public static TabLayoutDelegate create() {
        return new TabLayoutDelegate();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_tablayout;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initPager();
        initTab();
        initLayout();
    }

    private void initLayout() {
        CallbackManager.getInstance().addCallback(CallBackType.RECY_COUNT, new IGlobalCallback() {
            @Override
            public void executeCallBack(Object args) {
                mLinearLayout.setMinimumHeight(StatusBarHeight.dip2px((int)args));
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPager() {
        List<String> tabName = new ArrayList<>();
        tabName.add("热门歌单");
        tabName.add("收藏歌单");
        TabPagerAdpter adpter = new TabPagerAdpter(getChildFragmentManager(), tabName);
        mViewPager.setAdapter(adpter);

    }

    private void initTab() {
        //地下线的颜色
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.app_tab));
        //字的颜色
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        //设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
