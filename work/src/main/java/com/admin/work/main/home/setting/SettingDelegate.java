package com.admin.work.main.home.setting;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.dimen.SetToolBar;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.home.list.ListAdapter;
import com.admin.work.main.home.list.ListBean;
import com.admin.work.main.home.list.ListItemType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SettingDelegate extends LatteDelegate {

    @BindView(R2.id.settings_toolbar)
    Toolbar mToolbar = null;
    @BindView(R2.id.rv_settings)
    RecyclerView mRecyclerView = null;

    private ListAdapter adapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_setting;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        SetToolBar.setToolBar(mToolbar);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        setting();
    }

    private void setting() {
        ListBean about = new ListBean.Builder()
                .setmItemType(ListItemType.PER_ITEM)
                .setmId(4)
                .setmText("退出登录")
                .build();

        List<ListBean> list = new ArrayList<>();
        list.add(about);

        adapter = new ListAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new SettingOnclickListener(getContext(),this));
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        return true;
    }
}
