package com.liveness.dflivenesslibrary.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.camera.CameraBase;
import com.liveness.dflivenesslibrary.view.DFLivenessOverlayView;

public abstract class DFProductFragmentBase extends Fragment {

    protected SurfaceView mSurfaceView;
    protected DFLivenessOverlayView mOverlayView;
    protected View mRootView;
    protected CameraBase mCameraBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResourceId(), container,
                false);
        mSurfaceView = (SurfaceView) mRootView.findViewById(R.id.surfaceViewCamera);
        mOverlayView = (DFLivenessOverlayView) mRootView.findViewById(R.id.id_ov_mask);
        initCamera();
        initialize();
        return mRootView;
    }

    protected void initialize() {

    }

    private void initCamera() {
        if (mSurfaceView != null) {
            mCameraBase = new CameraBase(getActivity(), mSurfaceView, mOverlayView, isFrontCamera());
        }
    }

    protected void showToast(int showHintResId) {
        String showHint = getActivity().getString(showHintResId);
        Toast.makeText(getActivity(), showHint, Toast.LENGTH_SHORT).show();
    }

    protected boolean isFrontCamera() {
        return true;
    }

    protected abstract int getLayoutResourceId();

    protected void finishActivity(){
        Activity activity = getActivity();
        if (activity != null){
            activity.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
