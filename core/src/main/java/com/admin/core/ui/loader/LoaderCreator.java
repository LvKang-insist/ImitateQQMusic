package com.admin.core.ui.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Copyright (C)
 *
 * @file: LoaderCreator
 * @author: 345
 * @Time: 2019/4/18 16:34
 * @description: 以缓存的方式 创建Loader
 */
public final class LoaderCreator{

    //进行缓存
    private static final WeakHashMap<String,Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView creawte(String type, Context context){
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type) == null){
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type,indicator);
        }
        //拿到该 样式
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    private static Indicator getIndicator(String name){
        if (name == null || name.isEmpty()){
            return null;
        }
        final StringBuilder drawbleClassName = new StringBuilder();
        //如果类名不包含 . 就说明传入的是一个 整个的类名
        if (!name.contains(".")){
            //拿到 包名
            final String defultPackageName = AVLoadingIndicatorView.class.getPackage().getName();
            drawbleClassName.append(defultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawbleClassName.append(name);
        try {
            //通过反射 拿到给定的类没
            final Class<?> drawableClass = Class.forName(drawbleClassName.toString());
            //创建 该对象并返回
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
