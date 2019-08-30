package com.admin.core.app;

/**
 * Copyright (C)
 * 文件名称: ConfigType
 * 创建人: 345
 * 创建时间: 2019/4/14 18:39
 * 描述: 在整个应用程序中 是一个单例，并且只能被初始化一次。
 * @author Lv
 */
public enum ConfigType {
    /**
     * 初始化
     */
    //网络请求
    API_HOST,
    //全局的上下文
    APPLICATION_CONTEXT,
    // 初始化或者配置 完成了没有
    CONFIG_READY,
    ACTIVITY,
}
