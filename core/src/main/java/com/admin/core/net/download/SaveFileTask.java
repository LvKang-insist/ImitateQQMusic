package com.admin.core.net.download;

import android.content.Intent;
import android.os.AsyncTask;

import com.admin.core.app.Latte;
import com.admin.core.net.callback.IReqeust;
import com.admin.core.net.callback.ISuccess;
import com.admin.core.util.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Copyright (C)
 *
 * @file: SaveFileTask
 * @author: 345
 * @Time: 2019/4/19 16:50
 * @description: ${DESCRIPTION}
 */
public class SaveFileTask extends AsyncTask<Object, Void, File> {
    private final IReqeust REQUEST;
    private final ISuccess SUCCESS;


    public SaveFileTask(IReqeust reqeust, ISuccess success) {
        this.REQUEST = reqeust;
        this.SUCCESS = success;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];
        final InputStream is = body.byteStream();

        if (downloadDir == null || downloadDir.equals("")) {
            downloadDir = "down_loads";
        }
        if (extension == null || extension.equals("")) {
            extension = "";
        }

        if (name == null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return FileUtil.writeToDisk(is,downloadDir,name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if (REQUEST != null){
            REQUEST.onReqeustEnd();
        }
    }

    //安装apk
    private void autoInstallApk(File file){
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            Latte.getApplication().startActivity(install);
        }
    }



}
