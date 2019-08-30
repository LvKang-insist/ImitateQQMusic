package com.admin.work.main.player.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.admin.work.main.player.PlayerControl;

public class BelowBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerControl.getInstence().setBelow();
    }
}
