package com.admin.core.app;

import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

/**
 * Copyright (C)
 * 文件名称: Latte
 * 创建人: 345
 * 创建时间: 2019/4/14 18:34
 * 描述: 初始化
 */
public final class Latte {

    private final static Handler HEADLER = new Handler();

    /**
     * @return 返回一个handler 对象
     */
    public static Handler getHandler() {
        return HEADLER;
    }

    /**
     * @param context context
     * @return 返回配置对象
     */
    public static Configurator init(Context context) {
        //保存 Context
        getConfigurations()
                .put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }


    /**
     * @return 返回 配置信息
     */
    public static HashMap<Object, Object> getConfigurations() {
        return Configurator.getInstance().getLatteConfigs();
    }

    /**
     * @return 返回单例
     */
    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    /**
     * @param key 要查询的键
     * @param <T> 调用的类型
     * @return 返回值为调用的类型
     */
    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    /**
     * @return 返回 Context
     */
    public static Context getApplication() {
        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT);
    }
}
