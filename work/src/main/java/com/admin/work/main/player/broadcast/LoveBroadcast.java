package com.admin.work.main.player.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.admin.work.R;
import com.admin.work.main.player.PlayerControl;
import com.admin.work.main.player.services.MusicService;

public class LoveBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("-----", "onReceive: love" );
    }
}
