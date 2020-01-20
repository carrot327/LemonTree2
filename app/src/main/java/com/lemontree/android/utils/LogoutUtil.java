package com.lemontree.android.utils;

import android.webkit.WebStorage;

import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;

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
        SPUtils.remove(ConstantValue.COUPON_DIALOG_HAS_SHOWED);
        SPUtils.remove(ConstantValue.IS_SELECT_COUPON);
        SPUtils.remove(ConstantValue.LAST_ORDER_ID);

        WebStorage.getInstance().deleteAllData();


    }
}
