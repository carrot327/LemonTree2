package com.cocotreedebug.android.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cocotreedebug.android.R;


/**
 * 标题栏控件
 * Created by evanyu on 16/11/9.
 */
public class AppTitleBar extends FrameLayout implements View.OnClickListener {

    public static final int NO_ICON = -1;

    private TextView mTvTitle;
    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageButton mBtnBack;
    private ImageButton mBtnRight;
    private View mLeftButton;
    private View mBottomDivider;
    private OnClickListener mBtnBackListener;
    private OnClickListener mBtnRightListener;
    private OnClickListener mRightTextListener;
    private OnClickListener mTitleListener;

    public AppTitleBar(Context context) {
        this(context, null);
    }

    public AppTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected int getTitleBarLayoutResId() {
        return R.layout.titlebar_white;
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(getTitleBarLayoutResId(), this, true);

        mTvTitle = findViewById(R.id.tv_titlebar_title);
        mTvRight = findViewById(R.id.tv_titlebar_text_right);
        mTvLeft = findViewById(R.id.tv_titlebar_text_left);
        mBtnBack = findViewById(R.id.btn_titlebar_back);
        mBtnRight = findViewById(R.id.iv_titlebar_right);
        mLeftButton = findViewById(R.id.ll_titlebar_left_button);
        mBottomDivider = findViewById(R.id.view_titlebar_bottom_divider);

        // 设置监听
        if (mLeftButton != null) {
            mLeftButton.setOnClickListener(this);
        } else if (mBtnBack != null) {
            mBtnBack.setOnClickListener(this);
        }
        if (mBtnRight != null) {
            mBtnRight.setOnClickListener(this);
        }
        if (mTvRight != null) {
            mTvRight.setOnClickListener(this);
        }
        if (mTvTitle != null) {
            mTvTitle.setOnClickListener(this);
        }

        // 解析属性值
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
//        String title = ta.getString(R.styleable.CustomTitleBar_title_name);
//        String rightText = ta.getString(R.styleable.CustomTitleBar_right_text);
//        ta.recycle();
//        setTitle(title);
//        setRightText(rightText);
        // 设置背景色
//        setBackgroundColor(Color.WHITE);
    }

    /**
     * 获取标题
     */
    public CharSequence getTitle() {
        return mTvTitle == null ? null : mTvTitle.getText();
    }

    /**
     * 设置标题
     */
    public AppTitleBar setTitle(CharSequence text) {
        if (mTvTitle != null && text != null) {
            mTvTitle.setText(text);
        }
        return this;
    }

    /**
     * 设置标题的监听事件
     */
    public AppTitleBar setOnTitleClickListener(OnClickListener listener) {
        mTitleListener = listener;
        return this;
    }

    /**
     * 设置左侧返回按钮图标
     */
    public AppTitleBar setBtnBackIcon(int resId) {
        if (mBtnBack != null) {
            if (resId > 0) {
                mBtnBack.setImageResource(resId);
                mBtnBack.setVisibility(View.VISIBLE);
            } else {
                mBtnBack.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 设置左侧返回按钮是否可见
     */
    public AppTitleBar setBtnBackVisible(boolean visible) {
        if (mBtnBack != null) {
            mBtnBack.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    /**
     * 设置返回按钮的文本和监听事件
     */
    public AppTitleBar setOnBtnBackClickListener(OnClickListener listener) {
        mBtnBackListener = listener;
        return this;
    }

    /**
     * 设置左侧按钮的文本内容
     */
    public AppTitleBar setLeftText(String text) {
        mTvLeft.setText(text);
        return this;
    }

    /**
     * 设置左侧按钮的文本内容
     */
    public AppTitleBar setLeftTextColor(int color) {
        mTvLeft.setTextColor(color);
        return this;
    }

    /**
     * 设置返回按钮的图标和监听器
     */
    public AppTitleBar setBtnBack(int resId, OnClickListener listener) {
        setBtnBackIcon(resId);
        setOnBtnBackClickListener(listener);
        return this;
    }

    /**
     * 设置右侧的文本内容
     */
    public AppTitleBar setRightText(CharSequence text) {
        if (mTvRight != null) {
            mTvRight.setText(text);
        }
        return this;
    }

    /**
     * 设置右边文本控件的监听事件
     */
    public AppTitleBar setOnRightTextClickListener(OnClickListener listener) {
        mRightTextListener = listener;
        return this;
    }

    /**
     * 设置右边文本控件的文本和监听事件
     */
    public AppTitleBar setRightText(CharSequence rightText, OnClickListener listener) {
        setRightText(rightText);
        setOnRightTextClickListener(listener);
        return this;
    }

    public AppTitleBar setRightTextEnabled(boolean enable) {
        if (mTvRight != null) {
            mTvRight.setEnabled(enable);
        }
        return this;
    }

    /**
     * 设置右侧按钮图标
     */
    public AppTitleBar setBtnRightIcon(int resId) {
        if (mBtnRight != null) {
            if (resId > 0) {
                mBtnRight.setImageResource(resId);
                mBtnRight.setVisibility(View.VISIBLE);
            } else {
                mBtnRight.setVisibility(View.INVISIBLE);
            }
        }
        return this;
    }

    public AppTitleBar setOnBtnRightClickListener(OnClickListener listener) {
        mBtnRightListener = listener;
        return this;
    }

    public AppTitleBar setBtnRight(int resId, OnClickListener listener) {
        if (mBtnRight != null) {
            setBtnRightIcon(resId);
            setOnBtnRightClickListener(listener);
        }
        return this;
    }

    public AppTitleBar setBottomDividerVisiable(boolean visiable) {
        if (mBottomDivider != null) {
            mBottomDivider.setVisibility(visiable ? View.VISIBLE : View.INVISIBLE);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_titlebar_left_button:
            case R.id.btn_titlebar_back:
                if (mBtnBackListener != null) {
                    mBtnBackListener.onClick(v);
                }
                break;
            case R.id.iv_titlebar_right:
                if (mBtnRightListener != null) {
                    mBtnRightListener.onClick(v);
                }
                break;
            case R.id.tv_titlebar_text_right:
                if (mRightTextListener != null) {
                    mRightTextListener.onClick(v);
                }
                break;
            case R.id.tv_titlebar_title:
                if (mTitleListener != null) {
                    mTitleListener.onClick(v);
                }
                break;
            default:
                break;
        }
    }

}
