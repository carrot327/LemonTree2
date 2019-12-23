package com.sm.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sm.android.R;
import com.sm.android.bean.response.RecommendDialogResBean;
import com.sm.android.utils.IntentUtils;
import com.sm.android.utils.MarkUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeRecommendDialogOne extends Dialog {
    @BindView(R.id.iv_logo_left)
    ImageView ivLogoLeft;
    @BindView(R.id.tv_product_name_left)
    TextView tvProductNameLeft;
    @BindView(R.id.tv_range_left)
    TextView tvRangeLeft;
    @BindView(R.id.tv_speed_left)
    TextView tvSpeedLeft;
    @BindView(R.id.tv_month_rate_left)
    TextView tvMonthRateLeft;
    @BindView(R.id.iv_dialog_close)
    ImageView ivDialogClose;
    @BindView(R.id.btn_recommend_left)
    Button btnRecommendLeft;


    private Context mContext;
    private CloseListener mListener;
    private RecommendDialogResBean.ProductListBean mLeftData, mRightData;


    public HomeRecommendDialogOne(Context context, List<RecommendDialogResBean.ProductListBean> listData, CloseListener closeListener) {
        super(context);
        mContext = context;
        mListener = closeListener;
        if (listData.size() == 1) {
            mLeftData = listData.get(0);
        } else if (listData.size() == 2) {
            mLeftData = listData.get(0);
            mRightData = listData.get(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_recommend_dialog_one);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //Left
        Glide.with(mContext).load(mLeftData.productLogoUrl).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(ivLogoLeft);
        tvProductNameLeft.setText(mLeftData.productName);
        tvRangeLeft.setText(mLeftData.rangeLimit);
        tvSpeedLeft.setText(mLeftData.loanSpeed);
        tvMonthRateLeft.setText("月费率" + mLeftData.monthRate);
        btnRecommendLeft.setText(mLeftData.buttonTitle);
    }

    @OnClick({R.id.iv_dialog_close, R.id.btn_recommend_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dialog_close:
                mListener.close(HomeRecommendDialogOne.this, view);
                break;
            case R.id.btn_recommend_left:
                IntentUtils.openWebViewActivity(mContext, mLeftData.productUrl);
                MarkUtil.markCustomerProduct(mLeftData.id);
                this.dismiss();
                break;
        }
    }

    public interface CloseListener {
        void close(Dialog dialog, View view);
    }
}
