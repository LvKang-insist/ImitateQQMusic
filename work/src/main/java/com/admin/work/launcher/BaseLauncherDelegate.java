package com.admin.work.launcher;

import android.app.Activity;
import android.util.Log;

import com.admin.core.app.AccountManager;
import com.admin.core.app.IUserChecker;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.ui.loader.ILauncherListener;
import com.admin.core.ui.loader.OnLauncherFinishTag;


/**
 * Copyright (C)
 *
 * @file: BaseLauncherDelegate
 * @author: 345
 * @Time: 2019/4/23 20:20
 * @description: 抽象类，用于管理 首页倒计时和轮播图
 */
public abstract class BaseLauncherDelegate extends LatteDelegate {
    private ILauncherListener mILauncherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    public void checkSignIn() {
        //检查用户是否登录了 APP
        AccountManager.checkAccount(new IUserChecker() {
            @Override
            public void onSignIn(String SignState, String SignNumber) {
                //不等于 null 说明接口Activity实现了
                if (mILauncherListener != null) {
                    //已经登录
                    mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    if (SignState.equals(AccountManager.SignIn.NUMBER_SIGNIN.name())) {
                        Log.e("-------", "账号登录: "+SignNumber );
                    }else if (SignState.equals(AccountManager.SignIn.QQ_SIGNIN.name())) {
                        Log.e("-------", "qq登录: "+SignNumber );
                    }else if (SignState.equals(AccountManager.SignIn.PHONE_SIGNIN.name())) {
                        Log.e("-------", "手机号登录: "+SignNumber );
                    }
                }
            }

            @Override
            public void onNoSignIn() {
                if (mILauncherListener != null) {
                    mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                }
            }
        });
    }
}
