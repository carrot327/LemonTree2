package com.cocotree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cocotree.android.R;
import com.cocotree.android.base.BaseActivity;
import com.cocotree.android.bean.response.NewCustomerProductResBean;
import com.cocotree.android.utils.IntentUtils;
import com.cocotree.android.utils.MarkUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_amount_range_text)
    TextView tvAmountRangeText;
    @BindView(R.id.tv_amount_range)
    TextView tvAmountRange;
    @BindView(R.id.tv_time_limit_text)
    TextView tvTimeLimitText;
    @BindView(R.id.tv_time_limit)
    TextView tvTimeLimit;
    @BindView(R.id.tv_month_rate_text)
    TextView tvMonthRateText;
    @BindView(R.id.tv_month_rate)
    TextView tvMonthRate;
    @BindView(R.id.tv_apply_condition)
    TextView tvApplyCondition;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_bar_title)
    TextView tvTitleBarTitle;
    @BindView(R.id.iv_tag_rid_out)
    ImageView ivTag;
    @BindView(R.id.btn_apply)
    Button btnApply;

    public static final String PRODUCT_DATA = "product_data";
    private NewCustomerProductResBean.ProductListBean mProductData;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initializePrepareData() {
        super.initializePrepareData();
        mProductData = (NewCustomerProductResBean.ProductListBean) getIntent().getSerializableExtra(PRODUCT_DATA);
    }

    @Override
    protected void initializeView() {
        tvTitleBarTitle.setText(mProductData.productName);

        Glide.with(mContext).load(mProductData.productLogoUrl).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(ivLogo);
        tvProductName.setText(mProductData.productName);
        tvAmountRange.setText(mProductData.rangeLimit);//额度范围
        tvTimeLimit.setText(mProductData.timeLimit);//借款期限
        tvMonthRate.setText(mProductData.monthRate);
        tvApplyCondition.setText(mProductData.applyCondition);
        if (TextUtils.isEmpty(mProductData.productUrl)) {
            btnApply.setVisibility(View.GONE);
            ivTag.setVisibility(View.VISIBLE);
        } else {
            btnApply.setVisibility(View.VISIBLE);
            ivTag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.iv_back, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_apply:
                if (!TextUtils.isEmpty(mProductData.productUrl)) {
                    IntentUtils.openWebViewActivity(mContext, mProductData.productUrl);
                    MarkUtil.markCustomerProduct(mProductData.id);
                }

                break;
        }
    }


}
