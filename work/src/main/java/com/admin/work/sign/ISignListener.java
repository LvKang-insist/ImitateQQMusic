package com.admin.work.sign;

/**
 * Copyright (C)
 *
 * @file: ISignListener
 * @author: 345
 * @Time: 2019/4/23 18:00
 * @description: ${DESCRIPTION}
 */
public interface ISignListener {
    /**
     * 登录成功的回调
     */
    void onSignInSuccess();

    /**
     * 注册成功的回调
     */
    void onSignUpSuccess();
}
