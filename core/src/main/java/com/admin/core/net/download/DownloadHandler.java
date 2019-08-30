package com.admin.core.net.download;

import android.os.AsyncTask;

import com.admin.core.net.RestCreator;
import com.admin.core.net.callback.IError;
import com.admin.core.net.callback.IFailure;
import com.admin.core.net.callback.IReqeust;
import com.admin.core.net.callback.ISuccess;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright (C)
 *
 * @file: DownloadHandler
 * @author: 345
 * @Time: 2019/4/19 16:36
 * @description: ${DESCRIPTION}
 */
public class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IReqeust REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(
            String url,
            IReqeust reqeust,
            String downloadDir,
            String extension,
            String name,
            ISuccess success,
            IFailure failure,
            IError error) {
        this.URL = url;
        this.REQUEST = reqeust;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }
    public final void handleDownload(String baseUrl){
        if (REQUEST != null){
            REQUEST.onRequestStart();
        }

        RestCreator.getRestService().download(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST,SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,responseBody,NAME);
                            //这里一定要注意判断，否则文件下载不全
                            if (task.isCancelled()){
                                if (REQUEST != null){
                                    REQUEST.onReqeustEnd();
                                }
                            }
                        }else {
                            if (ERROR != null){
                                ERROR.OnError(response.code(),response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE != null){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
