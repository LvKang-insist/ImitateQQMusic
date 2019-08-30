package com.admin.core.util.callback;

/**
 * Copyright (C)
 *
 * @file: CallBackType
 * @author: 345
 * @Time: 2019/5/9 15:06
 * @description: ${DESCRIPTION}
 */
public enum CallBackType {
    /**
     * 剪裁后的回调
     */
    ON_CROP,

    /**
     * 推送
     */
    PUSH,

    /**
     *  二维码
     */
    ON_SCAN,

    /**
     *  RecyclerView 的条目数
     */
    RECY_COUNT,
    /**
     *  BOTTOM 动画回调
     */
    BOTTOM,
    /**
     * 获取扫描sd 卡权限的回调
     */
    SCAN_SD,

    /**
     *  音乐改变后的回调，用于改变item 的状态
     */
    MUSIC_STATE,

    /**
     *  删除音乐后的回调
     */
    DELETE_MUSIC,

    /**
     * 关闭通知
     */
    STOP_NOTIF,

    /**
     *  通知栏暂停音乐
     */
    PAUSE_NOTIF,

    /**
     *  通知栏点击喜欢
     */
    LOVE_NOTIF,

}
