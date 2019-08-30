package com.admin.work.main.home.tab;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.admin.work.main.discover.tab.DiscoverItemType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import java.util.WeakHashMap;

import butterknife.BindView;

public class TabDelegate extends LatteDelegate {

    @BindView(R2.id.home_tab_recyclerview)
    RecyclerView mRecyclerView = null;

    private int mTabCount;
    private TabListConverter converter;

    private TabDelegate(int mTabCount) {
        this.mTabCount = mTabCount;
    }

    public static TabDelegate create(int count) {
        return new TabDelegate(count);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_tab;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        setConverter();
    }


    private void setConverter() {
        if (mTabCount == 0) {
            String url = Resource.getString(R.string.song_list);
            RxRequest.onGetRx(getContext(), url, new WeakHashMap<>(), (flag, result) -> {
                if (flag) {
                    converter = new TabListConverter(0);
                    converter.setJsonData(result);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(manager);
                    TabListAdapter adapter = new TabListAdapter(converter.convert());
                    mRecyclerView.setAdapter(adapter);
                    IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.RECY_COUNT);
                    if (callBack != null) {
                        callBack.executeCallBack(converter.getDataSize() * 80 + 30);
                    }
                }
            });
        } else {
            converter = new TabListConverter(1);
        }
    }
}
