package com.admin.work.main.music_more;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.admin.core.app.Latte;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.image.RoundAngle;
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

public class PagerDelegate extends LatteDelegate {

    @BindView(R2.id.item_image)
    ImageView mImageview = null;

    private String url;

    PagerDelegate(String url) {
        this.url = url;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_image;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        RoundAngle.setRoundAngle(url,mImageview);
    }

}
