package com.lemontree.android.manager;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lemontree.android.BuildConfig;
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
    public static String mUserTag = "";
    public static boolean isOpenGodMode;
    public static boolean mHasUploadAddressBook;
    public static boolean mHasUpdateSmsSuccess;
    public static boolean mHasUpdateAppListSuccess;
    public static boolean mHasUpdateCallLogSuccess;
    public static boolean mHasShowPicExample;
    public static boolean mHasShowHoldPicExample;
    private DFProductResult mResult;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initSPData();
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
//            SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
//            sLoginState = true;
//            sUserName = "登录信息(081287566687)";
//            mUserId = "3832610";
//            mUserId = "3832081";//晶晶 测试id
//            mUserId = "3835666";//有待还订单
//            sPhoneNum = "081287566687";
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
