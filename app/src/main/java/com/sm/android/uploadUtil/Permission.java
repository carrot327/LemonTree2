package com.sm.android.uploadUtil;

import android.content.Context;
import android.util.Log;

import com.minchainx.permission.setting.MyPermission;
import com.minchainx.permission.simple.SimpleRuntimeForbid;
import com.minchainx.permission.simple.SimpleRuntimeRationale;
import com.minchainx.permission.util.PermissionListener;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/10 10:21
 * 文件描述:
 */
public class Permission {

    public Permission(Context context, String[] permissions, PermissionListener permissionListener) {

        MyPermission
                .runtime(context).permission(permissions)
                .rationale(new SimpleRuntimeRationale(permissionListener))
                .forbid(new SimpleRuntimeForbid(permissionListener))
                .onGranted(data -> {
                    permissionListener.onGranted();
                }).onDenied(data -> {
                    permissionListener.onDenied();
                }).start();
    }
}
