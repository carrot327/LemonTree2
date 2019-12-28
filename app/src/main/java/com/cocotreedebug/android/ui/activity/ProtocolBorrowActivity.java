package com.cocotreedebug.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.cocotreedebug.android.R;
import com.cocotreedebug.android.base.BaseActivity;
import com.cocotreedebug.android.bean.response.DisclaimerResponseBean;
import com.cocotreedebug.android.manager.ConstantValue;
import com.cocotreedebug.android.manager.NetConstantValue;
import com.cocotreedebug.android.network.OKHttpClientEngine;

import butterknife.BindView;
import okhttp3.Call;


/**
 * 获取借款协议
 */
public class ProtocolBorrowActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ProtocolBorrowActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initializeView() {
        tvTitle.setText("Perjanjian Pinjaman");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        NetworkLiteHelper
                .get()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_PROTOCOL_LOAN)
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<DisclaimerResponseBean>() {
                    @Override
                    public void onSuccess(Call call, DisclaimerResponseBean response, int id) {
                        if (response != null) {
                            if (!TextUtils.isEmpty(response.note)) {
                                tvContent.setText(response.note);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        Toast.makeText(mContext, R.string.toast_text_get_privacy_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
