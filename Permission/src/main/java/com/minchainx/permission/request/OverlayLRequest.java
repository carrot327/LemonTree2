package com.minchainx.permission.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.base.RequestExecutor;
import com.minchainx.permission.setting.PermissionActivity;
import com.minchainx.permission.util.Permission;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for overlay request permission
 * adapter Android 5.0(API>=21)
 */

public class OverlayLRequest extends OverlayRequest implements RequestExecutor, PermissionActivity.RequestListener {

    private Rationale<String[]> mRationale;

    public OverlayLRequest(Context context) {
        super(context);
    }

    @Override
    public OverlayLRequest rationale(Rationale<String[]> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public void start() {
        if (showOverlayDialog(mContext)) {
            callBackGranted();
        } else {
            if (mRationale != null)
                mRationale.showRationale(mContext, new String[]{Permission.SYSTEM_ALERT_WINDOW}, this);
            else
                execute(RequestExecutor.TASK_OVERLAY);
        }
    }

    @Override
    public void execute(int taskId) {
        if (taskId == TASK_OVERLAY)
            PermissionActivity.requestOverlayAlertWindow(mContext, this);
    }

    @Override
    public void cancel() {
        callBackDenied();
    }

    @Override
    public void onRequestCallback(boolean isForceCheckPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canDrawOverlays(mContext) && showOverlayDialog(mContext)) {
                    callBackGranted();
                } else {
                    callBackDenied();
                }
            }
        }, 100);
    }
}
