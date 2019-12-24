package com.cocotree.android.utils;

import android.webkit.WebStorage;

import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.manager.ConstantValue;

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

        WebStorage.getInstance().deleteAllData();


    }
}
