package com.lemontree.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

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
import com.lemontree.android.ui.widget.SimpleTextWatcher;
import com.lemontree.android.uploadUtil.Tools;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ApplyBaseInfoActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.textInputEditText_Name)
    TextInputEditText etName;
    @BindView(R.id.textInputEditText_KTP)
    TextInputEditText etKTP;
    @BindView(R.id.dropdown_tv_gender)
    AutoCompleteTextView dropTvGender;
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
    private TextWatcher textWatcher;
    private boolean mHasError;


    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyBaseInfoActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_base_info;
    }

    @Override
    protected void initializeView() {
        String[] GENDER = getResources().getStringArray(R.array.gender);
        String[] EDUCATION = getResources().getStringArray(R.array.education);
        String[] MARRY_STATE = getResources().getStringArray(R.array.marital_status);
        String[] CHILDREN_NUMBER = getResources().getStringArray(R.array.number_of_children);

        dropTvGender.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, GENDER));
        dropdownTvEducation.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, EDUCATION));
        dropdownTvMarryState.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, MARRY_STATE));
        dropdownTvChildrenNumber.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, CHILDREN_NUMBER));

        drapdownListener = v -> Tools.hideInput(mContext, v);
        dropTvGender.setOnClickListener(drapdownListener);
        dropdownTvEducation.setOnClickListener(drapdownListener);
        dropdownTvMarryState.setOnClickListener(drapdownListener);
        dropdownTvChildrenNumber.setOnClickListener(drapdownListener);

        etName.addTextChangedListener(new SimpleTextWatcher(etName, outlineName));
        etKTP.addTextChangedListener(new SimpleTextWatcher(etKTP, outlineKTP));
        dropTvGender.addTextChangedListener(new SimpleTextWatcher(dropTvGender, outlineGender));
        dropdownTvEducation.addTextChangedListener(new SimpleTextWatcher(dropdownTvEducation, outlineEducation));
        dropdownTvMarryState.addTextChangedListener(new SimpleTextWatcher(dropdownTvMarryState, outlineMarryState));
        dropdownTvChildrenNumber.addTextChangedListener(new SimpleTextWatcher(dropdownTvChildrenNumber, outlineChildrenNumber));
        textInputEditTextDetailAddress.addTextChangedListener(new SimpleTextWatcher(textInputEditTextDetailAddress, outlineDetailAddress));
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
        mHasError = false;
        doCheck(etName, outlineName);
        doCheck(etKTP, outlineKTP);
        doCheck(dropTvGender, outlineGender);
        doCheck(dropdownTvEducation, outlineEducation);
        doCheck(dropdownTvMarryState, outlineMarryState);
        doCheck(dropdownTvChildrenNumber, outlineChildrenNumber);
        doCheck(textInputEditTextDetailAddress, outlineDetailAddress);
        if (!mHasError) {
            submit();
        }
    }

    private void doCheck(TextInputEditText et, TextInputLayout outline) {
        if (TextUtils.isEmpty(et.getText().toString())) {
            outline.setError(getResources().getString(R.string.text_pls_input));
            mHasError = true;
        }
    }

    private void doCheck(AutoCompleteTextView autoEt, TextInputLayout outline) {
        if (TextUtils.isEmpty(autoEt.getText().toString())) {
            outline.setError(getResources().getString(R.string.text_pls_input));
            mHasError = true;
        }
    }

    private void submit() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Memuat...");
        mProgressDialog.show();
        BasicInfoReqBean basicInfoReqBean = new BasicInfoReqBean();
        basicInfoReqBean.customer_name = etName.getText().toString();
        basicInfoReqBean.id_card_no = etKTP.getText().toString();
        basicInfoReqBean.sex = dropTvGender.getText().toString();
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
                                startActivity(ApplyCompanyInfoActivity.createIntent(mContext));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    public boolean showMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.custom_menu, popup.getMenu());
        popup.show();
        return true;
    }
}
