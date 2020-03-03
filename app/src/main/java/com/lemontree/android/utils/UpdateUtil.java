package com.lemontree.android.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.Toast;

import com.lemontree.android.R;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.ui.activity.AboutUsActivity;
import com.lemontree.android.uploadUtil.Tools;
import com.lemontree.android.uploadUtil.UrlHostConfig;
import com.update.updatesdk.UpdateAgent;
import com.update.updatesdk.UpdateListener;
import com.update.updatesdk.UpdateReqBean;
import com.update.updatesdk.UpdateResBean;
import com.update.updatesdk.UpdateState;

public class UpdateUtil {

    public static void checkUpdate(Activity context) {
        String checkUrl = UrlHostConfig.getUpdateHost() + NetConstantValue.NET_REQUEST_UPDATE_STATUS;

        UpdateReqBean bean = new UpdateReqBean();
        bean.app_version = Tools.getAppVersion();
        bean.app_name = Tools.getAppName();
        bean.app_clientid = Tools.getChannel();
        bean.user_id = BaseApplication.mUserId;
        bean.phone = BaseApplication.sPhoneNum;
        String appName = context.getString(R.string.app_name);

        UpdateAgent.checkUpdate(context, checkUrl, bean, appName, new UpdateListener() {
            @Override
            public void onUpdateStatuesReturned(int updateStatus, UpdateResBean updateInfo) {
                switch (updateStatus) {
                    case UpdateState.NORMAL_UPDATE:
                        UpdateAgent.showDefaultNormalDialog(context).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
//                                Toast.makeText(context, "Mengunduh...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case UpdateState.FORCE_UPDATE:
                        UpdateAgent.showDefaultForceDialog(context);
                        break;
                    case UpdateState.CONNECT_FAILED:
                        Toast.makeText(context, "Koneksi gagal", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (context instanceof AboutUsActivity) {
                            Toast.makeText(context, "Sudah versi terbaru", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }
}
