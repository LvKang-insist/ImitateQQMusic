package com.admin.core.app;

/**
 * Copyright (C)
 *
 * @file: IUserChecker
 * @author: 345
 * @Time: 2019/4/23 17:53
 * @description: ${DESCRIPTION}
 */
public interface IUserChecker {
    void onSignIn(String SignState,String SignNumber);
    void onNoSignIn();
}
