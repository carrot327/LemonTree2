package com.cocotreedebug.android.ui.activity;

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

import com.deepfinch.liveness.DFLivenessSDK;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;
import com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity;
import com.liveness.dflivenesslibrary.liveness.util.Constants;
import com.minchainx.permission.util.PermissionListener;
import com.cocotreedebug.android.R;
import com.cocotreedebug.android.base.BaseActivity;
import com.cocotreedebug.android.uploadUtil.Permission;
import com.cocotreedebug.android.uploadUtil.Tools;
import com.cocotreedebug.android.uploadUtil.UploadImg;
import com.cocotreedebug.android.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cocotreedebug.android.ui.activity.MainActivity.TAB_APPLY;

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

//    private String MOTION_SEQUENCE = "HOLD_STILL BLINK";
    private String MOTION_SEQUENCE = "HOLD_STILL BLINK MOUTH NOD YAW";

    private void startActionLiveness() {
        Bundle bundle = new Bundle();
        bundle.putString(DFActionLivenessActivity.OUTTYPE, Constants.MULTIIMG);
        //HOLD_STILL(静止), BLINK(眨眼), MOUTH（张嘴）, NOD（点头）, YAW（摇头）, 各个动作以空格隔开。 第一个动作必须为HOLD_STILL。
        bundle.putString(DFActionLivenessActivity.EXTRA_MOTION_SEQUENCE, MOTION_SEQUENCE);

        Intent intent = new Intent();
        intent.setClass(mContext, DFActionLivenessActivity.class);
        intent.putExtras(bundle);
        //设置返回图片结果
        intent.putExtra(DFActionLivenessActivity.KEY_DETECT_IMAGE_RESULT, true);
        startActivityForResult(intent, LIVENESS_REQUEST_CODE);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //成功只有一种情况：上传照片成功和防hack成功。  失败有两种情况：识别失败和上传失败，此时都需重做。
        if (requestCode == LIVENESS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //上传照片，上传成功后，跳转到成功页。
                ivCenterImage.setImageDrawable(getResources().getDrawable(R.drawable.bg_start_liveness));
                tvHintTextTop.setText(getResources().getString(R.string.text_liveness_hint));
                tvHintTextBottom.setVisibility(View.VISIBLE);
                btnConfirm.setText(getResources().getString(R.string.text_liveness_btn));

                getAndUploadImg();
            } else {
                //记录失败的次数，如果超过2次，则开启静默识别（只是省掉后续四个步骤）
                mFailedCount++;
                if (mFailedCount >= 2) {
                    MOTION_SEQUENCE = "HOLD_STILL BLINK";
                }
//                startActivity(LivenessFailedActivity.createIntent(mContext));
                //update view
                ivCenterImage.setImageDrawable(getResources().getDrawable(R.drawable.bg_liveness_failed));
                tvHintTextTop.setText(getResources().getString(R.string.text_analysis_failed));
                tvHintTextBottom.setVisibility(View.INVISIBLE);
                btnConfirm.setText(getResources().getString(R.string.text_try_again));
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
                            Log.d("aaaaa", "uploadLivenessInfo success");
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


