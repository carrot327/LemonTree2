package com.minchainx.permission.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;

import com.minchainx.permission.util.AlertWindowSettingPage;
import com.minchainx.permission.util.OverlaySettingPage;
import com.minchainx.permission.util.PermissionRecord;
import com.minchainx.permission.util.SystemPermissionPage;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * Permission Activity
 */
public class PermissionActivity extends AppCompatActivity {
    private static final String TAG = "PermissionActivity";
    public static final String KEY_OPERATION = "key_input_operation";
    public static final String KEY_PERMISSIONS = "key_permissions";
    public static final String KEY_FORCE_CHECK_PERMISSION = "key_force_check_permission";

    private static final int VALUE_RUNTIME_PERMISSION = 1;
    private static final int VALUE_SETTING_PERMISSION = 2;
    private static final int VALUE_OVERLAY_ALERT_WINDOW = 3;
    private static final int VALUE_OVERLAY_SETTING = 4;
    private static final int VALUE_INSTALL = 5;
    private static final int VALUE_NO_RESULT = 99;

    private static RequestListener sRequestListener;

    /**
     * Request for runtime permission.
     */
    public static void requestRuntimePermission(Context context, String[] permissions, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_OPERATION, VALUE_RUNTIME_PERMISSION);
        intent.putExtra(KEY_PERMISSIONS, permissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for permission setting.
     */
    public static void requestSettingPermission(Context context, boolean isForceCheckPermission, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_OPERATION, VALUE_SETTING_PERMISSION);
        intent.putExtra(KEY_FORCE_CHECK_PERMISSION, isForceCheckPermission);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for overlay permission alert window.
     */
    public static void requestOverlayAlertWindow(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_OPERATION, VALUE_OVERLAY_ALERT_WINDOW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for overlay permission activity.
     */
    public static void requestOverlaySetting(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_OPERATION, VALUE_OVERLAY_SETTING);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request for package install.
     */
    public static void requestInstall(Context context, RequestListener requestListener) {
        PermissionActivity.sRequestListener = requestListener;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_OPERATION, VALUE_INSTALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int operation = getIntent().getIntExtra(KEY_OPERATION, VALUE_RUNTIME_PERMISSION);
        switch (operation) {
            case VALUE_RUNTIME_PERMISSION:
                String[] permissions = getIntent().getStringArrayExtra(KEY_PERMISSIONS);
                if (permissions != null && sRequestListener != null) {
                    requestPermissions(permissions, operation);
                } else {
                    finish();
                }
                break;
            case VALUE_SETTING_PERMISSION:
                if (sRequestListener != null) {
                    boolean force_check = getIntent().getBooleanExtra(KEY_FORCE_CHECK_PERMISSION, false);
                    if (!force_check) {
                        new SystemPermissionPage(this).launch(VALUE_NO_RESULT);
                    } else {
                        new SystemPermissionPage(this).launch(VALUE_SETTING_PERMISSION);
                    }
                } else {
                    finish();
                }
                break;
            case VALUE_OVERLAY_ALERT_WINDOW:
                if (sRequestListener != null) {
                    new AlertWindowSettingPage(this).launch(VALUE_NO_RESULT);
                } else {
                    finish();
                }
                break;
            case VALUE_OVERLAY_SETTING:
                if (sRequestListener != null) {
                    new OverlaySettingPage(this).launch(VALUE_NO_RESULT);
                } else {
                    finish();
                }
                break;
            case VALUE_INSTALL:
                if (sRequestListener != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(manageIntent, VALUE_NO_RESULT);
                    }
                } else {
                    finish();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            String denyPermission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(denyPermission)) {
                    PermissionRecord.recordState(this, denyPermission, PermissionRecord.STATE_RATIONAL);
                } else {
                    PermissionRecord.recordState(this, denyPermission, PermissionRecord.STATE_FORBID);
                }
            } else {
                PermissionRecord.deleteState(this, denyPermission);
            }
        }
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback(false);
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback(requestCode != VALUE_NO_RESULT);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        sRequestListener = null;
        super.finish();
    }

    public interface RequestListener {
        /**
         * @param isForceCheckPermission true:force check permission
         */
        void onRequestCallback(boolean isForceCheckPermission);
    }
}
