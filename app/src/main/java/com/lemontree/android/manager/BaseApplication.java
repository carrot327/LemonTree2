package com.lemontree.android.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.lemontree.android.BuildConfig;
import com.lemontree.android.uploadUtil.CLog;
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

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                CLog.i(TAG, "onActivityCreated()....");
            }

            @Override
            public void onActivityStarted(Activity activity) {

                CLog.i(TAG, "onActivityStarted()....");
            }

            @Override
            public void onActivityResumed(Activity activity) {

                CLog.i(TAG, "onActivityResumed()....");
            }

            @Override
            public void onActivityPaused(Activity activity) {

                CLog.i(TAG, "onActivityPaused()....");
            }

            @Override
            public void onActivityStopped(Activity activity) {

                CLog.i(TAG, "onActivityStopped()....");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        /*资源预加载 预加载webview对象，首次初始化WebView会比第二次慢很多的原因：
        初始化后，即使webview已经释放，但是WebView的一些共享的对象依然是存在的，
        我们可以在Application里面提前初始化一个Webview的对象，然后可以直接loadurl加载资源*/
//        initWebViewSetting();
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
//            mUserId = "3837157";
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
