package com.cocotreedebug.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cocotreedebug.android.R;
import com.cocotreedebug.android.base.BaseActivity;
import com.cocotreedebug.android.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BankCardManageActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_add_bankcard)
    RelativeLayout addBankcard;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bankcard_manage;
    }

    @Override
    protected void initializeView() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.iv_back, R.id.rl_add_bankcard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_add_bankcard:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("提示")
                        .setMessage("请先进行个人信息认证哦")
                        .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IntentUtils.gotoWebPageWithSuffixUrl(mContext, "/#/authList");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
    }
}
