package com.admin.work.main.discover.tab;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.admin.core.app.Latte;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.work.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class ItemRecyclerAdapter extends MultipleRecyclerAdapter {
    RoundedCorners roundedCorners = new RoundedCorners(10);
    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

    protected ItemRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(DiscoverItemType.DISCOVER_RECYCLER_ITEM, R.layout.item_dis_tab_recycler_item);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        if (holder.getItemViewType() == DiscoverItemType.DISCOVER_RECYCLER_ITEM) {
            holder.setText(R.id.item_dis_tab_recycler_name, entity.getField(MultipleFields.NAME));
            holder.setText(R.id.item_dis_tab_recycler_text, entity.getField(MultipleFields.TEXT));
            //圆角图片
            String url = entity.getField(MultipleFields.IMAGE_URL);

            Glide.with(Latte.getApplication())
                    .load(url)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Latte.getHandler().post(() -> {
                                AppCompatImageView view = holder.getView(R.id.item_dis_tab_recycler_phone);
                                view.setBackground(resource);
                            });
                            return false;
                        }
                    })
                    .submit();
        }
    }
}
