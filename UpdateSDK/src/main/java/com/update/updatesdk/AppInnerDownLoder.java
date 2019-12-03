package com.update.updatesdk;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;


public class AppInnerDownLoder {
    public final static String SD_FOLDER = Environment.getExternalStorageDirectory() + "/VersionChecker/";
    private static final String TAG = AppInnerDownLoder.class.getSimpleName();

    /**
     * 从服务器中下载APK
     */
    @SuppressWarnings("unused")
    public static void downLoadApk(final Context mContext, final String finalDownURL, final String originDownURL, final String downloadPackageName) {

        final CommonProgressDialog pd; // 进度条对话框
        pd = new CommonProgressDialog(mContext);
        pd.setCancelable(false);// 必须一直下载完，不可取消
        pd.setProgressNumberFormat("%1.2fM/%2.2fM");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("Mengunduh paket instalasi, harap tunggu");
        pd.setTitle("Upgrade Version");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = downloadFile(mContext, finalDownURL, originDownURL, downloadPackageName, pd);
                    sleep(3000);
                    installApk(mContext, file);
                    // 结束掉进度条对话框
                    pd.dismiss();
                } catch (Exception e) {
                    pd.dismiss();
                }
            }
        }.start();
    }

    /**
     * 从服务器下载最新更新文件
     *
     * @param mContext
     * @param path     下载路径
     * @param pd       进度条
     * @return
     * @throws Exception
     */
    private static File downloadFile(Context mContext, String path, String originPath, String downloadPackageName, CommonProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() != 200) {
            url = new URL(originPath);
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setConnectTimeout(5000);
        // 获取到文件的大小
//        pd.setMax(conn.getContentLength()/1024/1024);
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        String fileName;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            fileName = SD_FOLDER + appName + ".apk";
//        } else {
//            fileName = mContext.getFilesDir() + "/VersionChecker/" + appName + ".apk";
//        }
        fileName = mContext.getExternalFilesDir(null) + "/VersionChecker/" + downloadPackageName + ".apk";
        File file = new File(fileName);
        // 目录不存在创建目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            fileName = mContext.getFilesDir() + "/VersionChecker/" + downloadPackageName + ".apk";
            file = new File(fileName);
            // 目录不存在创建目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            // 获取当前下载量
            pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return file;
    }

    /**
     * 安装apk
     */
    private static void installApk(Context mContext, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(mContext, getFileProviderName(mContext), file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            Uri fileUri = Uri.fromFile(file);
            Intent it = new Intent();
            it.setAction(Intent.ACTION_VIEW);
            it.setDataAndType(fileUri, "application/vnd.android.package-archive");
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 防止打不开应用
            mContext.startActivity(it);
        }
    }

    /**
     * 获取应用程序版本（versionName）
     *
     * @return 当前应用的版本号
     */

    private static double getLocalVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "获取应用程序版本失败，原因：" + e.getMessage());
            return 0.0;
        }

        return Double.valueOf(info.versionName);
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileProvider";
    }
}
