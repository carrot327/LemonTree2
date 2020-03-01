package com.kantong.android.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.kantong.android.BuildConfig;
import com.kantong.android.uploadUtil.CLog;
import com.kantong.android.uploadUtil.UrlHostConfig;
import com.kantong.android.utils.SPUtils;
import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;

public class BaseApplication extends Application implements DFTransferResultInterface {

    private static String TAG = "BaseApplication";
    private static BaseApplication sInstance;

    public static Boolean sLoginState;
    public static String sUserName = "";
    public static String mUserId = "";
    public static String sPhoneNum = "";
    public static String mSharedPreferencesName;
    public static boolean isOpenGodMode;
    private DFProductResult mResult;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initSPData();

//        WebHelper.instanceView(getApplicationContext());
//        if (WebHelper.getWebView() != null) {
//            WebHelper.getWebView().loadUrl(UrlHostConfig.getH5BaseHost());
//        }
    }

    public static BaseApplication getInstance() {

        return sInstance;
    }

    public static BaseApplication getApplication() {
        return sInstance;
    }

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void setResult(DFProductResult result) {
        mResult = result;
    }

    @Override
    public DFProductResult getResult() {
        return mResult;
    }

    /**
     * 初始化SP数据
     */
    public void initSPData() {
        if (BuildConfig.DEBUG) {
            //3836000 晶晶    //3832079  丛丛
            //3832081 晶晶测试环境

//            SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
//            sLoginState = true;
//            sUserName = "asdf";
//            mUserId = "38360000";
//            sPhoneNum = "81290324175";
        }
        sLoginState = SPUtils.getBoolean(ConstantValue.LOGIN_STATE, false);
        mSharedPreferencesName = SPUtils.getString(ConstantValue.USER_ID, "", true);
        if (!TextUtils.isEmpty(mSharedPreferencesName)) {
            mUserId = SPUtils.getString(ConstantValue.KEY_USER_ID, "");
            sPhoneNum = SPUtils.getString(ConstantValue.PHONE_NUMBER, "");
            sUserName = SPUtils.getString(ConstantValue.KEY_LATEST_LOGIN_NAME, "");
        }
        isOpenGodMode = SPUtils.getBoolean(ConstantValue.GOD_MODE, false);
    }
}
