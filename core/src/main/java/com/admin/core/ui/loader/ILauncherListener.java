package com.admin.core.ui.loader;

/**
 * Copyright (C)
 *
 * @file: ILauncherListener
 * @author: 345
 * @Time: 2019/4/23 19:25
 * @description: 在程序启动的时候调用，用来处理用户是否登录
 */
public interface ILauncherListener {

    void onLauncherFinish(OnLauncherFinishTag onLauncherFinishTag);
}
