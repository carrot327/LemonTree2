package com.minchainx.permission.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.minchainx.permission.base.Action;
import com.minchainx.permission.base.RequestExecutor;
import com.minchainx.permission.setting.PermissionActivity;
import com.minchainx.permission.util.Permission;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * InstallORequest
 */

public class InstallORequest extends InstallRequest implements RequestExecutor, PermissionActivity.RequestListener {

    public InstallORequest(Context context) {
        super(context);
    }

    @Override
    public void start() {
        new RuntimeMRequest(mContext)
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<String[]>() {
                    @Override
                    public void onAction(String[] data) {
                        if (canRequestPackageInstalls()) {
                            callbackGranted();
                            install();
                        } else {
                            if (mRationale != null)
                                mRationale.showRationale(mContext, null, InstallORequest.this);
                            else
                                execute(RequestExecutor.TASK_INSTALL);
                        }
                    }
                })
                .onDenied(new Action<String[]>() {
                    @Override
                    public void onAction(String[] data) {

                    }
                })
                .start();
    }


    @Override
    public void execute(int taskId) {
        if (taskId == TASK_INSTALL)
            PermissionActivity.requestInstall(mContext, this);
    }

    @Override
    public void cancel() {
        callbackDenied();
    }

    @Override
    public void onRequestCallback(boolean isForceCheckPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canRequestPackageInstalls()) {
                    callbackGranted();
                    install();
                } else {
                    callbackDenied();
                }
            }
        }, 100);
    }

}
