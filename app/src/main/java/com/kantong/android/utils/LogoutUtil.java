package com.kantong.android.utils;

import android.webkit.WebStorage;

import com.kantong.android.manager.BaseApplication;
import com.kantong.android.manager.ConstantValue;
import com.kantong.android.manager.WebHelper;

public class LogoutUtil {
    public static void logout() {
        BaseApplication.sLoginState = false;
        BaseApplication.mUserId = "";
        BaseApplication.sPhoneNum = "";
        BaseApplication.sUserName = "";
        BaseApplication.mSharedPreferencesName = "";
        SPUtils.putBoolean(ConstantValue.LOGIN_STATE, false);
        SPUtils.remove(ConstantValue.USER_ID);
        SPUtils.remove(ConstantValue.PHONE_NUMBER);
        SPUtils.remove(ConstantValue.KEY_LATEST_LOGIN_NAME);

//        WebHelper.getWebView().clearCache(true);
        WebStorage.getInstance().deleteAllData();


    }
}
