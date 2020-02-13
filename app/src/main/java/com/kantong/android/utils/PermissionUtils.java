package com.kantong.android.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by CHULEI on 2017/10/20.
 */

public class PermissionUtils {
    public static boolean checkPermission(Context context, String permName, String pkgName) {
        PackageManager pm = context.getPackageManager();
        if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, pkgName)) {
            return true;
        } else {
            return false;
        }
    }
}
