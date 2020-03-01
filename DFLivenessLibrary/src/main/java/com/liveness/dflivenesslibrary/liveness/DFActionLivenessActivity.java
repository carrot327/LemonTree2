package com.liveness.dflivenesslibrary.liveness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.dfsdk.liveness.DFLivenessSDK;
import com.liveness.dflivenesslibrary.DFAcitivityBase;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;
import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.callback.DFLivenessResultCallback;
import com.liveness.dflivenesslibrary.fragment.DFActionLivenessFragment;
import com.liveness.dflivenesslibrary.fragment.DFProductFragmentBase;
import com.liveness.dflivenesslibrary.liveness.presenter.DFAntiHackProcessPresenter;
import com.liveness.dflivenesslibrary.liveness.presenter.DFCommonResultProcessPresenter;
import com.liveness.dflivenesslibrary.liveness.presenter.DFResultProcessBasePresenter;
import com.liveness.dflivenesslibrary.liveness.util.LivenessUtils;
import com.liveness.dflivenesslibrary.view.DFLivenessLoadingDialogFragment;

import java.io.File;

public class DFActionLivenessActivity extends DFAcitivityBase implements DFLivenessResultCallback, DFResultProcessBasePresenter.DFResultProcessCallback {
    private static final String TAG = "LivenessActivity";

    /**
     * Error loading library file
     */
    public static final int RESULT_CREATE_HANDLE_ERROR = 1001;

    /**
     * Internal error
     */
    public static final int RESULT_INTERNAL_ERROR = 3;

    /**
     * Package name binding error
     */
    public static final int RESULT_SDK_INIT_FAIL_APPLICATION_ID_ERROR = 4;

    /**
     * License expired
     */
    public static final int RESULT_SDK_INIT_FAIL_OUT_OF_DATE = 5;

    /**
     * The file path where the result is saved is passed in
     */
    public static String EXTRA_RESULT_PATH = "com.dfsdk.liveness.resultPath";

    /**
     * The sequence of action motion
     */
    public static final String EXTRA_MOTION_SEQUENCE = "com.dfsdk.liveness.motionSequence";

    /**
     *  output type
     */
    public static final String OUTTYPE = "outType";

    /**
     * set complexity
     */
    public static final String COMPLEXITY = "complexity";

    /**
     * Sets whether to return picture results or not
     */
    public static final String KEY_DETECT_IMAGE_RESULT = "key_detect_image_result";

    /**
     * Sets whether to return the video result, and only the video mode returns
     */
    public static final String KEY_DETECT_VIDEO_RESULT = "key_detect_video_result";

    public static final String KEY_HINT_MESSAGE_HAS_FACE = "com.dfsdk.liveness.message.hasface";
    public static final String KEY_HINT_MESSAGE_NO_FACE = "com.dfsdk.liveness.message.noface";
    public static final String KEY_HINT_MESSAGE_FACE_NOT_VALID = "com.dfsdk.liveness.message.facenotvalid";

    public static final String KEY_ANTI_HACK = "key_anti_hack";

    public static final String LIVENESS_FILE_NAME = "livenessResult";

    private DFLivenessLoadingDialogFragment mProgressDialog;

    private DFResultProcessBasePresenter mResultProcessPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        EXTRA_RESULT_PATH = bundle.getString(EXTRA_RESULT_PATH);
        initPresenter();
        if (EXTRA_RESULT_PATH == null) {
            EXTRA_RESULT_PATH = Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + "liveness" + File.separator;
        }
        File livenessFolder = new File(EXTRA_RESULT_PATH);
        if (!livenessFolder.exists()) {
            livenessFolder.mkdirs();
        }
    }

    private void initPresenter() {
        Intent intent = getIntent();
        boolean antiHackModel = intent.getBooleanExtra(KEY_ANTI_HACK, false);
        boolean isReturnImage = intent.getBooleanExtra(KEY_DETECT_IMAGE_RESULT, false);
        if (antiHackModel) {
            mResultProcessPresenter = new DFAntiHackProcessPresenter(isReturnImage, this);
        } else {
            mResultProcessPresenter = new DFCommonResultProcessPresenter(isReturnImage, this);
        }
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.string_action_liveness);
    }

    @Override
    protected DFProductFragmentBase getFrament() {
        return new DFActionLivenessFragment();
    }

    @Override
    public void saveFinalEncrytFile(byte[] livenessEncryptResult, byte[] videoResult, DFLivenessSDK.DFLivenessImageResult[] imageResult) {
        mResultProcessPresenter.dealLivenessResult(livenessEncryptResult, imageResult);
    }

    @Override
    public void showProgressDialog() {
        initProgressDialog();
        mProgressDialog.show(getFragmentManager(), "DFLivenessLoadingDialogFragment");
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismissAllowingStateLoss();
        }
    }

    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DFLivenessLoadingDialogFragment.getInstance();
        }
    }

    @Override
    public void returnDFProductResult(DFProductResult productResult) {
        Intent intent = new Intent();
        ((DFTransferResultInterface) getApplication()).setResult(productResult);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void deleteLivenessFiles() {
        LivenessUtils.deleteFiles(EXTRA_RESULT_PATH);
    }

    @Override
    public void saveFile(byte[] livenessEncryptResult) {
        LivenessUtils.saveFile(livenessEncryptResult, EXTRA_RESULT_PATH, LIVENESS_FILE_NAME);
    }
}
