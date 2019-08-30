package com.admin.core.deleggate.bottom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.admin.core.R;
import com.admin.core.R2;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Copyright (C)
 *
 * @file: BaseBottomDelegate
 * @author: 345
 * @Time: 2019/4/25 19:24
 * @description: 对所有的键值对进行管理，也就是 碎片和tab，这是一个抽象类。
 */
public abstract class BaseBottomDelegate extends LatteDelegate implements View.OnClickListener {

    /**
     * 存储所有的子 Fragment
     */
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    /**
     * 存储所有的子 TabBean
     */
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();

    /**
     * 存储 Fragment和TabBean 的映射
     */
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();

    /**
     * 当前Fragment 的位置
     */
    private int mCurrentDelegate = 0;
    /**
     * 进入程序展示 的Fragment
     */
    private int mIndexDelegate = 0;
    /**
     * Tab 的颜色
     */
    private int mClickedColor = Color.RED;

    /**
     * 底部 tab
     */
    @BindView(R2.id.bottom_bar)
    LinearLayoutCompat mBottomBar = null;
    /**
     * Bottom 的标志
     */
    boolean isBottomAnim = false;
    /**
     * 线
     */
    @BindView(R2.id.bottom_view)
    View mView = null;

    @BindView(R2.id.bottom_bar_player)
    LinearLayout mLinearLayout = null;


    /**
     * @param builder 要添加的 映射
     * @return 返回值为 LinkedHashMap
     */
    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

    public abstract int setIndexDelegate();

    /**
     * 该注解表示 这必须是一个颜色
     */
    @ColorInt
    public abstract int setClickedColor();

    public abstract void onPlayer(LinearLayout mLinearLayout);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor();
        }
        //拿到工厂类的实例
        final ItemBuilder builder = ItemBuilder.builder();
        //获取 添加完成的键值对
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);
        //将 键值对 保存在ITEMS 中
        ITEMS.putAll(items);
        //拿到键和值
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //加载音乐控制
        onPlayer(mLinearLayout);
        final int size = ITEMS.size();
        for (int i = 0; i < size; i++) {
            //第一个参数 布局，第二个参数 为给第一个参数加载的布局 设置一个父布局
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_item_icon_text_layout, mBottomBar);
            //返回指定的视图
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置每个 item的点击事件 和标记
            item.setTag(i);
            item.setOnClickListener(this);
            //拿到 item 的第一个和 第二个子布局
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);

            //获取 集合中对应的 Tab
            final BottomTabBean bean = TAB_BEANS.get(i);

            //初始化 tab 数据
            itemIcon.setText(bean.getIcon());
            itemTitle.setText(bean.getTitle());
            //判断是否是 当前显示
            if (i == mIndexDelegate) {
                itemIcon.setTextColor(mClickedColor);
                itemTitle.setTextColor(mClickedColor);
            }
        }
        //返回一个数组，里边是fragment的。注意fragment 是继承 supportFragment 的，所以这里的集合是这个类型
        //fragmentation 需要我们这样做
        final ISupportFragment[] delegateArry = ITEM_DELEGATES.toArray(new ISupportFragment[size]);
        //加载多个同级根fragment,位于当前fragment中(也就是说加载的fragment是子fragment)，
        // 并显示其中一个，第二个参数为要显示的fragment
        getSupportDelegate().loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, delegateArry);
        mCurrentDelegate = mIndexDelegate;

        // 监听动画
        setBottomAnimate();
    }

    /**
     * 重置所有颜色
     */
    private void resetColor() {
        //拿到 底部tab的子布局的size
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            itemIcon.setTextColor(Color.GRAY);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
        itemIcon.setTextColor(mClickedColor);
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(mClickedColor);

        //第一次参数 为要显示的，第二个则是要隐藏的
        getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        // 记住当前显示 的下标，注意顺序
        mCurrentDelegate = tag;
    }

    /**
     * Bottom 的动画
     */
    public void setBottomAnimate() {
        Log.e("---------------", "setBottomAnimate: 加载动画");
        CallbackManager.getInstance().addCallback(CallBackType.BOTTOM, (args -> {
            int width = mBottomBar.getMeasuredWidth();
            int height = mBottomBar.getMeasuredHeight();
            if (isBottomAnim) {
                ViewPropertyAnimator animate = mLinearLayout.animate();
                animate.translationYBy(-height).setDuration(500);
                animate.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLinearLayout.getLayoutParams();
                        params.topMargin = params.topMargin + ((mLinearLayout.getMeasuredHeight() + 30));
                        mLinearLayout.setLayoutParams(params);
                        mLinearLayout.clearAnimation();
                        mView.setVisibility(View.VISIBLE);
                    }
                });
                mBottomBar.animate().translationXBy(width).setDuration(500);
                isBottomAnim = false;
            } else {
                ViewPropertyAnimator animate = mLinearLayout.animate();
                animate.translationY(height).setDuration(500);
                mView.setVisibility(View.GONE);
                animate.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLinearLayout.getLayoutParams();
                        params.topMargin = params.topMargin + (-(mLinearLayout.getMeasuredHeight() + 30));
                        mLinearLayout.setLayoutParams(params);
                        mLinearLayout.clearAnimation();
                    }
                });
                animate.start();
                isBottomAnim = true;
                mBottomBar.animate().translationX(-width).setDuration(500);
            }
        }));
    }
}
