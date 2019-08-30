package com.admin.work.main.music_more;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.net.rx.RxRequest;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.dimen.SetToolBar;
import com.admin.core.util.image.RoundAngle;
import com.admin.core.util.value.Resource;
import com.admin.work.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import static com.admin.work.R.drawable.back_green;

public class MoreRecyclerAdapter extends MultipleRecyclerAdapter {

    LatteDelegate mDelegate;

    protected MoreRecyclerAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        this.mDelegate = delegate;
        addItemType(MoreItemType.MORE_RECYCLER, R.layout.item_recycler);
        addItemType(MoreItemType.MORE_RECYCLER_ONE, R.layout.item_more_recycler_item_one);
        addItemType(MoreItemType.MORE_RECYCLER_TWO, R.layout.item_more_recycler_item_two);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {

            case MoreItemType.MORE_RECYCLER:
                holder.setText(R.id.item_recycler_name, entity.getField(MoreItemFields.NAME));
                String json = entity.getField(MoreItemFields.JSON);
                int r_type = entity.getField(MoreItemFields.TYPE);
                LinearLayoutCompat linearlayout = holder.getView(R.id.item_recycler_more);
                linearlayout.setVisibility(View.VISIBLE);
                linearlayout.setOnClickListener((view -> Toast.makeText(mDelegate.getContext(), "更多", Toast.LENGTH_SHORT).show()));
                RecyclerView recyclerView = holder.getView(R.id.item_recycler);
                LinearLayoutManager manager = new LinearLayoutManager(mDelegate.getContext(), RecyclerView.HORIZONTAL, false);
                manager.setInitialPrefetchItemCount(4);
                recyclerView.setLayoutManager(manager);
                recyclerView.setItemViewCacheSize(200);
                ItemRecyclerConverter converter = new ItemRecyclerConverter();
                converter.setJsonData(json);
                if (r_type == 2) {
                    recyclerView.setAdapter(new MoreRecyclerAdapter(converter.convert(MoreItemType.MORE_RECYCLER_TWO), mDelegate));
                } else {
                    recyclerView.setAdapter(new MoreRecyclerAdapter(converter.convert(), mDelegate));
                }
                break;
            case MoreItemType.MORE_RECYCLER_ONE:
                holder.setText(R.id.item_more_recycler_one_text, entity.getField(MultipleFields.TEXT));
                RoundAngle.setRoundAngle(entity.getField(MultipleFields.IMAGE_URL),
                        holder.getView(R.id.item_more_recycler_one_photo));
                break;
            case MoreItemType.MORE_RECYCLER_TWO:
                holder.setText(R.id.item_more_recycler_two_text, entity.getField(MultipleFields.TEXT));
                RoundAngle.setRoundAngle(entity.getField(MultipleFields.IMAGE_URL),
                        holder.getView(R.id.item_more_recycler_two_photo));
                break;
            default:
        }
    }

}
