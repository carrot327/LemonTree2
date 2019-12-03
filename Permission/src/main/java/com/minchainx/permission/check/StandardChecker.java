package com.minchainx.permission.check;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

/**
 * Created by jimmy on 2018/8/3.
 * <p>
 * use for request permission only for Android 6.0(API>=23)
 */

public class StandardChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        int result = context.checkPermission(permission, Process.myPid(), Process.myUid());
        if (result == PackageManager.PERMISSION_DENIED)
            return false;
        String op = AppOpsManager.permissionToOp(permission);
        if (TextUtils.isEmpty(op))
            return true;
        AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (opsManager != null) {
            result = opsManager.checkOpNoThrow(op, Process.myUid(), context.getPackageName());
            if (result != AppOpsManager.MODE_ALLOWED)
                return false;
        }
        return true;
    }


}
