package com.cocotree.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.cocotree.android.R;
import com.cocotree.android.base.BaseActivity;

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

    /**
     * 商城首页开关
     *//*
    private void getShoppingState() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + NetConstantValue.NET_REQUEST_HOME_SHOPPING_STATE)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<HomeShoppingDataResBean>() {
                    @Override
                    public void onSuccess(Call call, HomeShoppingDataResBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                MainActivity.sHomeShoppingDataResBean = response;
                                if ("1".equals(response.switch_flag)) {
                                    mIsShopTabShow = true;
                                } else {
                                    mIsShopTabShow = false;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            finish();
        }, 1000);
    }
}
