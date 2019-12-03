
package com.minchainx.permission.simple;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.minchainx.permission.R;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.base.RequestExecutor;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/4/29.
 */
public class SimpleInstallRationale implements Rationale<File> {

    @Override
    public void showRationale(Context context, File data, final RequestExecutor executor) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.runtime_title_dialog)
                .setMessage(R.string.message_install_failed)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute(RequestExecutor.TASK_INSTALL);
                    }
                })
                .setNegativeButton(R.string.cancel2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                })
                .show();
    }
}