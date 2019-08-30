package com.admin.work.main.discover;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.work.R;
import com.admin.work.R2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;

public class PagerBannerDelegate extends LatteDelegate {

    @BindView(R2.id.item_image)
    AppCompatImageView mImageView = null;

    private  String mImageUrl ;


    public PagerBannerDelegate(String mImageUrl){
        this.mImageUrl = mImageUrl;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //圆角图片
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(Latte.getApplication())
                .load(mImageUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        getActivity().runOnUiThread(() -> mImageView.setBackground(resource));
                        return false;
                    }
                })
                .submit();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_image;
    }
}
