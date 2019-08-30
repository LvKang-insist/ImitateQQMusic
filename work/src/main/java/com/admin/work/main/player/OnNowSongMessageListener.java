package com.admin.work.main.player;

import com.admin.work.main.home.search.NetWorkKuGouSong;
import com.admin.work.main.home.search.NetWorkQQSong;

/**
 * 当前正在播放的音乐信息
 */
public interface OnNowSongMessageListener {
    void onSongMessage(NetWorkQQSong netWorkQQSong);
}
