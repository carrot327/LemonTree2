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
            startActivity(MainActivity.createIntent(mContext));
//            startActivity(PayActivity.createIntent(mContext));
//            startActivity(ApplyBaseInfoActivity.createIntent(mContext));
//            startActivity(ApplyCompanyInfoActivity.createIntent(mContext));
//            startActivity(ApplyContactInfoActivity.createIntent(mContext));
//            startActivity(ApplyPicInfoActivity.createIntent(mContext));
//            startActivity(InfoGetReadyActivity.createIntent(mContext));
            finish();
        }, 100);

//        startActivity(ApplyBaseInfoActivity.createIntent(mContext));


    }
}