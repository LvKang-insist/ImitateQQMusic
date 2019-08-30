package com.admin.core.deleggate.bottom;

/**
 * Copyright (C)
 *
 * @file: BottomTabBean
 * @author: 345
 * @Time: 2019/4/25 19:40
 * @description: 导航栏 的Tab
 */
public final class BottomTabBean {
    /**
     * 图标
     */
    private final CharSequence ICON;
    /**
     * 文字
     */
    private final CharSequence TITLE;

    public BottomTabBean(CharSequence ICON, CharSequence TITLE) {
        this.ICON = ICON;
        this.TITLE = TITLE;
    }

    public CharSequence getIcon() {
        return ICON;
    }

    public CharSequence getTitle() {
        return TITLE;
    }
}
