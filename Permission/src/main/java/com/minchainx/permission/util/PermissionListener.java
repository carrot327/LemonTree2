package com.minchainx.permission.util;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/27 09:50
 * 文件描述:
 */
public interface PermissionListener {
    void onGranted();

    void onDenied();
}
