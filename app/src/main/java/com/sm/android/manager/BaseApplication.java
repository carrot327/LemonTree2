package com.sm.android.manager;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sm.android.BuildConfig;
import com.sm.android.utils.SPUtils;
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

    public static FirebaseAnalytics sFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initSPData();
        sFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
            //线上用户
//            SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
//            sLoginState = true;
//            sUserName = "登录信息(081266568320)";
//            mUserId = "3835061";
//            mUserId = "3848839";
//            sPhoneNum = "081266568320";
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
