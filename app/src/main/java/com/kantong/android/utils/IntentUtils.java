package com.kantong.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.kantong.android.manager.BaseApplication;
import com.kantong.android.ui.activity.LoginActivity;
import com.kantong.android.ui.activity.MainActivity;
import com.kantong.android.ui.activity.WebViewActivity;
import com.kantong.android.uploadUtil.UrlHostConfig;

/**
 * IntentUtils
 * Created by evanyu on 16/11/21.
 */
public class IntentUtils {

    /**
     * 到期处理方式设置
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开密码登录页面
     *
     * @param activity
     * @param requestCode
     */
    public static void startLoginActivityForResult(Activity activity, int requestCode) {
        startLoginActivityForResult(activity, "", 0, requestCode);
    }

    /**
     * 打开密码登录页面
     *
     * @param activity
     * @param action
     * @param flags
     * @param requestCode
     */
    public static void startLoginActivityForResult(Activity activity, String action, int flags, int requestCode) {
        Intent intent = new Intent(activity, LoginActivity.class);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        if (flags != 0) {
            intent.addFlags(flags);
        }
//        intent.putExtra("isHomeToHere", isHomeToHere);
        activity.startActivityForResult(intent, requestCode);
    }


    public static void openWebViewActivity(@NonNull Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        Log.d("h5url", "url-" + url);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到web页（登录状态判断、自动拼接UrlHost）
     */
    public static void gotoWebPageWithSuffixUrl(@NonNull Context context, String suffixURL) {
        if (BaseApplication.sLoginState) {
            String h5Url = UrlHostConfig.appendUrlWithParamsAndHost(suffixURL);
            IntentUtils.openWebViewActivity(context, h5Url);
        } else {
            IntentUtils.startLoginActivityForResult((Activity) context, MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
        }
    }

    /**
     * 跳转到web页（登录状态判断、直接传入整个url）
     */
    public static void gotoWebPageWithWholeUrl(@NonNull Context context, String wholeURL) {
        if (BaseApplication.sLoginState) {
            String url = UrlHostConfig.appendUrlWithParamsOnly(wholeURL);
            IntentUtils.openWebViewActivity(context, url);
            Log.d("h5url", "h5url-" + url);
        } else {
            IntentUtils.startLoginActivityForResult((Activity) context, MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
        }
    }

    public static void gotoMainActivity(@NonNull Context context, String tabIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.TAB_INDEX, tabIndex);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
