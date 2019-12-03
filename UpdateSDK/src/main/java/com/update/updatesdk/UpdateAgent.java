package com.update.updatesdk;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.networklite.NetworkLite;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UpdateAgent {
    private static String sAppName;
    private static String sFinalDownloadLink;
    private static String sCurrentVersion;
    private static String baseHostUrl;
    private static long sDownloadId;
    private static final int HANDLE_DOWNLOAD = 0x001;

    private static OnProgressListener sOnProgressListener;
    private static UpdateListener sUpdateListener;
    private static DownloadChangeObserver sDownloadObserver;
    private static ScheduledExecutorService sScheduledExecutorService;
    private static NetworkLite sNetworkLite;
    private static DownloadManager sDownloadManager;
    private static ProgressDialog sProgressDialog;
    private static UpdateResBean sUpdateResBean;

    public static boolean DEBUG = false;


    public interface OnClickListener {
        void onClick(Dialog dialog, View view);
    }

    public interface OnProgressListener {
        void onProgress(float fraction);
    }

    /**
     * @param context
     * @param url
     */
    public synchronized static void checkUpdate(Context context, final String url, final String packageName, UpdateReqBean bean) {
        checkUpdate(context, url, bean, packageName, null);
    }

    /**
     * @param context
     * @param url
     */

    public synchronized static void checkUpdate(final Context context, final String url, UpdateReqBean updateReqBean, final String appName, UpdateListener listener) {
        if (context == null) {
            return;
        }
        sUpdateListener = listener;
        sAppName = appName;
        NetworkLiteHelper
                .postJson()
                .url(url)
                .content(new Gson().toJson(updateReqBean))
                .build()
                .execute(getNetworkLite(), new GenericCallback<UpdateResBean>() {
                    @Override
                    public void onSuccess(okhttp3.Call call, UpdateResBean response, int id) {
                        if (response != null) {
                            sUpdateResBean = response;
//                            sUpdateResBean.res_msg = ;
//                            sFinalDownloadLink = "https://yn-bao.oss-ap-southeast-1.aliyuncs.com/FlashLoan_pro.apk";
                            if (UpdateResBean.SUCCESS.equals(response.res_code)) {
                                if (sUpdateListener != null) {
                                    sUpdateListener.onUpdateStatuesReturned(UpdateState.NO_UPDATE, response);
                                }
                            } else {
                                if (!TextUtils.isEmpty(response.install_url)) {
                                    sFinalDownloadLink = response.install_url;

                                    if (UpdateResBean.RES_CODE_FORCE_UPDATE.equals(response.res_code)) {
                                        if (sUpdateListener != null) {
                                            sUpdateListener.onUpdateStatuesReturned(UpdateState.FORCE_UPDATE, response);
                                        } else {
                                            showDefaultForceDialog(context);
                                        }
                                    } else if (UpdateResBean.RES_CODE_SUGGEST_UPDATE.equals(response.res_code)) {
                                        if (sUpdateListener != null) {
                                            sUpdateListener.onUpdateStatuesReturned(UpdateState.NORMAL_UPDATE, response);
                                        } else {
                                            showDefaultNormalDialog(context);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(okhttp3.Call call, Exception exception, int id) {
                        if (sUpdateListener != null) {
                            sUpdateListener.onUpdateStatuesReturned(UpdateState.CONNECT_FAILED, null);
                        }
                    }
                });
    }


    public static Dialog showDefaultNormalDialog(Context context) {
        return showDefaultDialog(context, false);
    }

    public static Dialog showDefaultForceDialog(Context context) {
        return showDefaultDialog(context, true);
    }


    /**
     * 使用Okhttp方式下载文件，且展示ProgressBarDialog
     */
    public static void doForceDownload(Context context) {
        AppInnerDownLoder.downLoadApk(context, sFinalDownloadLink, "", sAppName);
    }

    /**
     * 使用DownloadManager方式下载文件，只在通知栏有提示
     */
    public static void doNormalDownload(Context context) {
        sDownloadObserver = new DownloadChangeObserver();
        registerContentObserver(context);
        // 创建下载管理器
        sDownloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        // 检测是否正在下载
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sDownloadId = sp.getLong("downloadId", -1L);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(sDownloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING); // 正在下载
        Cursor c = sDownloadManager.query(query);
        if (c != null && c.moveToFirst()) {
            // 正在下载中，不重新下载
            return;
        } else {
            try {
               /* URL url = new URL(sFinalDownloadLink);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() != 200) {
                    sFinalDownloadLink = sOriginDownloadLink;
                }*/
                // 创建下载请求
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(sFinalDownloadLink));
                // UIUtils.showToastLong("APK_URL = " + APK_URL);
                // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                // 显示在下载界面，即下载后的文件在系统下载管理里显示
                request.setVisibleInDownloadsUi(true);
                // 设置下载标题
                request.setTitle(sAppName);
                // 显示Notification
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                // 设置下载后文件存放的位置，在SDCard/Android/data/应用包名/files/目录下面
                request.setDestinationInExternalFilesDir(context.getApplicationContext(), null, "VersionChecker/" + sAppName + ".apk");

                // 加入下载队列,返回在队列中的id
                sDownloadId = sDownloadManager.enqueue(request);
                // 保存在sp中
                SharedPreferences sp2 = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                sp2.edit().putLong("downloadId", sDownloadId).commit();
                sp2.edit().putString("updateApkName", "VersionChecker/" + sAppName + ".apk").commit();
                unregisterContentObserver(context);
                registerBroadcast(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Dialog showDefaultDialog(final Context context, final boolean isForce) {
        UpdateDialog updateDialog = createUpdateDialog(context, isForce, new OnClickListener() {
            @Override
            public void onClick(Dialog dialog, View view) {
                //点击更新按钮
                if (view.getId() == R.id.btn_update) {
                    dialog.dismiss();
                    if (isForce) {
                        //强更，使用downLoadApk
                        doForceDownload(context);
                    } else {
                        //非强更，可关闭dialog，使用DownloadManager
                        doNormalDownload(context);
                    }
                }
            }
        });

        updateDialog.setOwnerActivity((Activity) context);

        //返回键监听
        updateDialog.setOnBackPressedListener(new UpdateDialog.OnBackPressedListener() {
            @Override
            public void onBackPressed(Dialog dialog) {
                //强更，禁用返回键
                if (!isForce) {
                    dialog.cancel();
                }
            }
        });

        if (updateDialog != null) {
            Activity ownerActivity = updateDialog.getOwnerActivity();
            if (ownerActivity != null && !ownerActivity.isFinishing()) {
                updateDialog.show();
            }
        }
        return updateDialog;
    }

    /**
     * 显示更新提示对话框
     */
    public static UpdateDialog createUpdateDialog(Context context, boolean isForce,
                                                  final OnClickListener listener) {

        final UpdateDialog dialog = new UpdateDialog(context, R.style.dialog_default_style);
        ImageView ivDel = dialog.findViewById(R.id.iv_dialog_update_del);
        TextView tvUpdateLog = dialog.findViewById(R.id.tv_dialog_update_log);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);

        // 关闭按钮
        if (isForce) {
            dialog.setCancelable(false);
            ivDel.setVisibility(View.INVISIBLE);
        } else {
            dialog.setCancelable(false);
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        tvUpdateLog.setText(sUpdateResBean.res_msg);
        // 更新按钮
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(dialog, view);
                }
            }
        });
        return dialog;
    }

    /**
     * 注册ContentObserver
     */
    private static void registerContentObserver(Context context) {
        //observer download change
        if (sDownloadObserver != null) {
            context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, sDownloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private static void unregisterContentObserver(Context context) {
        if (sDownloadObserver != null) {
            context.getContentResolver().unregisterContentObserver(sDownloadObserver);
        }
    }

    /**
     * 注册广播
     */
    private static void registerBroadcast(Context context) {
        /**注册service 广播 1.任务完成时 2.进行中的任务被点击*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(new DownloadCompleteReceiver(), intentFilter);
    }


    public static Handler downLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (sOnProgressListener != null && HANDLE_DOWNLOAD == msg.what) {
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    sOnProgressListener.onProgress(msg.arg1 / (float) msg.arg2);

                    sProgressDialog.incrementProgressBy((int) (msg.arg1 / (float) msg.arg2 * 100));
                }
            }
        }
    };

    private static Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    /**
     * 发送Handler消息更新进度和状态
     */
    private static void updateProgress() {
        int[] bytesAndStatus = getBytesAndStatus(sDownloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private static int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = sDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }


    /**
     * 监听下载进度
     */
    private static class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(downLoadHandler);
            sScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        /**
         * 当所监听的Uri发生改变时，就会回调此方法
         *
         * @param selfChange 此值意义不大, 一般情况下该回调值false
         */
        @Override
        public void onChange(boolean selfChange) {
            sScheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 2, TimeUnit.SECONDS);
        }

    }

    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileProvider";
    }

    private static NetworkLite getNetworkLite() {
        if (sNetworkLite == null) {
            sNetworkLite = NetworkLiteHelper.getNetworkLite();
        }
        return sNetworkLite;
    }
}
