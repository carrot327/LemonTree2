package com.lemontree.android.ui.activity;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.bean.response.DisclaimerResponseBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.DialogFactory;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 获取注册登录协议
 */
public class ProtocolRegisterActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initializeView() {
        tvTitle.setText(R.string.register_policy);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
        /*获取注册服务协议*/
       /* NetworkLiteHelper
                .get()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_PROTOCOL_REG)
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
                });*/
        tvContent.setText(Html.fromHtml(DialogFactory.privacy_content));
    }
}
