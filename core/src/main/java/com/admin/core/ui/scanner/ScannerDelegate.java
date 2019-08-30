package com.admin.core.ui.scanner;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.admin.core.deleggate.LatteDelegate;
import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Copyright (C)
 *
 * @file: ScannerDelegate
 * @author: 345
 * @Time: 2019/5/13 10:48
 * @description: ${DESCRIPTION}
 */
public class ScannerDelegate  extends LatteDelegate implements ZBarScannerView.ResultHandler {

    private ScanView mScanView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mScanView == null){
            mScanView = new ScanView(getContext());
        }
        //自动对焦
        mScanView.setAutoFocus(true);
        // 回调监听
        mScanView.setResultHandler(this);
    }

    @Override
    public Object setLayout() {
        return mScanView;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScanView != null){
            mScanView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanView != null){
            //停止预览
            mScanView.stopCameraPreview();
            //停止扫描
            mScanView.stopCamera();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleResult(Result rawResult) {
        final IGlobalCallback<String> callback = CallbackManager
                .getInstance()
                .getCallBack(CallBackType.ON_SCAN);
        if (callback!= null){
            callback.executeCallBack(rawResult.getContents());
        }
        getSupportDelegate().pop();
    }
}
