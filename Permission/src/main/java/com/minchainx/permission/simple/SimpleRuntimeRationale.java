package com.minchainx.permission.simple;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;


import com.minchainx.permission.R;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.base.RequestExecutor;
import com.minchainx.permission.util.Permission;
import com.minchainx.permission.util.PermissionListener;

import java.util.List;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * 若第一次没有同意权限申请，第二次会弹出该提示对话框。不强制校验权限
 */

public class SimpleRuntimeRationale implements Rationale<String[]> {

    private PermissionListener mListener = null;

    public SimpleRuntimeRationale() {
    }

    public SimpleRuntimeRationale(PermissionListener listener) {

        this.mListener = listener;
    }

    @Override
    public void showRationale(Context context, String[] permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));

        Log.e("权限申请", "msg===>" + message);
        new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//        new AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setCancelable(false)
                .setTitle(R.string.runtime_title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute(RequestExecutor.TASK_RUNTIME);
                    }
                })
                .setNegativeButton(R.string.cancel2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                        if (mListener != null)
                            mListener.onDenied();
                    }
                })
                .show();
    }
}
