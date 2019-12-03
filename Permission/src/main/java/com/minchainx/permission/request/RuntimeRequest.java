package com.minchainx.permission.request;

import android.content.Context;

import com.minchainx.permission.base.Action;
import com.minchainx.permission.base.Forbid;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.check.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * runtime permission request
 */

public abstract class RuntimeRequest implements PRequest<String[]> {

    protected Context mContext;
    protected String[] mPermissions;
    protected Action<String[]> mGranted;
    protected Action<String[]> mDenied;

    protected RuntimeRequest(Context context) {
        this.mContext = context;
    }

    public RuntimeRequest permission(String[] permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @Override
    public RuntimeRequest onGranted(Action<String[]> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public RuntimeRequest onDenied(Action<String[]> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public RuntimeRequest rationale(Rationale<String[]> rationale) {
        return this;
    }

    /**
     * Deal with never show permission prompt dialog
     */
    public abstract RuntimeRequest forbid(Forbid<String[]> forbid);

    /**
     * Use permissionChecker get denied permissions.
     */
    public String[] getDeniedPermissions(PermissionChecker checker, String[] permissions) {
        List<String> deniedList = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (!checker.hasPermission(mContext, permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList.toArray(new String[]{});
    }

    /**
     * CallBack all permissions
     */
    public void callBackPermissions() {
        if (mGranted != null)
            mGranted.onAction(mPermissions);
    }

    /**
     * CallBack Denied permissions
     */
    public void callBackPermissions(String[] deniedPermissions) {
        if (mDenied != null)
            mDenied.onAction(deniedPermissions);
    }

}
