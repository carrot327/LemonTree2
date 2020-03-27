package com.lemontree.android.ui.activity;

import android.app.ProgressDialog;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.WorkInfoReqBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.ui.widget.SimpleTextWatcher;
import com.lemontree.android.uploadUtil.Tools;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ApplyCompanyInfoActivity extends BaseActivity {

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
    @BindView(R.id.outline_work_category)
    TextInputLayout outlineWorkCategory;
    @BindView(R.id.outline_salary_range)
    TextInputLayout outlineSalaryRange;
    @BindView(R.id.outline_company_name)
    TextInputLayout outlineCompanyName;
    @BindView(R.id.outline_company_phone)
    TextInputLayout outlineCompanyPhone;
    @BindView(R.id.outline_company_address)
    TextInputLayout outlineCompanyAddress;

    private View.OnClickListener drapdownListener;

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyCompanyInfoActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_company_info;
    }

    @Override
    protected void initializeView() {
        String[] WORK_CATEGORY = getResources().getStringArray(R.array.work_category);
        String[] SALARY_RANGE = getResources().getStringArray(R.array.salary_range);

        tvWorkCategory.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, WORK_CATEGORY));
        tvSalaryRange.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, SALARY_RANGE));

        drapdownListener = v -> Tools.hideInput(mContext, v);
        tvWorkCategory.setOnClickListener(drapdownListener);
        tvSalaryRange.setOnClickListener(drapdownListener);


        tvWorkCategory.addTextChangedListener(new SimpleTextWatcher(tvWorkCategory, outlineWorkCategory));
        tvSalaryRange.addTextChangedListener(new SimpleTextWatcher(tvSalaryRange, outlineSalaryRange));
        etCompanyName.addTextChangedListener(new SimpleTextWatcher(etCompanyName, outlineCompanyName));
        etCompanyAddress.addTextChangedListener(new SimpleTextWatcher(etCompanyAddress, outlineCompanyAddress));
        etCompanyPhone.addTextChangedListener(new SimpleTextWatcher(etCompanyPhone, outlineCompanyPhone));
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
            outlineWorkCategory.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineWorkCategory.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(tvSalaryRange.getText().toString())) {
            outlineSalaryRange.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineSalaryRange.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etCompanyName.getText().toString())) {
            outlineCompanyName.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineCompanyName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etCompanyAddress.getText().toString())) {
            outlineCompanyAddress.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineCompanyAddress.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etCompanyPhone.getText().toString())) {
            outlineCompanyPhone.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineCompanyPhone.setErrorEnabled(false);
        }

        if (!hasError) {
            submit();
        }
    }

    private void submit() {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Memuat...");
        progressDialog.show();

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
                        progressDialog.dismiss();

                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            startActivity(ApplyContactInfoActivity.createIntent(mContext));
                            finishActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        progressDialog.dismiss();
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
