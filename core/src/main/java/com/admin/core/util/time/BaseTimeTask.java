package com.admin.core.util.time;


import java.util.TimerTask;

/**
 * Copyright (C)
 *
 * @file: BaseTimeTask
 * @author: 345
 * @Time: 2019/4/21 11:46
 * @description: ${DESCRIPTION}
 */
public class BaseTimeTask extends TimerTask {

    private ITimeListener mITimeListener = null;

    public BaseTimeTask(ITimeListener timeListener) {
        this.mITimeListener = timeListener;
    }

    @Override
    public void run() {
        if (mITimeListener != null){
            mITimeListener.onTime();
        }
    }
}
