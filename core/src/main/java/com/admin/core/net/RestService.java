package com.admin.core.net;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Copyright (C)
 *
 * @file: RestService
 * @author: 345
 * @Time: 2019/4/17 17:40
 * @description: Retrofit 的请求方法
 */
public interface RestService {
    /**
     *  get请求
     * @param url 地址
     * @param params 参数
     * @return 返回一个Call
     */
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    /**
     *  @FormUrlEncoded 表示请求体是一个Form 表单，
     */
    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    /**
     * 传入原始 数据就不能添加 @FormUrlEncoded 了
     * @Body 即非表单请求体，被body 注解的将会被 Gson 转换为json 发送到服务器
     */
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * download 默认是吧文件下载到内存中，最后统一写入文件里，这种方式存在一个问题：
     * 当文件过大是 会导致文件溢出，所以这里加入@Streaming ，代表一边下载一边写入到文件中
     * 这样就避免了 一次性在内存中写入过大的文件造成的问题
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);


    /**
     * @Multipart : 表示请求体是一个支持文件上传的From 表单
     */
    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

}
