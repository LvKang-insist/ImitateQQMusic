package com.admin.work.main.player.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.admin.core.ui.view.player.Player;
import com.admin.work.main.player.PlayerControl;

public class UpBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("-----", "onReceive: up" );
        PlayerControl.getInstence().setUp();
    }
}
