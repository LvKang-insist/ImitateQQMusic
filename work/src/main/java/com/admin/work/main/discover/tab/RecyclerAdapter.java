package com.admin.work.main.discover.tab;

import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.callback.IFailure;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.List;


import cn.jzvd.JzvdStd;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RecyclerAdapter extends MultipleRecyclerAdapter {

    LatteDelegate mTabDelegate;
    JzViewOutlineProvider provider = new JzViewOutlineProvider(20);

    protected RecyclerAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        mTabDelegate = delegate;
        addItemType(DiscoverItemType.DISCOVER_VIDEO, R.layout.item_dis_tab_video);
        addItemType(DiscoverItemType.DISCOVER_RECYCLER, R.layout.item_recycler);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case DiscoverItemType.DISCOVER_VIDEO:
                BaseMV baseMV = entity.getField(DiscoverItemFields.BASE_MV);
                Glide.with(mTabDelegate.getContext())
                        .load(baseMV.getCoverUrl())
                        .into((ImageView) holder.getView(R.id.item_dis_tab_video_photo));
                //姓名和内容
                holder.setText(R.id.item_dis_tab_video_name, baseMV.getName());
                holder.setText(R.id.item_dis_tab_video_text, baseMV.getDesc());
                //设置视频
                JzvdStd jzvdStd = holder.getView(R.id.item_dis_tab_video_jz);
                jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //视频设置圆角
                jzvdStd.setOutlineProvider(provider);
                jzvdStd.setClipToOutline(true);
                jzvdStd.setUp(baseMV.getMvUrl(), "");
                Glide.with(mTabDelegate.getContext())
                        .load(baseMV.getCoverUrl())
                        .into(jzvdStd.thumbImageView);
                break;
            case DiscoverItemType.DISCOVER_RECYCLER:
                holder.setText(R.id.item_recycler_name, entity.getField(MultipleFields.TITLE));
                String json = entity.getField(MultipleFields.TEXT);
                if (json != null) {
                    RecyclerView recycler = holder.getView(R.id.item_recycler);
                    recycler.setNestedScrollingEnabled(false);
                    LinearLayoutManager manager = new LinearLayoutManager(mTabDelegate.getContext(),
                            RecyclerView.HORIZONTAL, false);
                    recycler.setLayoutManager(manager);
                    ItemRecyclerConverter converter = new ItemRecyclerConverter();
                    converter.setJsonData(json);
                    ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(converter.convert());
                    recycler.setAdapter(adapter);
                }
                break;
            default:
        }
    }
}
