package com.lemontree.android.ui.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initializeView() {

    }

    @Override
    protected void loadData() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarEnable(false)
                .init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
//            startActivity(new Intent(mContext, MainActivity.class));
            startActivity(ApplyFirstActivity.createIntent(mContext));

            finish();
        }, 100);

    }
}
