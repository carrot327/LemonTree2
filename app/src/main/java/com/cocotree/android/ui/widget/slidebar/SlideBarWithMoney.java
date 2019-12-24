package com.cocotree.android.ui.widget.slidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cocotree.android.R;
import com.cocotree.android.utils.StringFormatUtils;
import com.cocotree.android.utils.UIUtils;

import java.lang.reflect.Field;


/**
 * @作者:My
 * @创建日期: 2017/7/12 15:21
 * @描述:${TODO}
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 */

public class SlideBarWithMoney extends LinearLayout implements SlideBarApi {
    public static final String TAG = "SlideBarWithText";
    private double minimum = 0;
    private double maximum = 100;
    private OnSlideDrag mOnSlideDrag;
    private int mBarHandlerResourceId;
    private boolean doRound;
    private int mStrokeWidth;
    private int mRoundNum;
    private float mSolidTextSize;
    private float mTipsTextSize;
    private int mBorderColor;
    private int mBottomColor;
    private int mForeColor;
    private int mSolidColor;
    private int mTipsColor;
    private int mTipsResId;
    private TextView mTips;
    private TextView mSolidMini;
    private TextView mSolidMaxi;
    private SeekBar mSeekBar;
    private boolean minimumSetUped;
    private boolean maxSetuped;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private View mView;
    private RelativeLayout mRelativeLayout;
    private String mMinText;
    private String mMaxText;
    private ValueConverter mConvertor;
    private int mHorizPadding;
    private int mPaddingHorizWithTips;
    private boolean mDragableState = true;
    private int oldsign;
    private int selectMode;
    private int clickCount;
//    private boolean mSeekBarCanClick;

    public SlideBarWithMoney(Context context) {
        this(context, null);
    }

    public SlideBarWithMoney(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SlideBarWithMoney(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideBarWithMoney(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SlideBarWithText);

        mBarHandlerResourceId = attributes.getResourceId(R.styleable.SlideBarWithText_barresid, 0);
        doRound = attributes.getBoolean(R.styleable.SlideBarWithText_doround, true);
        mStrokeWidth = attributes.getDimensionPixelSize(R.styleable.SlideBarWithText_borderstrong, 0);
        mRoundNum = attributes.getDimensionPixelSize(R.styleable.SlideBarWithText_roundnum, (int) SlideBarUtils.dp2px(context, 5));
        mSolidTextSize = attributes.getDimension(R.styleable.SlideBarWithText_solidtextsize, SlideBarUtils.sp2px(context, 14));
        mTipsTextSize = attributes.getDimension(R.styleable.SlideBarWithText_tipstextsize, SlideBarUtils.sp2px(context, 14));
        mBorderColor = attributes.getColor(R.styleable.SlideBarWithText_bordercolor, Color.TRANSPARENT);
        mBottomColor = attributes.getColor(R.styleable.SlideBarWithText_bottomcolor, Color.BLACK);
        mForeColor = attributes.getInt(R.styleable.SlideBarWithText_forecolore, 0);
        mSolidColor = attributes.getColor(R.styleable.SlideBarWithText_solidtextcolor, Color.BLACK);
        mTipsColor = attributes.getColor(R.styleable.SlideBarWithText_tipstextcolor, Color.RED);
        mTipsResId = attributes.getResourceId(R.styleable.SlideBarWithText_tipsresid, R.drawable.tips_blue);
        mMinText = attributes.getString(R.styleable.SlideBarWithText_minitext);
        mMaxText = attributes.getString(R.styleable.SlideBarWithText_maxitext);
        mPaddingHorizWithTips = attributes.getDimensionPixelSize(R.styleable.SlideBarWithText_paddingHor, 0);
        selectMode = attributes.getInt(R.styleable.SlideBarWithText_selectmode, 0);
        attributes.recycle();
        mTips = new TextView(context);
        mSolidMini = new TextView(context);
        mSolidMaxi = new TextView(context);
        mRelativeLayout = new RelativeLayout(context);
        mSeekBar = new SeekBar(context, null, R.style.style_seekBar);
        setupSeekBar();
        setupTextViews();
        addToParent();
    }

    private void addToParent() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams tipsLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(mTips, tipsLayoutParams);
        LayoutParams seekBarLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mSeekBar, seekBarLayoutParams);

        seekBarLayoutParams.topMargin = (int) SlideBarUtils.dp2px(getContext(), 6);
        seekBarLayoutParams.leftMargin = mPaddingHorizWithTips;
        seekBarLayoutParams.rightMargin = mPaddingHorizWithTips;
        LayoutParams relativeLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayoutParams.topMargin = (int) SlideBarUtils.dp2px(getContext(), 5);
        relativeLayoutParams.leftMargin = mPaddingHorizWithTips;
        relativeLayoutParams.rightMargin = mPaddingHorizWithTips;
        addView(mRelativeLayout, relativeLayoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        mMeasuredWidth = getMeasuredWidth();
//        mMeasuredHeight = getMeasuredHeight();
//        if (mMeasuredWidth < SlideBarUtils.dp2px(getContext(), 100)) {
//            throw new RuntimeException("to smalle space for width");
//        }
//        switch (MeasureSpec.getMode(heightMeasureSpec)) {
//            case MeasureSpec.UNSPECIFIED:
//            case MeasureSpec.AT_MOST:
//                mMeasuredHeight = (int) (SlideBarUtils.dp2px(getContext(), 200) + 0.5f);
//                break;
//        }
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mMeasuredWidth, MeasureSpec.EXACTLY)
//                , MeasureSpec.makeMeasureSpec(mMeasuredHeight, MeasureSpec.EXACTLY));
    }

    public void setPercentProgress(int percent) {
        if (percent >= 0 && percent <= 100) {
            mSeekBar.setProgress(percent);
            updateTipsPosition(percent);
        }
    }

   /* public boolean getSeekBarCanClick() {
        return mSeekBarCanClick;
    }

    public void setSeekBarCanClick(boolean seekBarCanClick) {
        mSeekBarCanClick = seekBarCanClick;
    }*/

    public void setValueProgress(double value) {
        if (value >= minimum && value <= maximum) {
            int progress = (int) ((value - minimum) * 100 / (maximum - minimum) + 0.5f);
            setPercentProgress(progress);
        } else if (value < minimum) {
            setPercentProgress(0);
        } else if (value > maximum) {
            setPercentProgress(100);
        }
    }

    private void updateTipsPosition(final int progress) {
        if (mSeekBar.getWidth() <= 0) {
            mSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    translateTipsX(progress);
                }
            });
        } else {
            translateTipsX(progress);
        }
    }

    private void translateTipsX(int progress) {
        int width = mSeekBar.getWidth() - mHorizPadding * 2;
        if (width == 0) {
            mSeekBar.measure(0, 0);
            width = mSeekBar.getMeasuredWidth() - mHorizPadding * 2;
        }
//        int i = mPaddingHorizWithTips + width / 100 * progress   + mSeekBar.getThumb().getIntrinsicWidth() / 2 + mHorizPadding/2 - mTips.getWidth() / 2;

        int i = (int) (mPaddingHorizWithTips + mSeekBar.getThumb().getIntrinsicWidth() / 2 + (float) width / 100 * progress);

        int result = i < 0 ? 0 : ((i + mTips.getWidth()) > getWidth() ? (getWidth() - mTips.getWidth()) : i);
        mTips.setTranslationX(result);
    }

    private void setupSeekBar() {
        switch (mForeColor) {
            case 0:
                mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist_blue));
                mSeekBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist_blue));
                break;
            case 1:
                mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist));
                mSeekBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist));
                break;
            case 2:
                mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist_orange));
                mSeekBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist_orange));
                break;
            default:
                mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist));
                mSeekBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.seekbar_layerlist));
        }


        mSeekBar.setIndeterminate(false);
        mSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thumb));
        mHorizPadding = mSeekBar.getThumb().getIntrinsicWidth() / 4;
        mSeekBar.setThumbOffset(mHorizPadding);
        mSeekBar.setFocusable(true);
        mSeekBar.setClickable(false);
        mSeekBar.setPadding(mHorizPadding, 0, mHorizPadding, 0);
        try {
            Class<?> superclass = mSeekBar.getClass().getSuperclass().getSuperclass();
            Field mMaxHeight = superclass.getDeclaredField("mMaxHeight");
            mMaxHeight.setAccessible(true);
            mMaxHeight.set(mSeekBar, UIUtils.dip2px(20));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTipsPosition(progress);
                clickCount++;

                if (progress >= 0 && progress <= 50) {
//                    mTips.setBackgroundResource(R.drawable.tip_left);

                    updateTipsPosition(progress);
                } else if (progress > 50 && progress <= 100) {
//                    mTips.setBackgroundResource(R.drawable.tip_right);

                    int tipsWidth = mTips.getWidth();
                    int seekBarWidth = mSeekBar.getWidth();
                    int moveProgress = (tipsWidth * 100) / seekBarWidth;
                    updateTipsPosition(progress - moveProgress - 4);
                }

                if (mOnSlideDrag != null) {
                    mOnSlideDrag.onDraging(mTips, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                if (mOnSlideDrag != null) {
                    mOnSlideDrag.onDragStart(mTips);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnSlideDrag != null) {
                    mOnSlideDrag.onDragEnd(mTips, mSeekBar.getProgress() == 100, clickCount);
                }
                clickCount = 0;
            }
        });
    }

    private void setupTextViews() {
//        mTips.setBackgroundResource(mTipsResId);
        mTips.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipsTextSize);
        mTips.setTextColor(mTipsColor);
        TextPaint paint = mTips.getPaint();
        paint.setFakeBoldText(true);
        mTips.setGravity(Gravity.CENTER);
        mTips.setText("");
        mTips.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mSolidMaxi.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSolidTextSize);
        mSolidMaxi.setTextColor(mSolidColor);
        mSolidMaxi.getPaint().setFakeBoldText(true);
        mSolidMaxi.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(mMaxText) && TextUtils.isEmpty(mSolidMaxi.getText())) {
            mSolidMaxi.setText(mMaxText);
        }
        RelativeLayout.LayoutParams mSolidMaxiLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSolidMaxi.setLayoutParams(mSolidMaxiLayoutParams);

        mSolidMini.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSolidTextSize);
        mSolidMini.getPaint().setFakeBoldText(true);
        mSolidMini.setTextColor(mSolidColor);
        mSolidMini.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(mMinText) && TextUtils.isEmpty(mSolidMini.getText())) {
            mSolidMini.setText(mMinText);
        }
        RelativeLayout.LayoutParams mSolidMiniLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSolidMini.setLayoutParams(mSolidMiniLayoutParams);
        RelativeLayout.LayoutParams miniLayoutParmas = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        miniLayoutParmas.leftMargin = mHorizPadding;
        mRelativeLayout.addView(mSolidMini, miniLayoutParmas);
        RelativeLayout.LayoutParams maxiLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        maxiLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        maxiLayoutParams.rightMargin = mHorizPadding;
        mRelativeLayout.addView(mSolidMaxi, maxiLayoutParams);

    }

    public void setValueConvertor(ValueConverter convertor) {
        mConvertor = convertor;
    }

    private void updateView() {
        if (mConvertor != null) {
            if (minimumSetUped) {
                mSolidMini.setText(mConvertor.converterValue(minimum));
            }
            if (maxSetuped) {
                mSolidMaxi.setText(mConvertor.converterValue(maximum));
            }
            if (minimumSetUped && maxSetuped) {
                mTips.setText(mConvertor.converterValue(getCurrentValue()));
            }
        } else {
            if (minimumSetUped) {
                mSolidMini.setText(StringFormatUtils.indMoneyFormat(minimum));
            }
            if (maxSetuped) {
                mSolidMaxi.setText(StringFormatUtils.indMoneyFormat(maximum));
            }
            if (minimumSetUped && maxSetuped) {
                mTips.setText(StringFormatUtils.indMoneyFormat(getCurrentValue()));
            }
        }
    }

    //TODO
    public void setDragableState(boolean dragableState) {
        mDragableState = dragableState;
    }

    @Override
    public double getMaximum() {
        return maximum;
    }

    @Override
    public void setMaximum(double maxmun) {
        maxSetuped = true;
        this.maximum = maxmun;
        updateView();
    }

    @Override
    public double getMinimum() {
        return minimum;
    }

    @Override
    public void setMinimum(double minimum) {
        minimumSetUped = true;
        this.minimum = minimum;
        updateView();
    }

    @Override
    public float getCurrentPerception() {
        return mSeekBar.getProgress();
    }

    @Override
    public double getCurrentValue() {
        if (mConvertor == null) {
            double moneyAmountValue = 0;
            int percent = mSeekBar.getProgress();
            if (percent >= 0 && percent <= 16.6) {
                moneyAmountValue = 5000000;
            } else if (percent > 16.6 && percent <= 16.6 * 2) {
                moneyAmountValue = 6000000;
            } else if (percent > 16.6 * 2 && percent <= 16.6 * 3) {
                moneyAmountValue = 7000000;
            } else if (percent > 16.6 * 3 && percent <= 16.6 * 4) {
                moneyAmountValue = 8000000;
            } else if (percent > 16.6 * 4 && percent <= 16.6 * 5) {
                moneyAmountValue = 9000000;

            } else {
                moneyAmountValue = 10000000;

            }
            return moneyAmountValue;
//            return mSeekBar.getProgress() * (maximum - minimum) / 100 + minimum;
        } else {
            return mConvertor.ValueFromPercent(mSeekBar.getProgress());
        }
    }

    @Override
    public void setOnDragCallBack(OnSlideDrag l) {
        mOnSlideDrag = l;
    }

    public float getTipsTextSize() {
        return mTipsTextSize;
    }

    public void setTipsTextSize(float tipsTextSize) {
        mTipsTextSize = tipsTextSize;
    }

    public String getMinText() {
        return mMinText;
    }

    public void setMinText(String minText) {
        mMinText = minText;
    }

    public String getMaxText() {
        return mMaxText;
    }

    public void setMaxText(String maxText) {
        mMaxText = maxText;
    }

    public interface ValueConverter {
        String converterValue(double value);

        double ValueFromPercent(int progress);
    }
}
