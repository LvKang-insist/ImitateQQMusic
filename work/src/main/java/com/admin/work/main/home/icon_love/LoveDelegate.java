package com.admin.work.main.home.icon_love;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.admin.core.app.MusicManager;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.player.nativemusic.Song;
import com.google.android.material.tabs.TabLayout;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LoveDelegate extends LatteDelegate {

    @BindView(R2.id.dele_love_tablayout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.dele_love_viewpager)
    ViewPager mViewPager = null;

    @BindView(R2.id.toolbar_first_back)
    IconTextView mIconBack = null;
    @BindView(R2.id.toolbar_first_textview)
    AppCompatTextView mTextName = null;
    @BindView(R2.id.toolbar_first_more)
    IconTextView mIconMore = null;

    @OnClick(R2.id.toolbar_first_back)
    void onBackClick(){
        getSupportDelegate().pop();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_love;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initViewPager();
        initTabLayout();
        mTextName.setText("我喜欢");
    }

    private void initViewPager() {
        List<String > list = new ArrayList<>();
        list.add("歌曲");
        list.add("专辑");
        list.add("歌单");
        list.add("视频");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),list);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        //地下线的颜色
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.app_tab));
        //字的颜色
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        //设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }
    /**
     * 此方法会读取当前播放的歌曲信息，并更新item
     */

    @Override
    public boolean onBackPressedSupport() {
        IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
        if (callBack != null) {
            callBack.executeCallBack(null);
        }
        getSupportDelegate().pop();
        return true;
    }
}
