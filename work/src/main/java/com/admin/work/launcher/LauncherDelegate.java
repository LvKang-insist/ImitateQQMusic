package com.admin.work.launcher;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.admin.core.ui.launcher.ScrollLauncherTag;
import com.admin.core.util.storage.LattePreference;
import com.admin.core.util.time.BaseTimeTask;
import com.admin.core.util.time.ITimeListener;
import com.admin.work.R;
import com.admin.work.R2;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C)
 *
 * @file: LauncherDelegate
 * @author: 345
 * @Time: 2019/4/21 11:45
 * @description: 首页倒计时 效果
 */
public class LauncherDelegate extends BaseLauncherDelegate implements ITimeListener {

    @BindView(R2.id.tv_launcher_time)
    AppCompatTextView mTvTimer = null;

    private Timer mTimer = null;
    private int mCount = 5;

    @OnClick(R2.id.tv_launcher_time)
    void onClickTimerView() {
        if (mTimer != null) {
            //倒计时 暂停
            mTimer.cancel();
            mTimer = null;
            checkIsShowScroll();
        }
    }

    /**
     * 初始化 Timer
     */
    @SuppressWarnings("AlibabaAvoidUseTimer")
    private void initTimer() {
        mTimer = new Timer();
        final BaseTimeTask task = new BaseTimeTask(this);
        //第一个参数 要执行的任务，2，延迟的时间，3，每隔一秒执行一次
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initTimer();
    }

    /**
     * 判断是否显示 滑动启动页
     */
    private void checkIsShowScroll() {
        if (!LattePreference.getAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name())) {
            getSupportDelegate().startWithPop(new LauncherScrollDelegate());
        } else {
            //判断是否登录 及后面的逻辑
            checkSignIn();
        }
    }

    @Override
    public void onTime() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvTimer != null) {
                    mTvTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            //倒计时 暂停
                            mTimer.cancel();
                            mTimer = null;
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }
}
