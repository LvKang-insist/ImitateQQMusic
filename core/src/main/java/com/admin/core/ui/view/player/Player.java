package com.admin.core.ui.view.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.R;
import com.admin.core.util.dimen.DimenUtil;
import com.admin.core.util.dimen.StatusBarHeight;

import java.util.Map;


/**
 * @author Lv
 * Created at 2019/7/6
 */
public class Player extends View {
    private Paint mPaint;
    //进度
    private int mProgress;

    //圆边的厚度
    private int ply = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

    //记录坐标
    RectF oval;
    Rect mRect;

    // 画线
    Path mPath;

    //是否播放
    boolean isPlay = false;

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public Player(Context context) {
        this(context, null);
    }

    public Player(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Player(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        int df = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = df;
        }
        //高
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = df;
        }
        setMeasuredDimension(width, height);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        int widht = getMeasuredWidth();
        int height = getMeasuredHeight();
        //圆心的位置
        int core = widht / 2;
        //圆的半径
        int r = (height / 2) - ply;
        //内圆的边
        int circleRect = ply * 2;

        if (oval == null) {
            oval = new RectF(circleRect, circleRect, widht - circleRect, height - circleRect);
        }
        //外圆
        mPaint.setColor(getResources().getColor(R.color.app_music_green, null));
        mPaint.setStrokeWidth(ply);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(core, core, r, mPaint);

        //内圆
        mPaint.setColor(getResources().getColor(R.color.app_music_green, null));
        canvas.drawArc(oval, -90, mProgress, false, mPaint);

        canvas.save();
        if (isPlay) {
            stopPlay(canvas);
        } else {
            startPlay(canvas);
        }
    }

    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
        invalidate();
    }

    public boolean isPlay() {
        return this.isPlay;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void stopPlay(Canvas canvas) {
        mPaint.reset();
        mPath.reset();

        mPaint.setColor(getResources().getColor(R.color.app_music_green, null));
        mPaint.setStyle(Paint.Style.STROKE);//描边 加 填充
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧

        int left = getMeasuredWidth() / 2 - (ply*2);
        int top = getMeasuredHeight() / 2 - (ply*2);
        int right = getMeasuredWidth() / 2 + (ply*2);
        int bottom = getMeasuredWidth() / 2 + (ply*2);
        mPath.moveTo(left, top);
        mPath.lineTo(left, bottom);
        mPath.moveTo(right, top);
        mPath.lineTo(right, bottom);
        canvas.drawPath(mPath, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startPlay(Canvas canvas) {
        mPaint.reset();
        mPath.reset();

        mPaint.setColor(getResources().getColor(R.color.app_music_green, null));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);//描边 加 填充
        mPaint.setStrokeWidth(ply);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧


        int left = getMeasuredWidth() / 2 - (ply*2);
        int top = getMeasuredHeight() / 2 - (ply*2);
        int right = getMeasuredWidth() / 2 + (ply*2);
        int bottom = getMeasuredWidth() / 2 + (ply*2);

        mPath.moveTo(left + 5, top);
        mPath.lineTo(right + 5, getMeasuredHeight() / 2);
        mPath.lineTo(left + 5, bottom);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
