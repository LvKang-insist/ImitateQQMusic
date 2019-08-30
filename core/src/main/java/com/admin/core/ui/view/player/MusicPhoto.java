package com.admin.core.ui.view.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.DrawableUtils;

import com.admin.core.R;

public class MusicPhoto extends AppCompatImageView {

    private Bitmap mBitmap;
    private Paint mPaint;
    private BitmapShader mShader;
    private Matrix mMatrix;

    public MusicPhoto(Context context) {
        this(context, null);
    }

    public MusicPhoto(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
    public MusicPhoto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("CustomViewStyleable")
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.music_photo, defStyleAttr, 0);
        mBitmap = BitmapFactory.decodeResource(getResources(),
                array.getResourceId(R.styleable.music_photo_src, 0));

        mPaint = new Paint();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        Drawable drawable = getDrawable();
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        invalidate();
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        Drawable drawable = getDrawable();
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        invalidate();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        Drawable drawable = getDrawable();
        mBitmap = ((BitmapDrawable) drawable).getBitmap();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         *  设置宽度
         */
        int mWidth = 0, mHeight = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        //是否为固定值，或者math
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                if (mBitmap != null) {
                    mWidth = Math.min(mBitmap.getWidth() + getPaddingLeft() + getPaddingRight(), widthSize);
                } else {
                    mWidth = widthSize;
                }
            }
        }
        /*
         *  设置高度
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
                if (mBitmap != null) {
                    int desire = getPaddingTop() + getPaddingBottom() + mBitmap.getHeight();
                    mHeight = Math.min(desire, heightSize);
                } else {
                    mHeight = heightSize;
                }
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int widht = getMeasuredWidth();
        int height = getMeasuredHeight();

        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setStrokeWidth(15);
        mPaint.setStyle(Paint.Style.STROKE);

        int r = widht / 2 - 15;
        canvas.drawCircle(height / 2, height / 2, r, mPaint);
        if (mShader == null) {
            mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

        if (mBitmap != null) {
            mPaint.reset();
            //内圆半径
            int br = widht / 2 - 20;
            float scale = (br * 2.0f) / Math.min(mBitmap.getHeight(), mBitmap.getWidth());
            if (mMatrix == null) {
                mMatrix = new Matrix();
            }
            mMatrix.setScale(scale, scale);
            mShader.setLocalMatrix(mMatrix);
            mPaint.setShader(mShader);
            canvas.drawCircle(height / 2, height / 2, br, mPaint);
        }

    }
}
