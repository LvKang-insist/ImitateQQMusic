package com.admin.core.deleggate;

/**
 * Copyright (C)
 * 文件名称: LatteDelegate
 * 创建人: 345
 * 创建时间: 2019/4/16 13:35
 * 描述: 要正式使用Delegate
 */
public abstract class LatteDelegate extends PermissionCheckerDelegate {

    public <T extends LatteDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }


}
