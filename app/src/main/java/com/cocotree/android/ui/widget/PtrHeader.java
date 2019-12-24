package com.cocotree.android.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocotree.android.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by evanyu on 17/8/1.
 */

public class PtrHeader extends FrameLayout implements RefreshHeader {

    private static final String PTR_TEXT_PULL_DOWN = "Pull To Perbarui";
    private static final String PTR_TEXT_REFRESHING = "Perbarui...";
//    private static final String PTR_TEXT_REFRESHING = "Refreshing...";
    private static final String PTR_TEXT_RELEASE = "Refresh Now";
    private static final String PTR_TEXT_REFRESH_SUCCESS = "Perbarui Success";
    private static final String PTR_TEXT_REFRESH_FAILED = "Perbarui Failed";

    private View mHeaderView;
    private TextView mTvHeaderText;
    private ImageView mIvArrow;
    //    private ImageView mIvLoading;
    private AnimationDrawable mLoadingAnimation;
//    private SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;

    public PtrHeader(Context context) {
        this(context, null, 0);
    }

    public PtrHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.ptr_header, this, true);
        mTvHeaderText = (TextView) findViewById(R.id.tv_ptr_header_text);
        mIvArrow = (ImageView) findViewById(R.id.iv_ptr_header_arrow);
//        mIvLoading = (ImageView) findViewById(R.id.iv_ptr_header_loading);


//        mLoadingAnimation = (AnimationDrawable) mIvLoading.getBackground();
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
    }

    @NonNull
    @Override
    public View getView() {
        if (mHeaderView != null) {
            return mHeaderView;
        } else {
            return this;
        }
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    @Override
    public void onHorizontalDrag(float v, int i, int i1) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        if (mLoadingAnimation != null) {
            mLoadingAnimation.start();
        }
    }

    @Override
    public int onFinish(RefreshLayout refreshLayout, boolean success) {
        if (mLoadingAnimation != null) {
            mLoadingAnimation.stop();
//            mIvLoading.setVisibility(INVISIBLE);
        }
//        if (success) {
//            mTvHeaderText.setText(PTR_TEXT_REFRESH_SUCCESS);
//        } else {
//            mTvHeaderText.setText(PTR_TEXT_REFRESH_FAILED);
//        }
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh: // 下拉刷新
                mTvHeaderText.setText(PTR_TEXT_PULL_DOWN);
//                mIvLoading.setVisibility(GONE);
                mIvArrow.setVisibility(View.VISIBLE);
                mIvArrow.animate().rotation(0);
                break;
            case Refreshing: // 刷新中
                mTvHeaderText.setText(PTR_TEXT_REFRESHING);
//                mIvLoading.setVisibility(VISIBLE);
                mIvArrow.setVisibility(GONE);
                break;
            case ReleaseToRefresh: // 释放刷新
                mTvHeaderText.setText(PTR_TEXT_RELEASE);
                mIvArrow.animate().rotation(180);
                break;
        }
    }
}
