package com.admin.core.ui.scanner;

import android.content.Context;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Copyright (C)
 *
 * @file: ScanView
 * @author: 345
 * @Time: 2019/5/13 10:52
 * @description: ${DESCRIPTION}
 */
public class ScanView extends ZBarScannerView {
    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * 返回一个 二维码扫描的外观视图
     */
    @Override
    protected IViewFinder createViewFinderView(Context context) {
        return new LatteViewFinderView(context);
    }
}
