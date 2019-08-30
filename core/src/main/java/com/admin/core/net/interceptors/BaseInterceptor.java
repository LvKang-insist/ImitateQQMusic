package com.admin.core.net.interceptors;

import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Copyright (C)
 *
 * @file: BaseInterceptor
 * @author: 345
 * @Time: 2019/4/20 13:58
 * @description: ${DESCRIPTION}
 */
public abstract class BaseInterceptor implements Interceptor {

    /**
     * @param
     * @return 拿到 请求的参数
     */
    protected LinkedHashMap<String,String> getUrlParameters(Chain chain){
        final HttpUrl url = chain.request().url();
        //请求参数 的个数
        int size = url.querySize();

        final LinkedHashMap<String ,String> Params = new LinkedHashMap<>();
        for (int i = 0 ;i < size ;i++){
            Params.put(url.queryParameterName(i),url.queryParameterValue(i));
        }
        return Params;
    }

    /**
     *
     * @param chain .
     * @param key 要查找的参数
     * @return 返回对应的参数
     */
    protected String getUrlParameters(Chain chain,String key){
        Request request = chain.request();
        return request.url().queryParameter(key);
    }

    /**
     *
     * @param chain .
     * @return 从post 的请求体中获取参数
     */
    private LinkedHashMap<String,String> getBodyparameters(Chain chain){
        final FormBody formBody = (FormBody) chain.request().body();
        final LinkedHashMap<String ,String> params = new LinkedHashMap<>();
        int size = formBody.size();
        for (int i = 0; i < size; i++) {
            params.put(formBody.name(i),formBody.value(i));
        }
        return params;
    }

    private String getBodyparameters(Chain chain,String key){
        return getBodyparameters(chain).get(key);
    }
}
