package com.sm.android.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sm.android.R;
import com.sm.android.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 通用对话框
 * Created by evanyu on 16/11/10.
 */
public class CommonDialog extends BaseDialog implements View.OnClickListener {

    public static final int STYLE_HORIZONTAL_BTNS = 1;
    public static final int STYLE_VERTICAL_BTNS = 2;

    public static final int STYLE_BTN_PLCAE_DEFAULT = STYLE_HORIZONTAL_BTNS;

    private Context mContext;
    private TextView mTvTitle, mTvMessage;
    private FrameLayout mFrameContent;
    private MaxHeightScrollView mMaxHeightScrollView;
    private Button mBtnNegative, mBtnPositive;
    private OnClickListener mBtnNegativeClickListener, mBtnPositiveClickListener;
    private View mContentView;
    private ImageView mIvLoading;
    private RelativeLayout mRlPositiveBtnPart;
    private Subscription mCountDwonTask;

    private List<DialogInterface.OnShowListener> mOnShowListeners;
    private List<DialogInterface.OnDismissListener> mOnDismissListeners;

    private DialogInterface.OnShowListener mInnerOnShowListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            if (mOnShowListeners != null) {
                for (DialogInterface.OnShowListener listener : mOnShowListeners) {
                    listener.onShow(dialog);
                }
            }
        }
    };

    private DialogInterface.OnDismissListener mInnerOnDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mOnDismissListeners != null) {
                for (DialogInterface.OnDismissListener listener : mOnDismissListeners) {
                    listener.onDismiss(dialog);
                }
            }
        }
    };
    private final LinearLayout mLlNegative;

    private CommonDialog(Context context, final Builder builder) {
        super(context);
        // 加载布局
        int layoutResId;
        if (builder != null) {
            switch (builder.btnPlaceStyle) {
                case STYLE_VERTICAL_BTNS: // 按钮纵向排列
                    layoutResId = R.layout.dialog_common_vertical_btns;
                    break;
                case STYLE_HORIZONTAL_BTNS: // 按钮横向排列
                default:
                    layoutResId = R.layout.dialog_common_horizontal_btns;
                    break;
            }
        } else {
            layoutResId = R.layout.dialog_common_horizontal_btns;
        }

        super.setContentView(layoutResId);

        mContext = context;

        // find views
        mTvTitle = (TextView) findViewById(R.id.tv_dialog_common_title);
        mTvMessage = (TextView) findViewById(R.id.tv_dialog_common_message);
        mFrameContent = (FrameLayout) findViewById(R.id.frame_dialog_common_content);
        mMaxHeightScrollView = (MaxHeightScrollView) findViewById(R.id.mhsv_dialog_common_content);
        mBtnNegative = (Button) findViewById(R.id.btn_dialog_common_negative);
        mBtnPositive = (Button) findViewById(R.id.btn_dialog_common_positive);
        mIvLoading = (ImageView) findViewById(R.id.iv_dialog_common_loading);
        mRlPositiveBtnPart = (RelativeLayout) findViewById(R.id.rl_dialog_common_positive_btn_part);
        mLlNegative = findViewById(R.id.ll_btn_negative);

        // set listeners
        if (mBtnNegative != null) {
            mBtnNegative.setOnClickListener(this);
        }
        if (mBtnPositive != null) {
            mBtnPositive.setOnClickListener(this);
        }
        super.setOnShowListener(mInnerOnShowListener);
        super.setOnDismissListener(mInnerOnDismissListener);

        // handle builder
        if (builder != null) {
            setTitle(builder.title);
            setMessage(builder.message);
            setNegativeButton(builder.negativeBtnText, builder.mBtnNegativeClickListener);
            setPositiveButton(builder.positiveBtnText, builder.mBtnPositiveClickListener);
            if (builder.contentView != null) {
                setContentView(builder.contentView);
            }
            if (!builder.useMaxHieght) {
                mMaxHeightScrollView.setMaxHeight(-1);
                mMaxHeightScrollView.invalidate();
            }
            mTvMessage.setGravity(builder.contentGravity);
         /*   // 操作倒计时
            if (builder.countDownTime > 0) {
                addOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        startCountDown(builder.countDownTime);
                    }
                });
            }*/
        }

        addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopCownDwon();
            }
        });
    }

    /* */

    /**
     * 在按钮上显示倒计时
     *//*
    public void startCountDown(int countDownTime) {
        stopCownDwon();
        if (mCountDwonTask == null) {
            mCountDwonTask = RxCountDown.create(countDownTime).subscribe(new Action1<Integer>() {
                String btnText = mBtnPositive.getText().toString();

                @Override
                public void call(Integer count) {
                    if (count > 0) {
                        mBtnPositive.setEnabled(false);
                        mBtnPositive.setText(btnText + "(" + count + "s)");
                    } else {
                        mBtnPositive.setEnabled(true);
                        mBtnPositive.setText(btnText);
                        stopCownDwon();
                    }
                }
            });
        }
    }*/
    private void stopCownDwon() {
        if (mCountDwonTask != null && !mCountDwonTask.isUnsubscribed()) {
            mCountDwonTask.unsubscribe();
            mCountDwonTask = null;
        }
    }

    /**
     * please use {@link CommonDialog#addOnShowListener(DialogInterface.OnShowListener)}
     */
    @Deprecated
    @Override
    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        // super.setOnShowListener(listener);
        addOnShowListener(listener);
    }

    /**
     * please use {@link CommonDialog#addOnDismissListener(DialogInterface.OnDismissListener)}
     */
    @Deprecated
    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        // super.setOnDismissListener(listener);
        addOnDismissListener(listener);
    }

    public void addOnShowListener(DialogInterface.OnShowListener listener) {
        if (mOnShowListeners == null) {
            mOnShowListeners = new ArrayList<>();
        }
        mOnShowListeners.add(listener);
    }

    public void addOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (mOnDismissListeners == null) {
            mOnDismissListeners = new ArrayList<>();
        }
        mOnDismissListeners.add(listener);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        setContentView(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void setContentView(int layoutResId) {
        if (layoutResId < 0) {
            throw new RuntimeException("layoutResID less than zero");
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        setContentView(inflater.inflate(layoutResId, null));
    }

    @Override
    public void setContentView(View view) {
        if (view == null) {
            throw new RuntimeException("view is null");
        }
        if (mFrameContent != null) {
            mFrameContent.removeAllViews();
            mFrameContent.addView(view);
            mContentView = view;
        }
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setTitle(CharSequence text) {
        if (mTvTitle != null) {
            mTvTitle.setText(text);
        }
    }

    public void setMessage(CharSequence text) {
        if (mTvMessage != null) {
            mTvMessage.setText(text);
        }
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public Button getNegativeBtn() {
        return mBtnNegative;
    }

    public Button getPositiveBtn() {
        return mBtnPositive;
    }

    public void setNegativeButton(CharSequence text, OnClickListener listener) {
        if (mBtnNegative != null) {
            mBtnNegative.setText(text);
        }
        mBtnNegativeClickListener = listener;
    }

    public void setPositiveButton(CharSequence text, OnClickListener listener) {
        if (mBtnPositive != null) {
            mBtnPositive.setText(text);
        }
        mBtnPositiveClickListener = listener;
    }

    public void setNegativeButtonEnabled(boolean enabled) {
        if (mBtnNegative != null) {
            mBtnNegative.setEnabled(enabled);
        }
    }

    public void setPositiveButtonEnabled(boolean enabled) {
        if (mBtnPositive != null) {
            mBtnPositive.setEnabled(enabled);
        }
    }

    public void setBtnVisibility(boolean negativeVisibale, boolean positiveVisible) {
        if (mBtnNegative != null) {
            if (negativeVisibale) {
                mLlNegative.setVisibility(View.VISIBLE);
//                mBtnNegative.setVisibility(View.VISIBLE);
            } else {
                mLlNegative.setVisibility(View.GONE);
//                mBtnNegative.setVisibility(View.GONE);
            }
        }
        if (mBtnPositive != null) {
            if (positiveVisible) {
                mRlPositiveBtnPart.setVisibility(View.VISIBLE);
            } else {
                mRlPositiveBtnPart.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_common_negative: // 左边/下边的按钮
                if (mBtnNegativeClickListener != null) {
                    mBtnNegativeClickListener.onClick(this, v);
                }
                break;
            case R.id.btn_dialog_common_positive: // 右边/上边的按钮
                if (mBtnPositiveClickListener != null) {
                    mBtnPositiveClickListener.onClick(this, v);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgressBar() {
//        if (mBtnPositive != null && mIvLoading != null && mContext != null) {
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ani_loading_blue);
//            if (drawable != null) {
//                mBtnPositive.setVisibility(View.INVISIBLE);
//                mIvLoading.setImageDrawable(drawable);
//                ((AnimationDrawable) drawable).start();
//            }
//
//        }
    }

    @Override
    public void hideProgressBar() {
        if (mBtnPositive != null && mIvLoading != null) {
            mBtnPositive.setVisibility(View.VISIBLE);
            mIvLoading.setImageDrawable(null);
        }
    }

    public static class Builder {

        int btnPlaceStyle = CommonDialog.STYLE_BTN_PLCAE_DEFAULT; // 按钮的摆放样式
        Context mContext;
        CharSequence title;
        CharSequence message;
        CharSequence negativeBtnText, positiveBtnText;
        OnClickListener mBtnNegativeClickListener, mBtnPositiveClickListener;
        View contentView;
        boolean useMaxHieght = true; // 是否使用最大高度（默认使用）
        int countDownTime; // 倒计时时间
        int contentGravity = Gravity.LEFT;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(CharSequence text) {
            this.title = text;
            return this;
        }

        public Builder setMessage(CharSequence text) {
            this.message = text;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.negativeBtnText = text;
            this.mBtnNegativeClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.positiveBtnText = text;
            this.mBtnPositiveClickListener = listener;
            return this;
        }

        public Builder setContentView(int layoutResId) {
            if (layoutResId < 0) {
                throw new RuntimeException("layoutResID less than zero");
            }
            contentView = LayoutInflater.from(mContext).inflate(layoutResId, null);
            return this;
        }

        public Builder setContentView(View view) {
            contentView = view;
            return this;
        }

        public Builder setMaxHieghtEnable(boolean useMaxHieght) {
            this.useMaxHieght = useMaxHieght;
            return this;
        }

        /**
         * 设置倒计时时间
         *
         * @param second 倒计时总时长（单位：秒）
         */
        public Builder setCountDownTime(int second) {
            this.countDownTime = second;
            return this;
        }

        /**
         * 设置按钮的摆放样式
         *
         * @param btnPlaceStyle use {@link CommonDialog#STYLE_HORIZONTAL_BTNS or {@link CommonDialog#STYLE_VERTICAL_BTNS}}
         * @return
         */
        public Builder setBtnPlaceStyle(int btnPlaceStyle) {
            if (btnPlaceStyle == CommonDialog.STYLE_HORIZONTAL_BTNS
                    || btnPlaceStyle == CommonDialog.STYLE_VERTICAL_BTNS) {
                this.btnPlaceStyle = btnPlaceStyle;
            }
            return this;
        }

        public Builder setContentGravity(int gravity) {
            this.contentGravity = gravity;
            return this;
        }


        public CommonDialog build() {
            return new CommonDialog(mContext, this);
        }

        public CommonDialog show() {
            CommonDialog dialog = build();
            if (dialog != null) {
                dialog.show();
            }
            return dialog;
        }
    }
}
