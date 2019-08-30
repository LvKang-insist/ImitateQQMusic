package com.admin.core.activitys;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import com.admin.core.R;
import com.admin.core.deleggate.LatteDelegate;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Copyright (C)
 * 文件名称: ProxyActivity
 * 创建人: 345
 * 创建时间: 2019/4/15 20:18
 * 描述:
 */
public abstract class ProxyActivity extends AppCompatActivity implements ISupportActivity {

    public final SupportActivityDelegate DELEGATE = new SupportActivityDelegate(this);

    /**
     * @return 返回根 Delegate
     */
    public abstract LatteDelegate setRootDelegate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DELEGATE.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }

    /**
     * 初始化 视图
     * @param savedInstanceState 保存的数据
     */
    @SuppressLint("RestrictedApi")
    private void initContainer(@Nullable Bundle savedInstanceState) {
         final ContentFrameLayout container = new ContentFrameLayout(this);
        //设置这个视图的Id
        container.setId(R.id.delegate_container);
        setContentView(container);
        if (savedInstanceState == null){
            //SupportActivity 独有的一个方法
            //加载根Fragment 即Activity的第一个Fragment ，或者Fragment内的第一个Fragment
            //即 加载碎片
            DELEGATE.loadRootFragment(R.id.delegate_container,setRootDelegate());
        }
    }


    @Override
    protected void onDestroy() {
        DELEGATE.onDestroy();
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }

    @Override
    public void post(Runnable runnable) {

    }

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return DELEGATE;
    }

    @Override
    public ExtraTransaction extraTransaction() {
        return DELEGATE.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return DELEGATE.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        DELEGATE.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return DELEGATE.onCreateFragmentAnimator();
    }

    @Override
    public void onBackPressedSupport() {
        DELEGATE.onBackPressedSupport();
    }

    @Override
    public void onBackPressed() {
        DELEGATE.onBackPressed();
    }
}
