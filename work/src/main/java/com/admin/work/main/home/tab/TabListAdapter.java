package com.admin.work.main.home.tab;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.app.Latte;
import com.admin.core.ui.recycler.MultipleFields;
import com.admin.core.ui.recycler.MultipleItemEntity;
import com.admin.core.ui.recycler.MultipleRecyclerAdapter;
import com.admin.core.ui.recycler.MultipleViewHolder;
import com.admin.core.util.image.RoundAngle;
import com.admin.work.R;
import com.admin.work.main.home.HomeItemFields;
import com.admin.work.main.home.HomeItemType;
import com.bumptech.glide.Glide;

import java.util.List;

public class TabListAdapter extends MultipleRecyclerAdapter {

    protected TabListAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(HomeItemType.HOME_TAB_LIST, R.layout.item_home_tab_child);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case HomeItemType.HOME_TAB_LIST:
                RoundAngle.setRoundAngle(entity.getField(MultipleFields.IMAGE_URL),
                        holder.getView(R.id.home_tab_child_image));
                holder.setText(R.id.home_table_child_songList,entity.getField(MultipleFields.TEXT));
                LinearLayoutCompat linearLayoutCompat = holder.getView(R.id.home_tab_child);
                linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, entity.getField(HomeItemFields.TEXT1), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "" + entity.getField(HomeItemFields.TEXT1));
                    }
                });
                break;
            default:
                break;
        }
    }
}
