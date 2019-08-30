package com.admin.core.ui.camera;

import android.net.Uri;

/**
 * Copyright (C)
 *
 * @file: CameraImageBean
 * @author: 345
 * @Time: 2019/5/9 9:56
 * @description: 存储一些中间值
 */
public final class CameraImageBean {

    private Uri mPath = null;
    private CameraImageBean(){}

    private static final CameraImageBean INSTANCE = new CameraImageBean();
    public static CameraImageBean getInstance(){
        return INSTANCE;
    }

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri mPath) {
        this.mPath = mPath;
    }
}
