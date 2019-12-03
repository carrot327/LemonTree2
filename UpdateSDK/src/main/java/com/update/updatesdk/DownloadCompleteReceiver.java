package com.update.updatesdk;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 下载完成事件广播接收器
 * Created by evanyu on 16/8/29.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 当前已下载完成的事件id
        long downloadCompleteId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        // 下载更新文件的事件id
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        long downloadId = sp.getLong("downloadId", -1L);
        String name = sp.getString("updateApkName", "");
        // 检查是否是自己的下载队列 id, 有可能是其他应用的
        if (downloadId != downloadCompleteId) {
            return;
        }
        if (context.getExternalFilesDir(null) != null) {
            String path = context.getExternalFilesDir(null).getPath() + "/" + name;
            promptInstall(context, path);
        }
    }

    private void promptInstall(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(context, UpdateAgent.getFileProviderName(context), new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            Uri fileUri = Uri.fromFile(new File(filePath));
            Intent it = new Intent();
            it.setAction(Intent.ACTION_VIEW);
            it.setDataAndType(fileUri, "application/vnd.android.package-archive");
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 防止打不开应用
            context.startActivity(it);
        }
    }
}
