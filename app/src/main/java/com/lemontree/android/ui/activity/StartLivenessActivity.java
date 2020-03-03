package com.lemontree.android.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dfsdk.liveness.DFLivenessSDK;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;
import com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity;
import com.liveness.dflivenesslibrary.liveness.util.Constants;
import com.minchainx.permission.util.PermissionListener;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.Tools;
import com.lemontree.android.uploadUtil.UploadImg;
import com.lemontree.android.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lemontree.android.ui.activity.MainActivity.TAB_APPLY;

public class StartLivenessActivity extends BaseActivity {

    private final int LIVENESS_REQUEST_CODE = 101;//活体识别
    @BindView(R.id.iv_center_image)
    ImageView ivCenterImage;
    @BindView(R.id.tv_hint_text_top)
    TextView tvHintTextTop;
    @BindView(R.id.tv_hint_text_bottom)
    TextView tvHintTextBottom;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String[] mPermissions;
    private int mFailedCount = 0;

    public static Intent createIntent(Context context) {
        return new Intent(context, StartLivenessActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_start_liveness;
    }

    @Override
    protected void initializeView() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限检查
                checkCameraPermission();
            }
        });
    }

    private void checkCameraPermission() {
        mPermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        new Permission(mContext, mPermissions, new PermissionListener() {
            @Override
            public void onGranted() {
                startActionLiveness();
                Log.d("StartLivenessActivity", "onGranted");
            }

            @Override
            public void onDenied() {
                Log.d("StartLivenessActivity", "onDenied");
            }
        });
    }

    //    private String MOTION_SEQUENCE = "STILL BLINK";
    private String MOTION_SEQUENCE = "STILL BLINK MOUTH NOD YAW";

    private void startActionLiveness() {
        Bundle bundle = new Bundle();
        bundle.putString(DFActionLivenessActivity.OUTTYPE, Constants.MULTIIMG);
        bundle.putString(DFActionLivenessActivity.EXTRA_MOTION_SEQUENCE, MOTION_SEQUENCE);

        Intent intent = new Intent();
        intent.setClass(mContext, DFActionLivenessActivity.class);
        intent.putExtras(bundle);
        //设置返回图片结果
        intent.putExtra(DFActionLivenessActivity.KEY_DETECT_IMAGE_RESULT, true);
        intent.putExtra(DFActionLivenessActivity.KEY_HINT_MESSAGE_HAS_FACE, getString(R.string.string_liveness_has_face_and_holdstill_hint));
        intent.putExtra(DFActionLivenessActivity.KEY_HINT_MESSAGE_NO_FACE, getString(R.string.string_liveness_no_face_hint));
        intent.putExtra(DFActionLivenessActivity.KEY_HINT_MESSAGE_FACE_NOT_VALID, getString(R.string.liveness_face_not_valid_hint));
        startActivityForResult(intent, LIVENESS_REQUEST_CODE);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIVENESS_REQUEST_CODE) {
            showToast(resultCode + "");
            if (resultCode == RESULT_OK) {
                //上传照片，上传成功后，跳转到成功页。
                ivCenterImage.setImageDrawable(getResources().getDrawable(R.drawable.bg_start_liveness));
                tvHintTextTop.setText(getResources().getString(R.string.text_liveness_hint));
                tvHintTextBottom.setVisibility(View.VISIBLE);
                btnConfirm.setText(getResources().getString(R.string.text_liveness_btn));

                getAndUploadImg();
            } else {
//                if (data != null) {
//                    int errorCode = data.getIntExtra(DFActionLivenessActivity.KEY_RESULT_ERROR_CODE, -10000);
//                    Log.e("onActivityResult", "action liveness cancel，error code:" + errorCode);
//                }else {
//                    showToast("data is null");
//                }
            }
        }
    }

    //上传活体照片
    private void getAndUploadImg() {
        DFProductResult mResult = ((DFTransferResultInterface) mContext.getApplication()).getResult();
        DFLivenessSDK.DFLivenessImageResult[] imageResultArr = mResult.getLivenessImageResults();

        if (imageResultArr != null && imageResultArr.length > 0) {
            //byte[]->Bitmap->File
            new UploadImg().upload(mContext,
                    Tools.compressImage(BitmapFactory.decodeByteArray(imageResultArr[0].image, 0, imageResultArr[0].image.length), Tools.getFileNameByTime()),
                    Tools.convertByteArrayToFile(mResult.getLivenessEncryptResult()),
                    mFailedCount,
                    new UploadImg.UploadLivenessInfoListener() {
                        @Override
                        public void success() {
                            handler.sendEmptyMessage(1);
                        }

                        @Override
                        public void error() {
                            handler.sendEmptyMessage(2);
                        }
                    });
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.d("aaaaa", "1");

                    startActivity(LivenessSuccessActivity.createIntent(mContext));
                    finish();//成功时finish页面，失败时保留。
                    break;
                case 2:
                    Log.d("aaaaa", "2");

                    startActivity(LivenessFailedActivity.createIntent(mContext));
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        IntentUtils.gotoMainActivity(mContext, TAB_APPLY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}


