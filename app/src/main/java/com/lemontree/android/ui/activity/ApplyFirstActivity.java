package com.lemontree.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.lemontree.android.bean.request.BasicInfoReqBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.uploadUtil.Tools;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ApplyFirstActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.textInputEditText_Name)
    TextInputEditText textInputEditTextName;
    @BindView(R.id.textInputEditText_KTP)
    TextInputEditText textInputEditTextKTP;
    @BindView(R.id.dropdown_tv_gender)
    AutoCompleteTextView dropdownTvGender;
    @BindView(R.id.dropdown_tv_education)
    AutoCompleteTextView dropdownTvEducation;
    @BindView(R.id.dropdown_tv_marry_state)
    AutoCompleteTextView dropdownTvMarryState;
    @BindView(R.id.dropdown_tv_children_number)
    AutoCompleteTextView dropdownTvChildrenNumber;
    @BindView(R.id.textInputEditText_DetailAddress)
    TextInputEditText textInputEditTextDetailAddress;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @BindView(R.id.outline_Name)
    TextInputLayout outlineName;
    @BindView(R.id.outline_KTP)
    TextInputLayout outlineKTP;
    @BindView(R.id.outline_gender)
    TextInputLayout outlineGender;
    @BindView(R.id.outline_education)
    TextInputLayout outlineEducation;
    @BindView(R.id.outline_marry_state)
    TextInputLayout outlineMarryState;
    @BindView(R.id.outline_children_number)
    TextInputLayout outlineChildrenNumber;
    @BindView(R.id.outline_DetailAddress)
    TextInputLayout outlineDetailAddress;

    private View.OnClickListener drapdownListener;
    private ProgressDialog mProgressDialog;


    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFirstActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_first;
    }

    @Override
    protected void initializeView() {
        String[] GENDER = getResources().getStringArray(R.array.gender);
        String[] EDUCATION = getResources().getStringArray(R.array.education);
        String[] MARRY_STATE = getResources().getStringArray(R.array.marital_status);
        String[] CHILDREN_NUMBER = getResources().getStringArray(R.array.number_of_children);

        dropdownTvGender.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, GENDER));
        dropdownTvEducation.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, EDUCATION));
        dropdownTvMarryState.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, MARRY_STATE));
        dropdownTvChildrenNumber.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, CHILDREN_NUMBER));

        drapdownListener = v -> Tools.hideInput(mContext, v);
        dropdownTvGender.setOnClickListener(drapdownListener);
        dropdownTvEducation.setOnClickListener(drapdownListener);
        dropdownTvMarryState.setOnClickListener(drapdownListener);
        dropdownTvChildrenNumber.setOnClickListener(drapdownListener);

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
        if (TextUtils.isEmpty(textInputEditTextName.getText().toString())) {
            outlineName.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(textInputEditTextKTP.getText().toString())) {
            outlineKTP.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineKTP.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(textInputEditTextDetailAddress.getText().toString())) {
            outlineDetailAddress.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineDetailAddress.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(dropdownTvGender.getText().toString())) {
            outlineGender.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineGender.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(dropdownTvEducation.getText().toString())) {
            outlineEducation.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineEducation.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(dropdownTvMarryState.getText().toString())) {
            outlineMarryState.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineMarryState.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(dropdownTvChildrenNumber.getText().toString())) {
            outlineChildrenNumber.setError(getResources().getString(R.string.text_pls_input));
            hasError = true;
        } else {
            outlineChildrenNumber.setErrorEnabled(false);
        }

        if (!hasError) {
            submit();
        }
    }

    private void submit() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Memuat...");
        mProgressDialog.show();
        BasicInfoReqBean basicInfoReqBean = new BasicInfoReqBean();
        basicInfoReqBean.customer_name = textInputEditTextName.getText().toString();
        basicInfoReqBean.id_card_no = textInputEditTextKTP.getText().toString();
        basicInfoReqBean.sex = dropdownTvGender.getText().toString();
        basicInfoReqBean.education = dropdownTvEducation.getText().toString();
        basicInfoReqBean.marriage_condition = dropdownTvMarryState.getText().toString();
        basicInfoReqBean.children_count = dropdownTvChildrenNumber.getText().toString().trim();
        basicInfoReqBean.address = textInputEditTextDetailAddress.getText().toString();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.AUTH_BASIC_INFO)
                .content(new Gson().toJson(basicInfoReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        mProgressDialog.dismiss();
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                startActivity(ApplySecondActivity.createIntent(mContext));
                                finishActivity();
                            } else {
                                showToast(response.res_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        mProgressDialog.dismiss();
                    }
                });
    }

}
