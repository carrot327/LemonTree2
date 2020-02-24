package com.sm.android.ui.activity;

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
import com.sm.android.R;
import com.sm.android.base.BaseActivity;
import com.sm.android.bean.response.NewCustomerProductResBean;
import com.sm.android.utils.IntentUtils;
import com.sm.android.utils.MarkUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sm.android.utils.StringFormatUtils.formatIndMoney;
import static com.sm.android.utils.StringFormatUtils.formatNumber;


public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_amount_range)
    TextView tvAmountRange;
    @BindView(R.id.tv_time_limit)
    TextView tvTimeLimit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_bar_title)
    TextView tvTitleBarTitle;
    @BindView(R.id.btn_apply)
    Button btnApply;

    @BindView(R.id.tv_loan_info_interest)
    TextView tvLoanInfoInterest;
    @BindView(R.id.tv_loan_info_total_get_amount)
    TextView tvLoanInfoTotalGetAmount;
    @BindView(R.id.tv_loan_info_loan_amount)
    TextView tvLoanInfoLoanAmount;

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
        if (!TextUtils.isEmpty(mProductData.rangeLimit) && !TextUtils.isEmpty(mProductData.monthRate)) {
            Double maxBorrowRange = Double.valueOf(mProductData.rangeLimit);
            Double dayRate = Double.valueOf(mProductData.monthRate);
            Double interest = maxBorrowRange * dayRate;
            Double getAmount = maxBorrowRange - interest;
            tvLoanInfoInterest.setText(formatIndMoney(interest + ""));
            tvLoanInfoTotalGetAmount.setText(formatIndMoney(getAmount + ""));
        } else {
            tvLoanInfoInterest.setText("0");
            tvLoanInfoTotalGetAmount.setText(formatIndMoney("0"));
        }

        tvTitleBarTitle.setText(mProductData.productName);

        Glide.with(mContext).load(mProductData.productLogoUrl).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(ivLogo);
        tvProductName.setText(mProductData.productName);
        tvAmountRange.setText(formatNumber(mProductData.rangeLimit));//额度范围
        tvTimeLimit.setText(mProductData.timeLimit);//借款期限
        tvLoanInfoLoanAmount.setText(formatIndMoney(mProductData.rangeLimit));

        if (TextUtils.isEmpty(mProductData.productUrl)) {
            btnApply.setVisibility(View.GONE);
        } else {
            btnApply.setVisibility(View.VISIBLE);
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
//                    IntentUtils.openWebViewActivity(mContext,"https://yn-coco-bao.oss-ap-southeast-1.aliyuncs.com/CocoTree-V1.1.apk");
                    MarkUtil.markCustomerProduct(mProductData.id);
                }

                break;
        }
    }


}
