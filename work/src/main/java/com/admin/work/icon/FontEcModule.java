package com.admin.work.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * Copyright (C)
 * 文件名称: FontEcModule
 * 创建人: 345
 * 创建时间: 2019/4/14 22:00
 * 描述: ${DESCRIPTION}
 */
public class FontEcModule implements IconFontDescriptor {
    @Override
    public String ttfFileName() {
        return "iconfont.ttf";
    }

    @Override
    public Icon[] characters() {
        return EcIcons.values();
    }
}
