package com.admin.work.main.music_more;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.admin.core.deleggate.bottom.BottomItemDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.image.RoundAngle;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.search.SearchDelegate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.WeakHashMap;

import butterknife.BindView;

/**
 * Copyright (C)
 *
 * @file: MusicDelegate
 * @author: 345
 * @Time: 2019/5/8 10:13
 * @description:
 */
public class MusicDelegate extends BottomItemDelegate {

    @BindView(R2.id.toolbar_textview)
    AppCompatTextView mToolbarText = null;
    @BindView(R2.id.toolbar_icon)
    IconTextView mIconText = null;
    @BindView(R2.id.more_recyclerview)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText editText;

    @BindView(R2.id.dele_more_viewapager)
    ViewPager mViewPagerBanner = null;
    @BindView(R2.id.dele_more_swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh = null;

    @BindView(R2.id.item_more_relativeOne)
    RelativeLayout rlvOne = null;
    @BindView(R2.id.item_more_relativeTwo)
    RelativeLayout rlvTwo = null;
    @BindView(R2.id.item_more_pthotoOne)
    AppCompatImageView mImageOne = null;
    @BindView(R2.id.item_more_pthotoTwo)
    AppCompatImageView mImageTwo = null;
    @BindView(R2.id.item_more_nameOne)
    AppCompatTextView nameOne = null;
    @BindView(R2.id.item_more_nameTwo)
    AppCompatTextView nameTwo = null;
    @BindView(R2.id.item_more_contentOne)
    AppCompatTextView contentOne = null;
    @BindView(R2.id.item_more_contentTwo)
    AppCompatTextView contentTwo = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_more;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initRecycler();
        initViewPageBanner();
        mSwipeRefresh.setColorSchemeResources(R.color.app_music_green);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecycler();
                initViewPageBanner();
                mSwipeRefresh.setRefreshing(false);
            }
        });
        editText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                //加载BOTTOM动画
                IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.BOTTOM);
                if (callBack != null) {
                    callBack.executeCallBack(null);
                }
                //搜索页面
                getParentDelegate(). getSupportDelegate().start(new SearchDelegate());
            }
        });
    }

    private void initViewPageBanner() {
        RxRequest.onGetRx(getContext(), Resource.getString(R.string.banner_163),
                new WeakHashMap<>(), ((flag, result) -> {
                    ArrayList<String> list = new ArrayList<>();
                    if (flag) {
                        JSONObject json = JSON.parseObject(result);
                        if (json.getInteger("code") == 200) {
                            JSONArray data = json.getJSONArray("data");
                            for (int i = 0; i < data.size(); i++) {
                                String picUrl = data.getJSONObject(i).getString("picUrl");
                                list.add(picUrl);
                            }
                            MorePagerAdapter adapter = new MorePagerAdapter(getChildFragmentManager(),list);
                            mViewPagerBanner.setAdapter(adapter);
                            mViewPagerBanner.setPageMargin(30);
                        }
                    }
                }));

        rlvOne.setBackgroundResource(R.drawable.back_green);
        rlvTwo.setBackgroundResource(R.drawable.back_yellow);
        RoundAngle.setRoundAngle("http://222.186.12.239:10010/xjt_170330/001.jpg", mImageOne);
        RoundAngle.setRoundAngle("http://pic1.win4000.com/mobile/0/55c17fc909f79.jpg", mImageTwo);
        nameOne.setText("新歌新碟");
        contentOne.setText("萧敬腾神hook...");
        nameTwo.setText("数字专辑·事务");
        contentTwo.setText("比比诠释时代女...");
    }

    private void initRecycler() {
        mToolbarText.setText("音乐馆");
        RxRequest.onGetRx(getContext(), Resource.getString(R.string.music_list)
                , new WeakHashMap<>(), (f, re) -> {
                    if (f) {
                        MoreRecyclerConverter converter = new MoreRecyclerConverter();
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(manager);
                        converter.setJsonData(re);
                        MoreRecyclerAdapter adapter = new MoreRecyclerAdapter(converter.convert(), this);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

    }
}
