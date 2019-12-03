package com.minchainx.permission.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import com.minchainx.permission.base.Forbid;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.base.RequestExecutor;
import com.minchainx.permission.check.StandardChecker;
import com.minchainx.permission.setting.PermissionActivity;
import com.minchainx.permission.util.PermissionRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for request runtime permission
 * adapter Android 6.0(API>=23)
 */
public class RuntimeMRequest extends RuntimeRequest implements RequestExecutor, PermissionActivity.RequestListener {

    private Rationale<String[]> mRationale;
    private Forbid<String[]> mForbid;

    private String[] mDeniedPermissions;

    public RuntimeMRequest(Context context) {
        super(context);
    }

    @Override
    public RuntimeMRequest rationale(Rationale<String[]> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public RuntimeMRequest forbid(Forbid<String[]> forbid) {
        this.mForbid = forbid;
        return this;
    }

    @Override
    public void start() {
        String[] deniedPermissions = getDeniedPermissions(new StandardChecker(), mPermissions);
        if (deniedPermissions.length > 0) {
            mDeniedPermissions = Arrays.copyOf(deniedPermissions, deniedPermissions.length);
            int state = getDeniedPermissionState(deniedPermissions);
            switch (state) {
                case PermissionRecord.STATE_INIT:
                    execute(TASK_RUNTIME);
                    break;
                case PermissionRecord.STATE_RATIONAL:
                    if (mRationale != null) {
                        mDeniedPermissions = Arrays.copyOf(getRationalePermissions(mDeniedPermissions), getRationalePermissions(mDeniedPermissions).length);
                        mRationale.showRationale(mContext, mDeniedPermissions, this);
                    } else {
                        execute(TASK_RUNTIME);
                    }
                    break;
                case PermissionRecord.STATE_FORBID:
                    if (mForbid != null) {
                        mDeniedPermissions = Arrays.copyOf(getForbidPermissions(mDeniedPermissions), getForbidPermissions(mDeniedPermissions).length);
                        mForbid.showForbidden(mContext, mDeniedPermissions, this);
                    } else {
                        execute(TASK_RUNTIME);
                    }
                    break;
            }
        } else {
            callBackPermissions();
        }
    }

    @Override
    public void execute(int taskId) {
        switch (taskId) {
            case TASK_RUNTIME:
                PermissionActivity.requestRuntimePermission(mContext, mDeniedPermissions, this);
                break;
            case TASK_SETTING:
                PermissionActivity.requestSettingPermission(mContext, false, this);
                break;
            case TASK_SETTING_STRICT_CHECK:
                PermissionActivity.requestSettingPermission(mContext, true, this);
                break;
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onRequestCallback(final boolean isForceCheckPermission) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] deniedPermissions = getDeniedPermissions(new StandardChecker(), mPermissions);
                if (deniedPermissions.length == 0)
                    callBackPermissions();
                else
                    callBackPermissions(deniedPermissions);
                if (isForceCheckPermission)
                    start();
            }
        }, 100);
    }

    /**
     * Get permissions to show rationale.
     */
    private String[] getRationalePermissions(String... permissions) {
        return getPermissionsByRecordState(PermissionRecord.STATE_RATIONAL, permissions);
    }

    /**
     * Get permissions to show forbid.
     */
    private String[] getForbidPermissions(String... permissions) {
        return getPermissionsByRecordState(PermissionRecord.STATE_FORBID, permissions);
    }

    /**
     * Get permissions by recordState.
     */
    private String[] getPermissionsByRecordState(int recordState, String... permissions) {
        List<String> list = new ArrayList<>(1);
        for (String permission : permissions) {
            int state = PermissionRecord.getState(mContext, permission);
            if (state == recordState)
                list.add(permission);
        }
        return list.toArray(new String[]{});
    }

    /**
     * Get permissions record state
     */
    private int getDeniedPermissionState(String... permissions) {
        for (String permission : permissions) {
            int state = PermissionRecord.getState(mContext, permission);
            if (state == PermissionRecord.STATE_RATIONAL || state == PermissionRecord.STATE_FORBID)
                return state;
        }
        return PermissionRecord.STATE_INIT;
    }

}
