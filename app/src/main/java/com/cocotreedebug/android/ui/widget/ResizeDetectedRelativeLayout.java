package com.cocotreedebug.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author linqi
 * @description
 */
public class ResizeDetectedRelativeLayout extends RelativeLayout {

    private OnResizeListener mListener;
    private OnSoftInputListener mSoftInputListener;
    private int mSoftInputCriticalValue = 0;

    public ResizeDetectedRelativeLayout(Context context) {
        super(context);
    }

    public ResizeDetectedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    public void setSoftInputListener(int criticalValue, OnSoftInputListener softInputListener) {
        this.mSoftInputCriticalValue = criticalValue;
        this.mSoftInputListener = softInputListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.onResize(w, h, oldw, oldh);
        }
        if (mSoftInputListener != null) {
            if (h <= 0 || oldh <= 0) {
                return;
            }
            if (Math.abs(h - oldh) > mSoftInputCriticalValue) {
                mSoftInputListener.onSoftInputStateChanged(h > oldh);
            }
        }
    }

    public interface OnResizeListener {
        void onResize(int w, int h, int oldw, int oldh);
    }

    public interface OnSoftInputListener {
        /**
         * 软键盘状态改变回调
         *
         * @param isHidden 软键盘是否隐藏
         */
        void onSoftInputStateChanged(boolean isHidden);
    }
}
