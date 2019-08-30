package com.admin.core.ui.recycler;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.admin.core.R;
import com.admin.core.ui.banner.BannerCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C)
 *
 * @file: MultipleRecyclerAdapter
 * @author: 345
 * @Time: 2019/4/27 17:34
 * @description: RecyclerView 的多类型适配器
 */

public class MultipleRecyclerAdapter extends
        BaseMultiItemQuickAdapter<MultipleItemEntity, MultipleViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup, OnItemClickListener {

    /**
     * 确保初始化一次Banner，防止重复加载
     */
    private boolean mIsInitBanner = false;

    protected MultipleRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        //初始化布局
        init();
    }

    public static MultipleRecyclerAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecyclerAdapter(data);
    }
    public static MultipleRecyclerAdapter create(DataConverter converter) {
        return new MultipleRecyclerAdapter(converter.convert());
    }

    private void init() {
        /*
         * 设置不同的item 布局
         * addItemType 中的type 类型，必须和接收到的类型一模一样。
         * 种类：有几种type ，就需要写几个addItemType，少些或者错写都会直接报错
         * 报错类型(javax.net.ssl.SSLHandshakeException: SSL handshake aborted: ......)
         */
        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_image_text);
        addItemType(ItemType.BANNER, R.layout.item_multiple_banner);
        //设置 宽度监听
        setSpanSizeLookup(this);
        //加载时打开动画
        openLoadAnimation();
        //多次执行动画
        isFirstOnly(false);
    }

    /**
     * 如果你想在适配器中使用BaseViewHolder的子类，您必须重写该方法来创建新的ViewHolder。
     */
    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    /**
     * 现此方法，并使用helper将视图调整为给定项
     */
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        final String text;
        final String imageUrl;
        final ArrayList<String> bannerImages;
        //根据不同的 type 设置不同的数据
        switch (holder.getItemViewType()) {
            case ItemType.TEXT:
                text = entity.getField(MultipleFields.TEXT);
                holder.setText(R.id.text_single, text);
                break;
            case ItemType.IMAGE:
                imageUrl = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(imageUrl)
                        /*
                         * 图片的缓存：
                         * DiskCacheStrategy.NONE 什么都不缓存
                         * DiskCacheStrategy.SOURCE 只缓存全尺寸图
                         * DiskCacheStrategy.RESULT 只缓存最终的加载图
                         * DiskCacheStrategy.ALL 缓存所有版本图（默认行为）
                         */
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        //将他图片按比例缩放到足以填充imageView 的尺寸,但是图片可能显示不完整
                        .centerCrop()
                        //将图片缩放到小于等于imageView的尺寸，这样图片会完整显示但是 imageView 就可能填不满了
                        .fitCenter()
                        .into((ImageView) holder.getView(R.id.img_single));
                break;
            case ItemType.TEXT_IMAGE:
                text = entity.getField(MultipleFields.TEXT);
                imageUrl = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .into((ImageView) holder.getView(R.id.img_single));
                holder.setText(R.id.tv_multiple,text);
                break;
            case ItemType.BANNER:
                if (!mIsInitBanner){
                    bannerImages = entity.getField(MultipleFields.BANNERS);
                    final ConvenientBanner<String> convenientBanner = holder.getView(R.id.banner_recycler_item);
                    BannerCreator.setDefault(convenientBanner,bannerImages,this);
                    mIsInitBanner = true;
                }
                break;
            default:
                break;
        }
    }
    /**
     * 设置宽度
     */
    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return getData().get(position).getField(MultipleFields.SPAN_SIZE);
    }

    @Override
    public void onItemClick(int position) {
    }
}
