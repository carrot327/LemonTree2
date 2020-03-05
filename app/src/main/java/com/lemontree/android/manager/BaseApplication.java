package com.lemontree.android.manager;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lemontree.android.BuildConfig;
import com.lemontree.android.uploadUtil.UrlHostConfig;
import com.lemontree.android.utils.SPUtils;
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

        WebHelper.instanceView(getApplicationContext());
        if (WebHelper.getWebView() != null) {
            WebHelper.getWebView().loadUrl(UrlHostConfig.getH5BaseHost());
        }
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
            //晶晶 colada  test
            SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
            sLoginState = true;
            sUserName = "登录信息(081287566687)";
            mUserId = "3832081";
            sPhoneNum = "081287566687";
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
