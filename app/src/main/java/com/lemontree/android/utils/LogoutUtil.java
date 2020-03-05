package com.lemontree.android.utils;

import android.util.Log;
import android.webkit.WebStorage;

import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;

import java.io.File;

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
        clearWebViewCache();

    }

    public static final String TAG = "karl";
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            BaseApplication.getInstance().deleteDatabase("webview.db");
            BaseApplication.getInstance().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(BaseApplication.getInstance().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(BaseApplication.getInstance().getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            BaseApplication.getInstance().deleteFile(String.valueOf(webviewCacheDir));
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            BaseApplication.getInstance().deleteFile(String.valueOf(appCacheDir));
        }
    }
}
