package com.lemontree.android.ui.activity;

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
import com.lemontree.android.bean.request.AddBankInfoReqBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.response.GetBankListResBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class BankInfoActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.dropdown_tv_bank_name)
    AutoCompleteTextView dropdownTvBankName;
    @BindView(R.id.outline_bank_name)
    TextInputLayout outlineBankName;
    @BindView(R.id.textInputEditText_bankNum)
    TextInputEditText etBankNum;
    @BindView(R.id.outline_bank_num)
    TextInputLayout outlineBankNum;
    private List<String> mBankNameList;

    public static Intent createIntent(Context context) {
        return new Intent(context, BankInfoActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bank_card_info;
    }

    @Override
    protected void initializeView() {
        getBankNameList();
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    private void getBankNameList() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.GET_BANK_NAME_LIST)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<GetBankListResBean>() {
                    @Override
                    public void onSuccess(Call call, GetBankListResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code) && response.bankCardList != null) {
                            if (response.bankCardList.size() > 0) {
                                mBankNameList = new ArrayList<>();
                                for (int i = 0; i < response.bankCardList.size(); i++) {
                                    mBankNameList.add(response.bankCardList.get(i).card_bank_name);
                                }
                                dropdownTvBankName.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, mBankNameList));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    private boolean checkFormatSuccess() {
        boolean hasErrow = false;
        if (TextUtils.isEmpty(dropdownTvBankName.getText().toString())) {
            outlineBankName.setErrorEnabled(true);
            outlineBankName.setError("Silakan isi");
            hasErrow = true;
        } else if (!mBankNameList.contains(dropdownTvBankName.getText().toString())) {
            outlineBankName.setErrorEnabled(true);
            outlineBankName.setError("Silakan masukkan nama bank yang benar");//请输入正确的银行名称
            hasErrow = true;
        } else {
            outlineBankName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(etBankNum.getText().toString())) {
            outlineBankNum.setErrorEnabled(true);
            outlineBankNum.setError("Silakan isi");
            hasErrow = true;
        } else {
            outlineBankNum.setErrorEnabled(false);
        }


        if (hasErrow) {
            return false;
        } else {
            return true;
        }
    }

    private void submitBankInfo() {
        AddBankInfoReqBean addBankInfoReqBean = new AddBankInfoReqBean();
        addBankInfoReqBean.type = 1;
        addBankInfoReqBean.bank_card_no = etBankNum.getText().toString().trim();
        addBankInfoReqBean.card_bank_name = dropdownTvBankName.getText().toString().trim();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_BANKCARD_ADD)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
//                            showToast("submit success!");
                            //进行活体。 活体完成后跳确认页，确认页中再上传信息
                            startActivity(StartLivenessActivity.createIntent(mContext));
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:

                break;
            case R.id.btn_confirm:
                if (checkFormatSuccess()) {
                    submitBankInfo();
                }
                break;
        }
    }


}
