package com.minchainx.permission.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * fit to System Permission Activity
 */

public class SystemPermissionPage {
    private static final String TAG = "SystemPermissionPage";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();
    private Context mContext;

    public SystemPermissionPage(Context context) {
        this.mContext = context;
    }

    public void launch(int requestCode) {
        Intent intent = getIntentByMark();
        if (mContext != null && mContext instanceof Activity) {
            Activity a = (Activity) mContext;
            try {
                a.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                a.startActivityForResult(defaultPage(mContext), requestCode);
            }
        } else {
            throw new IllegalStateException("must initialize context correct");
        }
    }

    public void launch() {
        Intent intent = getIntentByMark();
        if (mContext != null && mContext instanceof Activity) {
            Activity a = (Activity) mContext;
            try {
                a.startActivity(intent);
            } catch (Exception e) {
                a.startActivity(defaultPage(mContext));
            }
        } else {
            throw new IllegalStateException("must initialize context correct");
        }
    }

    private Intent getIntentByMark() {
        if (MARK.contains("huawei")) {
            return huaweiPage(mContext);
        } else if (MARK.contains("xiaomi")) {
            return xiaomiPage(mContext);
        } else if (MARK.contains("oppo")) {
            return oppoPage(mContext);
        } else if (MARK.contains("vivo")) {
            return vivoPage(mContext);
        } else if (MARK.contains("meizu")) {
            return meizuPage(mContext);
        } else if (MARK.contains("sony")) {
            return sonyPage(mContext);
        } else if (MARK.contains("lg")) {
            return lgPage(mContext);
        }
        return defaultPage(mContext);
    }

    private static Intent defaultPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private static Intent huaweiPage(Context context) {
        Intent intent = new Intent(context.getPackageName());
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    private static Intent xiaomiPage(Context context) {
        String rom = getMiUiVersion();
        Intent intent = new Intent();
        if ("V5".equals(rom)) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getApplicationInfo().packageName));
        } else if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else {
            return defaultPage(context);
        }
        return intent;
    }

    private static Intent oppoPage(Context context) {
        String[] packageInfo = startApplicationPackageInfo(context, "com.color.safecenter");
        if (packageInfo != null && packageInfo[0] != null && packageInfo[1] != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageInfo[0], packageInfo[1]);
            intent.setComponent(cn);
            return intent;
        } else {
            return defaultPage(context);
        }
    }

    private static Intent vivoPage(Context context) {
        String[] packageInfo = startApplicationPackageInfo(context, "com.bairenkeji.icaller");
        if (packageInfo != null && packageInfo[0] != null && packageInfo[1] != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageInfo[0], packageInfo[1]);
            intent.setComponent(cn);
            return intent;
        } else {
            return defaultPage(context);
        }
    }

    private static Intent meizuPage(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    private static Intent sonyPage(Context context) {
        Intent intent = new Intent(context.getPackageName());
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent lgPage(Context context) {
        Intent intent = new Intent(context.getPackageName());
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static String getMiUiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
            }
        }
        Log.e(TAG, "Rom--- rom : " + line);
        return line;
    }

    private static String[] startApplicationPackageInfo(Context context, String packageName) {
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageinfo != null) {
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(packageinfo.packageName);
                List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
                Log.e(TAG, "resolveInfoList" + resolveInfoList.size());
                ResolveInfo resolveInfo = resolveInfoList.iterator().next();
                if (resolveInfo != null && resolveInfo.activityInfo != null) {
                    return new String[]{resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name};
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return null;
    }

}
