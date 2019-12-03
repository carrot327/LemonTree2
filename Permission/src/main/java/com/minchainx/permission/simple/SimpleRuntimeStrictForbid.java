package com.minchainx.permission.simple;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.minchainx.permission.R;
import com.minchainx.permission.base.Forbid;
import com.minchainx.permission.base.RequestExecutor;
import com.minchainx.permission.util.Permission;

import java.util.List;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * 永久禁止权限询问弹出该提示对话框。并强制校验权限
 */

public class SimpleRuntimeStrictForbid implements Forbid<String[]> {

    @Override
    public void showForbidden(final Context context, String[] permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.runtime_title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute(RequestExecutor.TASK_SETTING_STRICT_CHECK);
                    }
                })
                .show();
    }
}
