package com.admin.core.util.image;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.admin.core.app.Latte;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class RoundAngle {


    public static void setRoundAngle(String url, ImageView imageView) {
       /* RequestManager glideRequest;
        glideRequest = Glide.with(Latte.getApplication());

        glideRequest
                .load("https://www.baidu.com/img/bdlogo.png")
                .centerCrop()
                .fitCenter()
                .transform(new GlideRoundTransform(Latte.getApplication(), 10)).into(imageView);*/

      /*  RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(20));//图片圆角为30
        Glide.with(Latte.getApplication())
                .load(url)
                .apply(options)
                .into(imageView);*/


        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
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
                        Latte.getHandler().post(() -> imageView.setBackground(resource));
                        return false;
                    }
                })
                .submit();
    }
}
