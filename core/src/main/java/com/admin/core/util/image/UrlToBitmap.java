package com.admin.core.util.image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.admin.core.app.Latte;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class UrlToBitmap {

    public interface onBitmapListener{
        void onBitmap(Bitmap bitmap);
    }

    public static void urlToBitmap(String imageUrl, onBitmapListener listener) {
        Glide.with(Latte.getApplication())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource != null) {
                            BitmapDrawable bd = (BitmapDrawable) resource;
                            Bitmap bm = bd.getBitmap();
                            if (listener != null) {
                                listener.onBitmap(bm);
                            }
                        } else {
                            listener.onBitmap(null);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
