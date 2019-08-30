package com.admin.core.util.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.admin.core.app.Latte;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Copyright (C)
 *
 * @file: LattePreference
 * @author: 345
 * @Time: 2019/4/21 11:25
 * @description: 提示：
 * Activity.getPreferences(int mode) 生成Activity名.xml 用于Activity内部存储
 * PreferenceManager.getDefaultsharedPreferences(Content) 生成 包名_preferences.xml
 * Context.getSharedPreferences(String name,int mode) 生成name.xml
 */
public class LattePreference {
    /**
     * 每个 应用都有一个默认的配置文件 preferences.xml ，使用getDefaultSharedPreferences 获取
     */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(Latte.getApplication());


    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }


    public static void setAppFlag(String key,boolean flag){
        getAppPreference()
                .edit()
                .putBoolean(key,flag)
                .apply();
    }
    public static boolean getAppFlag(String key){
        return getAppPreference().getBoolean(key,false);
    }

    public static void setAppData(String key, String flag){
        getAppPreference()
                .edit()
                .putString(key,flag)
                .apply();
    }
    public static String getAppData(String key){
        return getAppPreference().getString(key,null);
    }

    public static void setAppInteger(String key, int value){
        getAppPreference()
                .edit()
                .putInt(key,value)
                .apply();
    }
    public static int getAppInteger(String key){
        return getAppPreference().getInt(key,0);
    }

}
