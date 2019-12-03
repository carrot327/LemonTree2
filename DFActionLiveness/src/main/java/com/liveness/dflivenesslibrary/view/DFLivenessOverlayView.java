package com.liveness.dflivenesslibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.liveness.dflivenesslibrary.R;

/**
 * Copyright (c) 2017-2019 DEEPFINCH Corporation. All rights reserved.
 **/
public class DFLivenessOverlayView extends View {
    private static final String TAG = DFLivenessOverlayView.class.getSimpleName();

    /**
     * the color of four corner
     */
    private int mBorderColor = Color.TRANSPARENT;

    /**
     * paint of border
     */
    private Paint mBorderPaint;

    protected Paint mXmodePaint;

    /**
     * color of background
     */
    protected int mBackgroundColor = Color.WHITE;

    /**
     * region of background
     */
    protected Path mLockedBackgroundPath;

    /**
     * background's paint
     */
    protected Paint mLockedBackgroundPaint;

    /**
     * scanner region
     */
    protected Rect mScanRect;

    /**
     * scanner resource
     */
    protected Bitmap mScanLineVerticalBitmap;


    protected Bitmap mDisplayBitmap;

    protected Bitmap mAnimationBitmap = null;
    protected int mCurrentLineOffset = 0;
    protected Matrix mRotateMatrix = new Matrix();
    protected Canvas mCanvas = new Canvas();
    private boolean mIsBorderHidden = true;

    protected PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);


    private int mCircleRadius;

    private int mCircleCenterX, mCircleCenterY;

    public DFLivenessOverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        initBackgroundPaint();
        initBorderPaint();

        initialize();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        mScanLineVerticalBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.livenesslibrary_icon_scanner_line, options);

        mXmodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXmodePaint.setFilterBitmap(false);
    }

    protected void initialize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float scale = displayMetrics.heightPixels / 1920.f;
        mCircleRadius = (int) (437 * scale);
        mCircleCenterY = (int) (319 * scale + mCircleRadius);
    }

    private void initBackgroundPaint() {
        mLockedBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLockedBackgroundPaint.clearShadowLayer();
        mLockedBackgroundPaint.setStyle(Paint.Style.FILL);
        mLockedBackgroundPaint.setColor(mBackgroundColor); // 75% black
//        mLockedBackgroundPaint.setAlpha(200);//set BackGround alpha, range of value 0~255
    }

    private void initBorderPaint() {
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.clearShadowLayer();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(14);
    }

    public void setBorderColor(int color) {
        if (mIsBorderHidden == true) {
            return;
        }
        mBorderColor = color;
        if (mBorderPaint != null) {
            mBorderPaint.setColor(mBorderColor);
        }
        postInvalidate();
    }

    public void showBorder() {
        mIsBorderHidden = false;
        setBorderColor(Color.RED);
    }

    public void hideBorder(){
        mIsBorderHidden = true;
        mBorderColor = Color.TRANSPARENT;
        if (mBorderPaint != null) {
            mBorderPaint.setColor(mBorderColor);
        }
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mScanRect == null) {
            return;
        }
        canvas.drawPath(mLockedBackgroundPath, mLockedBackgroundPaint);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mBorderPaint);
        drawVerticalScanLine(canvas);
    }


    public Rect getScanRect() {
        return mScanRect;
    }

    public RectF getScanRectRatio() {
        RectF ratioRectF = new RectF();
        ratioRectF.left = (float)mScanRect.left / getWidth();
        ratioRectF.top = (float)mScanRect.top / getHeight();
        ratioRectF.right = (float)mScanRect.right / getWidth();
        ratioRectF.bottom = (float)mScanRect.bottom / getHeight();
        return ratioRectF;
    }

    protected void initialInfo() {
        mCircleCenterX = getWidth() / 2;
        mScanRect = new Rect(mCircleCenterX - mCircleRadius, mCircleCenterY - mCircleRadius, mCircleCenterX + mCircleRadius, mCircleCenterY + mCircleRadius);
        mLockedBackgroundPath = new Path();
        mLockedBackgroundPath.addRect(new RectF(getLeft(), getTop(), getRight(), getBottom()), Path.Direction.CCW);
        mLockedBackgroundPath.addCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, Path.Direction.CW);

        recycleBitmap(mDisplayBitmap);
        mDisplayBitmap = makeCircleBitmap(mScanRect.width(), mScanRect.height());
        recycleBitmap(mAnimationBitmap);
        mAnimationBitmap = Bitmap.createBitmap(mScanRect.width(), mScanRect.height(), Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mAnimationBitmap);
        mCurrentLineOffset = -mScanRect.height() / 2;

    }
    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);

        initialInfo();
        invalidate();
    }


    protected void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    private Bitmap makeCircleBitmap(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        c.drawCircle(w/2, h/2, w/2, p);
        return bm;
    }

    private Bitmap makeAnimationBmp(int padding) {
        if (mCanvas != null) {
            Canvas canvas = mCanvas;
            mXmodePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(mXmodePaint);

            mRotateMatrix.reset();
            mXmodePaint.setXfermode(null);
            float scaleX = (mScanRect.width() + 0.0f) / mScanLineVerticalBitmap.getWidth();
            mRotateMatrix.setScale(scaleX, scaleX);
            mRotateMatrix.postTranslate(0, padding);
            canvas.drawBitmap(mScanLineVerticalBitmap, mRotateMatrix, mXmodePaint);
            mXmodePaint.setXfermode(mPorterDuffXfermode);
            canvas.drawBitmap(mDisplayBitmap, 0, 0, mXmodePaint);
        }
        return mAnimationBitmap;
    }

    private void drawVerticalScanLine(Canvas canvas) {
        canvas.save();

        if (mDisplayBitmap != null) {
            mCurrentLineOffset += 8;
            canvas.drawBitmap(makeAnimationBmp(mCurrentLineOffset), mScanRect.left, mScanRect.top, mBorderPaint);

            int currentScanLineY = mScanRect.top + mCurrentLineOffset;
            if (currentScanLineY > mScanRect.bottom) {
                mCurrentLineOffset = -mScanRect.height() / 2;
            }
        }

        canvas.restore();

        postInvalidateDelayed(2, mScanRect.left, mScanRect.top, mScanRect.right, mScanRect.bottom);
    }

    public void releaseSource(){
        recycleBitmap(mScanLineVerticalBitmap);
        recycleBitmap(mDisplayBitmap);
        recycleBitmap(mAnimationBitmap);
    }
}
