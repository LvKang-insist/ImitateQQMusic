package com.admin.work.main.discover.tab;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.admin.work.R2;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class TabVideoDelegate extends LatteDelegate {

    @BindView(R2.id.discover_tab_recyclerview)
    RecyclerView mRecyclerView = null;
    private RecyclerAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_discover_tab;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        setVideo();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setVideo() {
        WeakHashMap<String, Object> weakHashMap = new WeakHashMap();
        weakHashMap.put("pageSize", 30);
        weakHashMap.put("page", 2);
        RxRequest.onGetRx(getContext(), Resource.getString(R.string.mv_top), weakHashMap, (flag, result) -> {
            if (flag) {
                List<BaseMV> list = new ArrayList<>();
                JSONObject json = JSON.parseObject(result);
                if (json.getInteger("code") == 200) {
                    JSONArray array = json.getJSONArray("data");
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        BaseMV mv = new BaseMV();
                        mv.setArtistName(object.getString("artistName"));
                        mv.setArtistId(object.getInteger("artistId"));
                        mv.setMvId(object.getInteger("id"));
                        mv.setName(object.getString("name"));
                        mv.setPlayCount(object.getInteger("playCount"));
                        mv.setCoverUrl(object.getString("cover"));

                        RxRequest.onGetRx(getContext(),Resource.getString(R.string.mv_message),
                                new String[]{"id"}, new Object[]{mv.getMvId()}, ((f, res) -> {
                                    if (f) {
                                        //判断是否超时
                                        if (res.equals("timeout")) {
                                            return;
                                        }
                                        JSONObject js = JSON.parseObject(res);
                                        if (js.getInteger("code") == 200) {
                                            JSONObject data = js.getJSONObject("data").getJSONObject("data");
                                            mv.setDesc(data.getString("desc"));
                                            mv.setMvUrl(Resource.getString(R.string.mv_url) + "?id=" + mv.getMvId());
                                            Log.e("mv 地址", "setVideo: " + mv.getMvUrl());
                                        }
                                    }
                                }));

                        list.add(mv);
                    }
                    TabVideoConverter converter = new TabVideoConverter();
                    mAdapter = new RecyclerAdapter(converter.convert(list), this);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(manager);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.e("rxjava 获取mv数据 失败", "onNext: " + result);
                }
            }
        });
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                //当视图离开时
                JzvdStd jzvd = view.findViewById(R.id.item_dis_tab_video_jz);
                if (jzvd != null && jzvd.state == Jzvd.STATE_PLAYING) {
                    //释放资源
                    JzvdStd.releaseAllVideos();
                }
            }
        });
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        Jzvd.releaseAllVideos();
    }
}
