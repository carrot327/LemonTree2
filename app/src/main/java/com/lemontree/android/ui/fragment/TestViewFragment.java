package com.lemontree.android.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.gyf.barlibrary.ImmersionBar;
import com.lemontree.android.BuildConfig;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseFragment;
import com.lemontree.android.uploadUtil.CLog;
import com.lemontree.android.uploadUtil.UrlHostConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;

/**
 * 安全管理Fragment
 */
public class TestViewFragment extends BaseFragment implements BridgeHandler {
    @BindView(R.id.wvWebView)
    BridgeWebView applyWebView;
    @BindView(R.id.pbWebView)
    ProgressBar mProgressBar;

    private CallBackFunction mCallBackFunction;

    @Override
    protected void loadData(boolean hasRequestData) {
        applyWebView.loadUrl(UrlHostConfig.GET_H5_AUTHENTICATION());
        Log.d("h5url", applyWebView.getUrl());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initializeView(View view) {
//        EventBus.getDefault().register(this);

        enableLazyLoad(false);
        initWebViewSetting();
        initListener();
        setImmersiveMode();
    }

    private void initListener() {
        initWebViewListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 111 && resultCode == 111) {
//        }
    }

    /**
     * init webView setting
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        WebSettings webSettings = applyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        // 允许跨域
        try {
            Class<?> clazz = webSettings.getClass();
            Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
            method.invoke(webSettings, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initWebViewListener() {
        applyWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        applyWebView.setWebViewClient(new BridgeWebViewClient(applyWebView) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  //接受所有证书
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            /**
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                CLog.d("karl", "url = " + applyWebView.getUrl());
            }
        });

        applyWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                if (newProgress >= 100) {
//                    mProgressBar.setVisibility(View.GONE);
//                } else {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    mProgressBar.setProgress(newProgress);
//                }
                mProgressBar.setProgress(newProgress);
            }

            /**
             * 处理js中的Alert
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
//                return true;
            }

            /**
             * 处理js中的Confirm
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }

            /**
             * 设置应用程序的标题title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                mAppTitleBar.setTitle(title);
            }
        });
    }


/*    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenUrlEvent(OpenUrlEvent event) {
        if (!applyWebView.getUrl().equals(event.url)) {
            applyWebView.loadUrl(event.url);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    protected void setImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImmersionBar.with(this)
                    .reset()
                    .fitsSystemWindows(true) // 解决状态栏和布局重叠问题，使用该属性，必须指定状态栏颜色
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true)
                    .navigationBarEnable(false)
                    .init();
        }
    }

    @Override
    public void handler(String data, CallBackFunction function) {

        mCallBackFunction = function;
        CLog.e("handler", "从js端获取到的数据--->" + data);
        try {
            if (TextUtils.isEmpty(data)) return;

            JSONObject obj = new JSONObject(data);
            // 1 orc 认证 2 抓取到短信、通讯录等 3 重新抓取到短信、通讯录等 4 返回首页
            if (obj.optInt("type") == 1) {//orc 认证  跳OCR认证页面
//                    Intent intent = new Intent(this, InfoAuthenticationActivity.class);
//                    intent.putExtra("user_id", obj.optString("user_id"));
//                    startActivityForResult(intent, OCR_AUTHENTICATE);
            } else if (obj.optInt("type") == 2) {//2 抓取到短信、通讯录等
//                    handleType2(obj, function);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
        }
    }*/
}
