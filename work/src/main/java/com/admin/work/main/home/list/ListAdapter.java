package com.admin.work.main.home.list;

import android.content.SharedPreferences;

import com.admin.work.R;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ListAdapter extends BaseMultiItemQuickAdapter<ListBean, BaseViewHolder> {

    public ListAdapter(List<ListBean> data) {
        super(data);
        addItemType(ListItemType.PER_ITEM, R.layout.arraw_item_layout);
        addItemType(ListItemType.SWITCH_ITEM, R.layout.arrow_switch_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {
        switch (helper.getItemViewType()) {
            case ListItemType.PER_ITEM:
                helper.setText(R.id.tv_arrow_text, item.getText());
                helper.setText(R.id.tv_arrow_value, item.getValue());
                break;
            case ListItemType.SWITCH_ITEM:
                helper.setText(R.id.tv_arrow_switch_text, item.getText());
                break;
            default:
                break;
        }
    }
}