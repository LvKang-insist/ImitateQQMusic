package com.admin.work.main.player.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.admin.core.util.callback.CallBackType;
import com.admin.core.util.callback.CallbackManager;
import com.admin.core.util.callback.IGlobalCallback;
import com.admin.work.R;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.services.MusicService;

public class PauseBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        IGlobalCallback callBack = CallbackManager.getInstance().getCallBack(CallBackType.PAUSE_NOTIF);
        if (callBack != null) {
            callBack.executeCallBack(null);
        }

    }
}
