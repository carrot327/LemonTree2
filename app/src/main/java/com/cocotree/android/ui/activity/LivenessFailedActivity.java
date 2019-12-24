package com.cocotree.android.ui.activity;

import android.content.Context;
import android.content.Intent;

import android.view.View;

import com.cocotree.android.R;
import com.cocotree.android.base.BaseActivity;

public class LivenessFailedActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, LivenessFailedActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_liveness_failed;
    }

    @Override
    protected void initializeView() {
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
