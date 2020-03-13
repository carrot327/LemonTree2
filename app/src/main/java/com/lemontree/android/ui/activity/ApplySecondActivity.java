package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.WorkInfoReqBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ApplySecondActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_work_category)
    AutoCompleteTextView tvWorkCategory;
    @BindView(R.id.tv_salary_range)
    AutoCompleteTextView tvSalaryRange;
    @BindView(R.id.et_company_name)
    TextInputEditText etCompanyName;
    @BindView(R.id.et_company_address)
    TextInputEditText etCompanyAddress;
    @BindView(R.id.et_company_phone)
    TextInputEditText etCompanyPhone;


    public static Intent createIntent(Context context) {
        return new Intent(context, ApplySecondActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_second;
    }

    @Override
    protected void initializeView() {
        String[] WORK_CATEGORY = getResources().getStringArray(R.array.work_category);
        String[] SALARY_RANGE = getResources().getStringArray(R.array.salary_range);

        tvWorkCategory.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, WORK_CATEGORY));
        tvSalaryRange.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, SALARY_RANGE));

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                checkContent();
                break;
        }
    }

    private void checkContent() {
        boolean hasError = false;
        if (TextUtils.isEmpty(tvWorkCategory.getText().toString())) {
            tvWorkCategory.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        }
        if (TextUtils.isEmpty(tvSalaryRange.getText().toString())) {
            tvSalaryRange.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        }
        if (TextUtils.isEmpty(etCompanyName.getText().toString())) {
            etCompanyName.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        }

        if (TextUtils.isEmpty(etCompanyAddress.getText().toString())) {
            etCompanyAddress.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        }

        if (TextUtils.isEmpty(etCompanyPhone.getText().toString())) {
            etCompanyPhone.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        }
        if (!hasError) {
            submit();
        }
    }

    private void submit() {
        WorkInfoReqBean workInfoReqBean = new WorkInfoReqBean();
        workInfoReqBean.work_category = tvWorkCategory.getText().toString();
        workInfoReqBean.salary_range = tvSalaryRange.getText().toString();
        workInfoReqBean.company_name = etCompanyName.getText().toString();
        workInfoReqBean.company_tel = etCompanyPhone.getText().toString();
        workInfoReqBean.company_address = etCompanyAddress.getText().toString();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.AUTH_COMPANY_INFO)
                .content(new Gson().toJson(workInfoReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
//                        showToast("success");
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            startActivity(ApplyThirdActivity.createIntent(mContext));
                            finishActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    @Override
    protected void initializeImmersiveMode() {
        // 不使用ImmersionBar，以防导致输入框不会自动上移
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.theme_color));
        }
    }

}
