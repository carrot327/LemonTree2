package com.liveness.dflivenesslibrary.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfsdk.liveness.DFLivenessSDK;
import com.liveness.dflivenesslibrary.DFAcitivityBase;
import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.callback.DFLivenessResultCallback;
import com.liveness.dflivenesslibrary.fragment.model.DFLivenessOverlayModel;
import com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity;
import com.liveness.dflivenesslibrary.liveness.util.Constants;
import com.liveness.dflivenesslibrary.liveness.util.DFSensorManager;
import com.liveness.dflivenesslibrary.liveness.util.LivenessUtils;
import com.liveness.dflivenesslibrary.process.DFActionLivenessProcess;
import com.liveness.dflivenesslibrary.utils.DFBitmapUtils;
import com.liveness.dflivenesslibrary.utils.DFMediaPlayer;
import com.liveness.dflivenesslibrary.utils.DFViewShowUtils;
import com.liveness.dflivenesslibrary.view.CircleTimeView;
import com.liveness.dflivenesslibrary.view.DFGifView;
import com.liveness.dflivenesslibrary.view.DFAlertDialog;
import com.liveness.dflivenesslibrary.view.TimeViewContoller;

import java.util.HashMap;
import java.util.Map;

import static com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity.EXTRA_MOTION_SEQUENCE;
import static com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity.KEY_HINT_MESSAGE_FACE_NOT_VALID;
import static com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity.KEY_HINT_MESSAGE_HAS_FACE;
import static com.liveness.dflivenesslibrary.liveness.DFActionLivenessActivity.KEY_HINT_MESSAGE_NO_FACE;

public class DFActionLivenessFragment extends DFProductFragmentBase {
    private static final String TAG = "DFLivenessFragment";

    private static final int CURRENT_ANIMATION = -1;

    protected DFActionLivenessProcess mProcess;
    protected DFGifView mGvView;
    protected TextView mNoteTextView;
    protected ViewGroup mVGBottomDots;
    private RelativeLayout mWaitDetectView;
    private View mAnimFrame;
    protected CircleTimeView mTimeView;
    private TextView mTvCountdown;
    protected String[] mDetectList = null;
    private DFAlertDialog mDialog;
    protected TimeViewContoller mTimeViewContoller;
    protected DFLivenessSDK.DFLivenessMotion[] mMotionList = null;
    protected DFSensorManager mSensorManger;
    private boolean mIsStart = false;
    protected DFLivenessResultCallback mLivenessResultFileProcess;

    protected String mHasFaceHint, mNoFaceHint, mFaceNotValid;
    private Map<String, DFLivenessOverlayModel> mFaceHintMap;
    private String mFaceProcessResult;

    private DFMediaPlayer mMediaPlayer;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_liveness_fragment;
    }

    protected void initView() {
        mGvView = (DFGifView) mRootView.findViewById(R.id.id_gv_play_action);
        mNoteTextView = (TextView) mRootView.findViewById(R.id.noteText);
        mWaitDetectView = (RelativeLayout) mRootView.findViewById(R.id.wait_time_notice);
        mWaitDetectView.setVisibility(View.VISIBLE);
        mAnimFrame = mRootView.findViewById(R.id.anim_frame);
        mVGBottomDots = (ViewGroup) mRootView.findViewById(R.id.viewGroup);
        if (mDetectList != null && mDetectList.length >= 1) {
            for (int i = 0; i < mDetectList.length; i++) {
                TextView tvBottomCircle = new TextView(getActivity());
                tvBottomCircle.setBackgroundResource(R.drawable.drawable_liveness_detect_bottom_cicle_bg_selector);
                tvBottomCircle.setEnabled(i == 0 ? false : true);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(8),
                        dp2px(8));
                layoutParams.leftMargin = dp2px(8);
                mVGBottomDots.addView(tvBottomCircle, layoutParams);
            }
        }

        mTimeView = (CircleTimeView) mRootView.findViewById(R.id.time_view);
        mTimeViewContoller = new TimeViewContoller(getActivity(), mTimeView);

        mRootView.findViewById(R.id.id_ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DFActionLivenessActivity) getActivity()).onErrorHappen(DFAcitivityBase.RESULT_BACK_PRESSED);
            }
        });

        mHasFaceHint = getActivity().getIntent().getStringExtra(KEY_HINT_MESSAGE_HAS_FACE);
        mNoFaceHint = getActivity().getIntent().getStringExtra(KEY_HINT_MESSAGE_NO_FACE);
        mFaceNotValid = getActivity().getIntent().getStringExtra(KEY_HINT_MESSAGE_FACE_NOT_VALID);

        initFaceHintMap();

        mMediaPlayer = new DFMediaPlayer();

        onFaceDetectCallback(-1, false, false);
    }

    @Override
    protected void initialize() {
        mLivenessResultFileProcess = (DFLivenessResultCallback) getActivity();
        mSensorManger = new DFSensorManager(getActivity());
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String motionString = bundle.getString(EXTRA_MOTION_SEQUENCE);
            if (motionString != null) {
                mDetectList = LivenessUtils.getDetectActionOrder(motionString);
                setMotionList(motionString);
            }
        }

        mProcess = new DFActionLivenessProcess(getActivity(), mCameraBase);
        mProcess.registerLivenessDetectCallback(mLivenessListener);
        initView();
    }

    private void setMotionList(String motionString) {
        mMotionList = LivenessUtils.getMctionOrder(motionString);
    }

    private void refreshCountdownView(int count) {
//        DFViewShowUtils.refreshText(mTvCountdown, String.valueOf(count));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManger.registerListener(mSensorEventListener);
        if (mIsStart) {
            LivenessUtils.logI(TAG, "onResume", "showDialog");
            showFailDialog();
        }
    }

    private void restartAnimationAndLiveness() {
        setLivenessState(false);
        mLivenessResultFileProcess.deleteLivenessFiles();
        if (mDetectList.length >= 1) {
            View childAt = mVGBottomDots.getChildAt(0);
            childAt.setEnabled(false);
        }
        startAnimation(CURRENT_ANIMATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        LivenessUtils.logI(TAG, "onPause");
        mSensorManger.unregisterListener(mSensorEventListener);
        stopGuideAudio();
        if (mProcess != null) {
            mProcess.onPause();
        }
    }

    protected DFActionLivenessProcess.OnLivenessCallBack mLivenessListener = new DFActionLivenessProcess.OnLivenessCallBack() {
        @Override
        public void onLivenessDetect(final int value, final int status, byte[] livenessEncryptResult,
                                     byte[] videoResult, DFLivenessSDK.DFLivenessImageResult[] imageResult) {
            LivenessUtils.logI(TAG, "onLivenessDetect===DFMediaPlayer==" + "***value***" + value);
            onLivenessDetectCallBack(value, status, livenessEncryptResult, videoResult, imageResult);
        }

        @Override
        public void onFaceDetect(int value, boolean hasFace, boolean faceValid) {
//            LivenessUtils.logI(TAG, "onFaceDetect" + "***value***" + value + "=hasFace=" + hasFace + "=faceValid=" + faceValid);
            onFaceDetectCallback(value, hasFace, faceValid);
        }

        @Override
        public void onDetectCountdown(int count) {
            LivenessUtils.logI(TAG, "onDetectCountdown" + count);
            refreshCountdownView(count);
        }
    };

    protected void removeDetectWaitUI() {
        mWaitDetectView.setVisibility(View.GONE);
        setLivenessState(false);
        onLivenessDetectCallBack(mMotionList[0].getValue(), 0, null, null, null);
    }

    protected void showDetectWaitUI() {
        mWaitDetectView.setVisibility(View.VISIBLE);
        mIsStart = true;
        if (mTimeViewContoller != null) {
            mTimeViewContoller.setCallBack(null);
        }
    }

    private void setLivenessState(boolean pause) {
        if (null == mProcess) {
            return;
        }
        if (pause) {
            mProcess.stopLiveness();
        } else {
            mProcess.startLiveness();
        }
    }

    protected void onLivenessDetectCallBack(final int value, final int status, final byte[] livenessEncryptResult, final byte[] videoResult, final DFLivenessSDK.DFLivenessImageResult[] imageResult) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value == DFLivenessSDK.DFLivenessMotion.BLINK.getValue()) {
                    updateUi(R.raw.raw_liveness_detect_blink, status);
                    startCountdown();
                } else if (value == DFLivenessSDK.DFLivenessMotion.MOUTH.getValue()) {
                    updateUi(R.raw.raw_liveness_detect_mouth, status);
                    startCountdown();
                } else if (value == DFLivenessSDK.DFLivenessMotion.NOD.getValue()) {
                    updateUi(R.raw.raw_liveness_detect_nod, status);
                    startCountdown();
                } else if (value == DFLivenessSDK.DFLivenessMotion.YAW.getValue()) {
                    updateUi(R.raw.raw_liveness_detect_yaw, status);
                    startCountdown();
                } else if (value == DFLivenessSDK.DFLivenessMotion.HOLD_STILL.getValue()) {
                    updateUi(R.raw.raw_liveness_detect_holdstill, status);
                } else if (value == Constants.LIVENESS_SUCCESS) {
                    mIsStart = false;
                    LivenessUtils.logI(TAG, "onLivenessDetectCallBack", "LIVENESS_SUCCESS");
                    stopPreview();
                    stopGvView();
                    stopGuideAudio();
                    stopCountDown();
                    if (imageResult != null) {
                        for (DFLivenessSDK.DFLivenessImageResult itemImageResult : imageResult) {
                            byte[] image = itemImageResult.image;
                            Bitmap cropBitmap = null;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            cropBitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
                            Bitmap bmp = DFBitmapUtils.cropResultBitmap(cropBitmap, mCameraBase.getPreviewWidth(), mCameraBase.getPreviewHeight(), mCameraBase.getScanRatio());
                            itemImageResult.detectImage = DFBitmapUtils.convertBmpToJpeg(bmp);
                            DFBitmapUtils.recyleBitmap(cropBitmap);
                            DFBitmapUtils.recyleBitmap(bmp);
                        }
                    }
                    mLivenessResultFileProcess.saveFinalEncrytFile(livenessEncryptResult, videoResult, imageResult);
                } else if (value == Constants.LIVENESS_TRACKING_MISSED) {
                    LivenessUtils.logI(TAG, "onLivenessDetect" + "track miss" + "DFMediaPlayer");
                    stopGuideAudio();
                    LivenessUtils.logI(TAG, "LIVENESS_TRACKING_MISSED", "showDialog");
                    showFailDialog();
                    mLivenessResultFileProcess.saveFile(livenessEncryptResult);
                } else if (value == Constants.LIVENESS_MORE_THAN_FACE) {
                    LivenessUtils.logI(TAG, "onLivenessDetect" + "LIVENESS_MORE_THAN_FACE");
                    stopGuideAudio();
                    LivenessUtils.logI(TAG, "LIVENESS_MORE_THAN_FACE", "showDialog");
                    showMoreThanFaceDialog();
                    mLivenessResultFileProcess.saveFile(livenessEncryptResult);
                } else if (value == Constants.LIVENESS_TIME_OUT) {
                    LivenessUtils.logI(TAG, "LIVENESS_TIME_OUT", "showDialog");
                    showFailDialog();
                    mLivenessResultFileProcess.saveFile(livenessEncryptResult);
                } else if (value == Constants.DETECT_BEGIN_WAIT) {
                    updateUi(R.raw.raw_liveness_detect_holdstill, 0);
                    showDetectWaitUI();
                } else if (value == Constants.DETECT_END_WAIT) {
                    removeDetectWaitUI();
                }
            }
        });
    }

    protected void onFaceDetectCallback(int value, boolean hasFace, boolean faceValid) {
//        if (value == DFLivenessSDK.DFLivenessMotion.HOLD_STILL.getValue()) {
        String action = String.valueOf(value);
        String hasFaceShow = DFViewShowUtils.booleanTrans(hasFace);
        String faceValidShow = DFViewShowUtils.booleanTrans(faceValid);
        String faceProcessResult = action.concat("_").concat(hasFaceShow).concat("_").concat(faceValidShow);
        if (!TextUtils.equals(mFaceProcessResult, faceProcessResult)) {
            mFaceProcessResult = faceProcessResult;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DFLivenessOverlayModel overlayModel = mFaceHintMap.get(mFaceProcessResult);
                    if (overlayModel != null) {
                        String hintID = overlayModel.getShowHint();
                        int borderColor = overlayModel.getBorderColor();
                        int audioGuideResId = overlayModel.getAudioGuideResId();
                        if (borderColor != -1) {
                            mOverlayView.showBorder();
                            mOverlayView.setBorderColor(borderColor);
                        }
                        if (audioGuideResId != 0) {
                            startGuideAudio(audioGuideResId);
                        }
                        refreshHintText(hintID);
                    }
                }
            });

        }
//        } else {
//            mOverlayView.hideBorder();
//        }
    }

    protected void refreshHintText(String hintStr) {
        if (hintStr != null) {
            mNoteTextView.setText(hintStr);
        }
    }

    protected int isBottomDotsVisibility() {
        return View.VISIBLE;
    }

    protected void showIndicateView() {
        if (mGvView != null) {
            mGvView.setVisibility(View.VISIBLE);
        }
        if (mVGBottomDots != null) {
            mVGBottomDots.setVisibility(isBottomDotsVisibility());
        }
        if (mNoteTextView != null) {
            mNoteTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isDialogShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    private void hideTimeContoller() {
        if (mTimeViewContoller != null) {
            mTimeViewContoller.hide();
        }
    }

    private void hideIndicateView() {
        if (mGvView != null) {
            mGvView.setVisibility(View.GONE);
        }
        if (mVGBottomDots != null) {
            mVGBottomDots.setVisibility(View.GONE);
        }
        if (mNoteTextView != null) {
            mNoteTextView.setVisibility(View.GONE);
        }
    }

    private void stopGvView() {
        if (mGvView != null) {
            mGvView.setPaused(true);
        }
    }

    protected void showFailDialog() {
        showDialog(R.string.livenesslibrary_failure_dialog_title_track_miss);
    }

    protected void showMoreThanFaceDialog() {
        showDialog(R.string.livenesslibrary_failure_dialog_title_more_than_face);
    }

    protected void showDialog(int dialogId) {
        if (isDialogShowing()) {
            return;
        }
        if (mDetectList.length >= 1) {
            for (int i = 0; i < mDetectList.length; i++) {
                View childAt = mVGBottomDots.getChildAt(i);
                if (childAt != null) {
                    childAt.setEnabled(true);
                }
            }
        }

        hideTimeContoller();
        hideIndicateView();
        String dialogTitle = getStringWithID(dialogId);
        mDialog = new DFAlertDialog(getActivity()).builder().setCancelable(false).
                setTitle(dialogTitle).setNegativeButton(getStringWithID(R.string.cancel), new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ((DFActionLivenessActivity) getActivity()).onErrorHappen(DFAcitivityBase.RESULT_BACK_PRESSED);
            }
        }).setPositiveButton(getStringWithID(R.string.restart_preview), new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showIndicateView();
                mProcess.registerLivenessDetectCallback(mLivenessListener);
                restartAnimationAndLiveness();
                mProcess.initNv21Data();
                mFaceProcessResult = null;
            }
        });
        if (getActivity().isFinishing()) {
            return;
        }
        mDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProcess != null) {
            mProcess.registerLivenessDetectCallback(null);
            mProcess.stopDetect();
            mProcess.exitDetect();

            mProcess = null;
        }
        if (mTimeViewContoller != null) {
            mTimeViewContoller.setCallBack(null);
            mTimeViewContoller = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    protected void startAnimation(int animation) {
        if (animation != CURRENT_ANIMATION) {
            mGvView.setMovieResource(animation);
            if (isDialogShowing()) {
                return;
            }
        }
    }

    private void startCountdown() {
        if (mTimeViewContoller != null) {
            mTimeViewContoller.start();
            mTimeViewContoller.setCallBack(new TimeViewContoller.CallBack() {
                @Override
                public void onTimeEnd() {
                    stopGuideAudio();
                    mProcess.onTimeEnd();
                }
            });
        }
    }

    private void stopCountDown() {
        if (mTimeViewContoller != null) {
            mTimeViewContoller.stop();
            mTimeViewContoller.setCallBack(null);
        }
    }

    protected void startGuideAudio(int audioResId) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setMediaSource(getActivity(), audioResId, false);
            mMediaPlayer.start();
        }
    }

    protected void stopGuideAudio() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    protected void stopPreview() {
        if (mProcess != null) {
            mProcess.stopPreview();
        }
    }

    protected void updateUi(int animationId, int number) {
        LivenessUtils.logI(TAG, "mNoteTextView", "number", number);
        if (animationId != 0) {
            startAnimation(animationId);
        }
        if (number >= 0) {
            resetVGBottomDots();
            View childAt = mVGBottomDots.getChildAt(number);
            childAt.setEnabled(false);
        }
    }

    private void resetVGBottomDots() {
        if (mVGBottomDots != null) {
            int childCount = mVGBottomDots.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = mVGBottomDots.getChildAt(i);
                childAt.setEnabled(true);
            }
        }
    }

    private String getStringWithID(int id) {
        return getResources().getString(id);
    }

    protected SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            mProcess.addSequentialInfo(event.sensor.getType(), event.values);
        }
    };

    private int dp2px(float dpValue) {
        int densityDpi = this.getResources().getDisplayMetrics().densityDpi;
        return (int) (dpValue * (densityDpi / 160));
    }

    private void initFaceHintMap() {
        mFaceHintMap = new HashMap<>();

        mFaceHintMap.put("-1_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, 0));

        mFaceHintMap.put("4_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("4_0_1", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("4_1_0", new DFLivenessOverlayModel(mFaceNotValid, Color.RED, R.raw.audio_liveness_detect_move_away));
        mFaceHintMap.put("4_1_1", new DFLivenessOverlayModel(mHasFaceHint, Color.GREEN, R.raw.audio_liveness_detect_holdstill));

        mFaceHintMap.put("0_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("0_0_1", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("0_1_0", new DFLivenessOverlayModel(mFaceNotValid, Color.RED, R.raw.audio_liveness_detect_move_away));
        mFaceHintMap.put("0_1_1", new DFLivenessOverlayModel(getStringWithID(R.string.note_blink), Color.GREEN, R.raw.audio_liveness_detect_blink));

        mFaceHintMap.put("1_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("1_0_1", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("1_1_0", new DFLivenessOverlayModel(mFaceNotValid, Color.RED, R.raw.audio_liveness_detect_move_away));
        mFaceHintMap.put("1_1_1", new DFLivenessOverlayModel(getStringWithID(R.string.note_mouth), Color.GREEN, R.raw.audio_liveness_detect_mouth));

        mFaceHintMap.put("2_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("2_0_1", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("2_1_0", new DFLivenessOverlayModel(mFaceNotValid, Color.RED, R.raw.audio_liveness_detect_move_away));
        mFaceHintMap.put("2_1_1", new DFLivenessOverlayModel(getStringWithID(R.string.note_yaw), Color.GREEN, R.raw.audio_liveness_detect_yaw));

        mFaceHintMap.put("3_0_0", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("3_0_1", new DFLivenessOverlayModel(mNoFaceHint, Color.RED, R.raw.audio_liveness_detect_no_face));
        mFaceHintMap.put("3_1_0", new DFLivenessOverlayModel(mFaceNotValid, Color.RED, R.raw.audio_liveness_detect_move_away));
        mFaceHintMap.put("3_1_1", new DFLivenessOverlayModel(getStringWithID(R.string.note_nod), Color.GREEN, R.raw.audio_liveness_detect_nod));
    }

}
