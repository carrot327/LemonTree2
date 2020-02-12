package com.cocotree.android.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class WebHelper {
    private static BridgeWebView mWebView;

    public static BridgeWebView getWebView() {
        return mWebView;
    }

    static void instanceView(Context context) {
        mWebView = new BridgeWebView(context);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        initSetting();
        initClient();
    }

    private static void initSetting() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);        // 支持js脚本
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);

        // 设置UserAgent
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        // 允许跨域
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = webSettings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webSettings, true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mWebView.requestFocusFromTouch();
    }


    private static void initClient() {

        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理js中的Alert
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return true;
            }

            /**
             * 处理js中的Confirm
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }

            /**
             * 设置网页中的Progress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 设置应用程序的标题title
             */
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota,
                                                WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return true;
            }
        });

        mWebView.setWebViewClient(new BridgeWebViewClient(mWebView) {

            @Override
            protected boolean onCustomShouldOverrideUrlLoading(String url) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError
                    error) {
                handler.proceed();  //接受所有证书

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("WebViewActivity2", "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebViewActivity2", "onPageFinished");

            }
        });
    }


}
