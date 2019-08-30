package com.admin.core.ui.camera;

import com.yalantis.ucrop.UCrop;

/**
 * Copyright (C)
 *
 * @file: RequestCode
 * @author: 345
 * @Time: 2019/5/9 9:55
 * @description: 请求码存储
 */
public class RequestCode {
    /**
     * 拍照
     */
    public static final int TAKE_PHOTO = 4;
    /**
     * 选照
     */
    public static final int PICK_PHOTO = 5;
    /**
     * 剪裁
     */
    public static final int CROP_PHOTO = UCrop.REQUEST_CROP;
    /**
     * 剪裁错误
     */
    public static final int CROP_ERROR= UCrop.RESULT_ERROR;
    /**
     * 扫描二维码
     */
    public static final int SCAN = 7;
}
