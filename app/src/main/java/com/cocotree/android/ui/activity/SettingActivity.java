package com.cocotree.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocotree.android.R;
import com.cocotree.android.base.BaseActivity;
import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.utils.LogoutUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.btn_titlebar_back)
    ImageView btnTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @BindView(R.id.rl_law_agreement)
    RelativeLayout rlLawAgreement;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initializeView() {
        tvTitlebarTitle.setText(R.string.title_setting);
        if (BaseApplication.sLoginState) {
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogout.setVisibility(View.GONE);
        }

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_titlebar_back, R.id.rl_about_us, R.id.rl_law_agreement, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_titlebar_back:
                finish();
                break;
            case R.id.rl_about_us:          //关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.rl_law_agreement:     //法律协议
                startActivity(new Intent(this, ProtocolPrivacyPolicyActivity.class));
                break;
            case R.id.btn_logout:
                new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setCancelable(false)
                        .setTitle(R.string.dialog_logout_confirm)
                        .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogoutUtil.logout();
                                startActivity(new Intent(mContext, MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
        }
    }
}
