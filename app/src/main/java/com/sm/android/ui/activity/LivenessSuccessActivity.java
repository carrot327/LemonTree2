package com.sm.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sm.android.R;
import com.sm.android.base.BaseActivity;
import com.sm.android.uploadUtil.UrlHostConfig;
import com.sm.android.utils.IntentUtils;

public class LivenessSuccessActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, LivenessSuccessActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_liveness_success;
    }

    @Override
    protected void initializeView() {
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开信息确认页
                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_INFO_CONFIRM());
                finish();
            }
        });
    }

    @Override
    protected void loadData() {
//        Log.d("LivenessSuccessActivity", "getIntent().getData():" + getIntent().getData());
//        Log.d("LivenessSuccessActivity", "getIntent().getExtras():" + getIntent().getExtras());
//        Log.d("LivenessSuccessActivity", "getIntent().getBundleExtra:" + getIntent().getBundleExtra("LivenessData"));
//        getIntent();
//        Log.d("LivenessSuccessActivity", "getIntent():" + getIntent());
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
