package com.admin.work.main.player;

import com.admin.core.ui.recycler.MultipleItemEntity;

/**
 * 当开始播放新的音乐时，将数据传递到详情页面
 */
public interface OnNowSongDataListener {
    void nowSongData(MultipleItemEntity entity);
}
