package com.cocotree.android.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cocotree.android.R;


/**
 * WebView加载进度条
 *
 * @author evanyu
 * @date 18/5/7
 */
public class WebViewProgressBar extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    private int mProgressMax;
    private int mCurProgress;
    private ValueAnimator mAnimator;

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private OnProgressUpdateListener mProgressUpdateListener;

    public WebViewProgressBar(Context context) {
        super(context);
        initView(context, null);
    }

    public WebViewProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public WebViewProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        int defaultColor = context.getResources().getColor(R.color.Blue500);
        int paintColor = defaultColor;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WebViewProgressBar);
            mProgressMax = array.getInt(R.styleable.WebViewProgressBar_max, 100);
            mCurProgress = array.getInt(R.styleable.WebViewProgressBar_progress, 0);
            paintColor = array.getColor(R.styleable.WebViewProgressBar_progress_color, defaultColor);
            array.recycle();
        }
        mPaint = new Paint();
        mPaint.setColor(paintColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float progressWidth = mWidth * ((float) mCurProgress / mProgressMax);
        canvas.drawRect(0, 0, progressWidth, mHeight, mPaint);
        if (mProgressUpdateListener != null) {
            mProgressUpdateListener.onProgressUpdate(mCurProgress);
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, 0);
    }

    public void setProgress(int progress, int aniDuration) {
        progress = correctProgress(progress);
        if (progress == mCurProgress) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        if (aniDuration > 0) {
            mAnimator = ValueAnimator.ofInt(mCurProgress, progress);
            mAnimator.setDuration(aniDuration);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(getAnimatorUpdateListener());
            mAnimator.start();
        } else {
            mCurProgress = progress;
            postInvalidate();
        }
    }

    public int getProgress() {
        return mCurProgress;
    }

    /**
     * 修正进度
     */
    private int correctProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > mProgressMax) {
            progress = mProgressMax;
        }
        return progress;
    }

    public interface OnProgressUpdateListener {
        void onProgressUpdate(int progress);
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        mProgressUpdateListener = listener;
    }

    @NonNull
    private ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = animation -> {
                if (mAnimator == null) {
                    return;
                }
                int progress = (int) animation.getAnimatedValue();
                if (progress != mCurProgress) {
                    mCurProgress = progress;
                    postInvalidate();
                }
            };
        }
        return mAnimatorUpdateListener;
    }
}