package com.lemontree.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

import com.lemontree.android.R;


/**
 * 可限定最大高度的ScrollView
 * Created by evanyu on 17/2/13.
 */
public class MaxHeightScrollView extends NestedScrollView {

    private int mMaxHeight = -1; // 最大高度

    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
            mMaxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightScrollView_mhsv_maxHeight, -1);
            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0 && MeasureSpec.getSize(heightMeasureSpec) > mMaxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置最大高度
     * 单位:px，－1代表不可用
     */
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }

}
