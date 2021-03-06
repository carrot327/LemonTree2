package com.lemontree.android.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lemontree.android.BuildConfig;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.uploadUtil.Tools;
import com.lemontree.android.uploadUtil.UrlHostConfig;
import com.lemontree.android.utils.UpdateUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.rl_version_update)
    RelativeLayout rlVersionUpdate;
    @BindView(R.id.rl_rate)
    RelativeLayout rlRate;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_current_version)
    TextView tvCurrentVersion;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_us;
    }


    @Override
    protected void initializeView() {
        if (BuildConfig.DEBUG) {
            tvCurrentVersion.setText("Version " + Tools.getAppVersion() + "-debug");
        } else {
            tvCurrentVersion.setText("Version " + Tools.getAppVersion());
        }
        ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "AppName: " + Tools.getAppName()+"\nChannel: " + Tools.getChannel(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        if (BuildConfig.DEBUG) {
            ivLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, UrlHostConfig.getBackgroundHost(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.iv_back, R.id.rl_version_update, R.id.rl_rate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_version_update:
                    UpdateUtil.checkUpdate(AboutUsActivity.this);
                break;
            case R.id.rl_rate:
//                Toast.makeText(mContext, "正在开发，敬请期待~", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
