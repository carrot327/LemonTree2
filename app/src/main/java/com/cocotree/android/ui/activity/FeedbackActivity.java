package com.cocotree.android.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.cocotree.android.manager.BaseApplication;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.cocotree.android.R;
import com.cocotree.android.base.BaseActivity;
import com.cocotree.android.base.BaseResponseBean;
import com.cocotree.android.bean.request.FeedbackReqBean;
import com.cocotree.android.bean.response.LoginResponseBean;
import com.cocotree.android.manager.ConstantValue;
import com.cocotree.android.manager.NetConstantValue;
import com.cocotree.android.network.OKHttpClientEngine;

import butterknife.BindView;
import okhttp3.Call;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    EditText etContent;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_feedback_back)
    ImageView ivFeedbackBack;

    String mContent;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initializeView() {
        etMobile.setText("+86 "+BaseApplication.sPhoneNum);

        ivFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackReqBean feedbackReqBean = new FeedbackReqBean();
                feedbackReqBean.content = mContent;

                NetworkLiteHelper
                        .postJson()
                        .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_FEEDBACK)
                        .content(new Gson().toJson(feedbackReqBean))
                        .build()
                        .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<LoginResponseBean>() {
                            @Override
                            public void onSuccess(Call call, LoginResponseBean response, int id) {
                                if (response != null) {
                                    if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                        Toast.makeText(mContext, "Terima kasih atas balasan Anda, kami akan menghubungi Anda tepat waktu!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(mContext, R.string.text_submit_failed, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, R.string.text_submit_failed, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Exception exception, int id) {
                                Toast.makeText(mContext, R.string.text_submit_failed_and_try, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        etContent.addTextChangedListener(watcher);
    }

    @Override
    protected void loadData() {

    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mContent = etMobile.getText().toString().trim()
                    + "-" + BaseApplication.mUserId
                    + "\n" + etContent.getText().toString().trim();
            if (!TextUtils.isEmpty(mContent)) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
        }
    };
}
