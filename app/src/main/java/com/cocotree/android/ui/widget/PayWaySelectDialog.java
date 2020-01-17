package com.cocotree.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cocotree.android.R;
import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.manager.ConstantValue;
import com.cocotree.android.uploadUtil.Tools;
import com.cocotree.android.utils.IntentUtils;
import com.cocotree.android.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cocotree.android.manager.ConstantValue.EXTEND_PAY;
import static com.cocotree.android.manager.ConstantValue.NORMAL_PAY;
import static com.cocotree.android.uploadUtil.UrlHostConfig.getH5BaseHost;


public class PayWaySelectDialog extends Dialog {
    Context mContext;
    @BindView(R.id.btn_pay_way_alfamart)
    Button btnPayWayAlfamart;
    @BindView(R.id.btn_pay_way_amt)
    Button btnPayWayAmt;
    @BindView(R.id.btn_pay_way_mobile_bank)
    Button btnPayWayMobileBank;


    private String mFrom = "1";


    public PayWaySelectDialog(Context context, String from) {
        super(context);
        mContext = context;
        if (NORMAL_PAY.equals(from)) {
            mFrom = "1";
        } else if (EXTEND_PAY.equals(from)) {
            mFrom = "2";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_way_select_dialog);
        ButterKnife.bind(this, this);
    }

    @OnClick({R.id.btn_pay_way_alfamart, R.id.btn_pay_way_amt, R.id.btn_pay_way_mobile_bank})
    public void onViewClicked(View view) {
        String type = "1";
        switch (view.getId()) {
            case R.id.btn_pay_way_alfamart:
                type = "1";
                break;
            case R.id.btn_pay_way_amt:
                type = "2";
                break;
            case R.id.btn_pay_way_mobile_bank:
                type = "3";
                break;
        }
        String suffixURL = ConstantValue.H5_REPAY + "?type=" + type + "&from=" + mFrom;

        String url = getH5BaseHost() + suffixURL + "&" +
                "app_clientid=" + Tools.getChannel() +
                "&app_name=android" +
                "&app_version=" + Tools.getAppVersion() +
                "&phone=" + BaseApplication.sPhoneNum +
                "&user_id=" + BaseApplication.mUserId +
                "&user_name=" + StringUtils.toUTF8(BaseApplication.sUserName);

        IntentUtils.openWebViewActivity(mContext, url);
        this.dismiss();
    }
}

