package com.admin.frame;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.admin.core.app.Latte;
import com.admin.core.util.value.Resource;
import com.admin.work.icon.FontEcModule;
import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.mob.MobSDK;

import org.litepal.LitePal;
/**
 * Copyright (C)
 * 文件名称: ExampleApp
 * 创建人: 345
 * 创建时间: 2019/4/14 20:29
 * 描述: 继承自 Application ，用于进行初始化
 */
public class ExampleApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);

        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withHost("http://192.168.2.140:80/Frame_Api/")
                .configure();

        //初始化数据库
        LitePal.initialize(this);
        initStetho();
    }

    /**
     *  查看数据库
     */
    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

}
