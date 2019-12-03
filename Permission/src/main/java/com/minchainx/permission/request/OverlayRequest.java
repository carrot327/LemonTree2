package com.minchainx.permission.request;

import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.minchainx.permission.R;
import com.minchainx.permission.base.Action;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.util.Permission;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for overlay request permission
 */

public abstract class OverlayRequest implements PRequest<String[]> {

    protected Context mContext;
    protected Action<String[]> mGranted;
    protected Action<String[]> mDenied;

    protected OverlayRequest(Context context) {
        this.mContext = context;
    }

    @Override
    public OverlayRequest onGranted(Action<String[]> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public OverlayRequest onDenied(Action<String[]> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public OverlayRequest rationale(Rationale<String[]> rationale) {
        return this;
    }

    /**
     * CallBack all permission
     */
    public void callBackGranted() {
        if (mGranted != null)
            mGranted.onAction(new String[]{Permission.SYSTEM_ALERT_WINDOW});
    }

    /**
     * CallBack Denied permission
     */
    public void callBackDenied() {
        if (mDenied != null)
            mDenied.onAction(new String[]{Permission.SYSTEM_ALERT_WINDOW});
    }

    /**
     * is can draw overlays
     */
    boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (opsManager != null) {
                int result = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, android.os.Process.myUid(), context.getPackageName());
                return result == AppOpsManager.MODE_ALLOWED;
            }
        }
        return true;
    }

    /**
     * has show overlay dialog
     */
    boolean showOverlayDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.Permission_Theme_Dialog);
        int windowType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowType = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        try {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setType(windowType);
                dialog.show();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (dialog.isShowing()) dialog.dismiss();
        }
        return true;
    }
}
