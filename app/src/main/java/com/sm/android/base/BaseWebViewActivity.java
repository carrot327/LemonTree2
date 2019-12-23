package com.sm.android.base;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.sm.android.R;
import com.sm.android.manager.BaseApplication;
import com.sm.android.manager.NetConstantValue;
import com.sm.android.ui.widget.AppTitleBar;
import com.sm.android.ui.widget.AppWebView;
import com.sm.android.ui.widget.WebViewProgressBar;
import com.sm.android.utils.LogUtils;
import com.sm.android.utils.NetUtils;
import com.sm.android.utils.StringUtils;
import com.sm.android.utils.UrlUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

import java.lang.reflect.Method;
import java.util.Map;

import butterknife.BindView;

/**
 * BaseWebViewActivity
 *
 * @author evanyu
 * @date 18/4/24
 */
public abstract class BaseWebViewActivity extends BaseActivity {

    private static final String URL_EMPTY = "about:blank";
    protected static final String TAG = "H5WebView";
    private static final int REQUEST_CODE_FILE_CHOOSER = 0x0099;

    public interface IntentKey {
        String SHOW_NATIVE_TITLE_BAR = "show_native_title_bar";
        String URL = "url";
        String TITLE = "title";
        String NEED_SESSION_ID = "need_session_id";
    }

    @BindView(R.id.fl_webview_container)
    FrameLayout mWebViewContainer;
    @BindView(R.id.webview_progress)
    WebViewProgressBar mProgressBar;
    @BindView(R.id.ll_h5_error_view)
    View mErrorView;

    private AppWebView mWebView;
    private boolean mShowNativeTitleBar;
    private String mUrl;
    private boolean mIsNeedSessionId;
    private ValueCallback<Uri[]> mFileChooserCallbackAboveL;
    private ValueCallback<Uri> mFileChooserCallbackBelowL;
    private int mLastProgress;

    /**
     * 清空历史记录标记
     * 默认true，需要先清理上一次打开页面时的浏览记录
     */
    private boolean mClearHistoryFlag = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview_base;
    }

    @Override
    protected boolean isUseDefaultTitleBar() {
        return true;
    }

    @Override
    protected void initTitleBar(AppTitleBar titleBar) {
        initTitleBar(titleBar, getIntent());
    }

    private void initTitleBar(AppTitleBar titleBar, Intent intent) {
        if (titleBar == null || intent == null) {
            return;
        }
        mShowNativeTitleBar = intent.getBooleanExtra(IntentKey.SHOW_NATIVE_TITLE_BAR, true);
        String title = intent.getStringExtra(IntentKey.TITLE);
        mUrl = intent.getStringExtra(IntentKey.URL);

        if (isShowNativeTitleBar()) {
            titleBar.setVisibility(View.VISIBLE);
            titleBar.setTitle(title);
        } else {
            titleBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initializeView() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mIsNeedSessionId = intent.getBooleanExtra(IntentKey.NEED_SESSION_ID, true);
        String url = intent.getStringExtra(IntentKey.URL);
        LogUtils.d(TAG, "h5url -> " + url);
        mUrl = UrlUtils.handleUrl(url);
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }

        initWebView();
        if (mErrorView != null) {
            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
        loadUrl(mUrl);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        initTitleBar(getTitleBar(), intent);
        String url = intent.getStringExtra(IntentKey.URL);
        LogUtils.d(TAG, "h5url -> " + url);
        mUrl = UrlUtils.handleUrl(url);
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        reload(mUrl);
    }

    private void initWebView() {
        disableAccessibility();
        AppWebView.InternalContext.getInstance().setBaseContext(this);
        mWebView = AppWebView.getDefault();
        mWebView.isCleaning = false;
        mWebView.setWebViewClient(createBridgeWebViewClient());
        mWebView.setWebChromeClient(createWebChromeClient());
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        );
        initWebView(mWebView);

        // 添加到当前页面前，先确保 WebView 控件没有父容器
        mWebView.removeFromParent();
        mWebViewContainer.addView(mWebView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    protected abstract void initWebView(@NonNull BridgeWebView bridgeWebView);

    protected void loadUrl(String url) {
        if (mWebView != null && !StringUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    protected void reload() {
        if (mWebView != null) {
            reload(mWebView.getUrl());
        }
    }

    protected void reload(String url) {
        reload(url, true);
    }

    protected void reload(String url, boolean clearHistoryFlag) {
        LogUtils.d(TAG, "reload h5url -> " + url);
        if (mWebView != null) {
            url = UrlUtils.handleUrl(url);
            UrlUtils.reload(mWebView, url);
            mClearHistoryFlag = clearHistoryFlag;
        }
    }

    /**
     * 判断url是否包涵关键字（与H5约定的关键字）
     */
    private boolean containsKeyword(@NonNull String url) {
        return !TextUtils.isEmpty(url)
                && (url.contains("nono-app/")
                || url.contains("nono/interoperate")
                || url.contains("nono/reservation")
                || url.contains("nono-standalone")
                || url.contains("/hscg"));
    }

//    protected String handleUrl(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return "";
//        }
//        // 保证在测试环境下对部分只部署在生产环境的地址的参数拼接
//        if ((!url.startsWith(NetConstantValue.BASE_HOST)
//                && !url.startsWith(EnvironmentUtils.getBaseHost(EnvironmentUtils.ENVIRONMENT_MODE_PRD)))) {
//            return url;
//        }
//        Map<String, String> replaceMap = new HashMap();
//        replaceMap.put("terminal", NetConstantValue.TERMINAL);
//        replaceMap.put("version", BuildConfig.VERSION_NAME);
//        if (mIsNeedSessionId && BaseApplication.sLoginState) {
//            replaceMap.put("sessionId", BaseApplication.mSessionId);
//        }
//        url = UrlUtils.replaceParams(url, replaceMap);
//        LogUtils.d("handleUrl(h5url) -> " + url);
//        return url;
//    }

    private boolean isShowNativeTitleBar() {
        return mShowNativeTitleBar && !containsKeyword(mUrl);
    }

    /**
     * 解析地址中的参数
     *
     * @param url
     */
    private void parseUrlParams(String url) {
        if (isShowNativeTitleBar() && !TextUtils.isEmpty(url) && (url.startsWith(NetConstantValue.BASE_HOST)
        )) {
            boolean isShowTitlebar = true;
            Map<String, String> params = UrlUtils.getQueryMap(url);
            if (params != null && params.containsKey("nav")) {
                String navValue = params.get("nav");
                if (!TextUtils.isEmpty(navValue) && "0".equals(navValue)) {
                    isShowTitlebar = false;
                }
            }
            setTitlebarVisibility(isShowTitlebar);
        }
    }

    /**
     * 是否显示标题栏
     *
     * @param ifShow
     */
    private void setTitlebarVisibility(boolean ifShow) {
        AppTitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            if (ifShow) {
                titleBar.setVisibility(View.VISIBLE);
            } else {
                titleBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 关闭辅助功能，针对4.2.1和4.2.2 崩溃问题
     * java.lang.NullPointerException
     * at android.webkit.AccessibilityInjector$TextToSpeechWrapper$1.onInit(AccessibilityInjector.java:753)
     * ... ...
     * at android.webkit.CallbackProxy.handleMessage(CallbackProxy.java:321)
     */
    private void disableAccessibility() {
        // 4.2 (Build.VERSION_CODES.JELLY_BEAN_MR1)
        if (Build.VERSION.SDK_INT == 17) {
            try {
                AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
                if (am == null || !am.isEnabled()) {
                    return;
                }
                Method set = am.getClass().getDeclaredMethod("setState", int.class);
                set.setAccessible(true);
                set.invoke(am, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void loadData() {
        // empty
    }

    protected BridgeWebView getWebView() {
        return mWebView;
    }

    protected String getUrl() {
        return mUrl;
    }

    private BridgeWebViewClient createBridgeWebViewClient() {
        return new BridgeWebViewClient(getWebView()) {
            /**
             * 当前页面开始加载
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                parseUrlParams(url);
                if (mWebView != null && mWebView.getVisibility() != View.VISIBLE) {
                    mWebView.setVisibility(View.VISIBLE);
                    if (mErrorView != null) {
                        mErrorView.setVisibility(View.GONE);
                    }
                }
                mLastProgress = 0;
                LogUtils.d(TAG, "onPageStarted -> " + url);
            }

            /**
             * 当前页面结束加载
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.d(TAG, "onPageFinished -> " + url);
                if (mWebView != null) {
                    if (mClearHistoryFlag) {
                        mClearHistoryFlag = false;
                        mWebView.clearHistory();
                    }
                    if (mWebView.isCleaning) {
                        cleanWebView();
                    }
                }
            }

            /**
             * 拦截 url 跳转，在里边添加点击链接跳转或者操作
             */
           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(TAG, "shouldOverrideUrlLoading -> h5url -> " + url);
                if (!TextUtils.isEmpty(url)) {
                    if (url.contains("tmast") && CommonUtils.isAppInstalled(mContext, "com.tencent.android.qqdownloader")) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    }
                    if (RouterRequestManager.validateUrl(url)) {
                        RouterRequest routerRequest = RouterRequestManager.urlWithScheme(mContext, url);
                        if (RouterConstant.ROUTER_PROVIDER_WEB.equals(routerRequest.getProvider())) {
                            routerRequest.process(ProcessConstant.PROCESS_WEB);
                        }
                        LocalRouter.getInstance().route(mContext, routerRequest);
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }*/

//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                LogUtils.d(TAG, "shouldInterceptRequest -> h5url -> " + request.getUrl());
//                return super.shouldInterceptRequest(view, request);
//            }

            /**
             * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtils.d(TAG, "onReceivedError -> " + failingUrl);
                if (mWebView != null && mErrorView != null) {
                    mWebView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }

            /**
             * 当接收到https错误时，会回调此函数，在其中可以做错误处理
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 忽略错误，继续加载
                handler.proceed();
            }

            /**
             * 在每一次请求资源时回调（比如超链接、JS文件、CSS文件、图片等）
             * 在非UI线程中执行，不能在该方法中直接进行UI操作
             */
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                // 如果不需要处理，则返回null，默认实现就是返回null
                return super.shouldInterceptRequest(view, url);
            }
        };
    }

    private WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    AppTitleBar appTitleBar = getTitleBar();
                    if (appTitleBar != null && !TextUtils.isEmpty(title)) {
                        appTitleBar.setTitle(title);
                    }
                }
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                try {
                    if (!NetUtils.isNetworkConnected(mContext) || URL_EMPTY.equals(view.getUrl())) {
                        return;
                    }
                    if (progress > mLastProgress) {
                        if (progress >= 100) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1, 0);
                            anim.setDuration(2000);
                            anim.start();
                        } else if (mProgressBar.getVisibility() == View.GONE) {
                            mProgressBar.setAlpha(1);
                        }
                        mLastProgress = progress;
                        mProgressBar.setProgress(progress, 1000);
                    }
                } catch (Exception e) {
                    // ignore
                }
            }

            /**
             * 通知客户端显示文件选择器。用来处理file类型的HTML标签，响应用户点击选择文件的按钮操作。
             * 调用filePathCallback.onReceiveValue(null)并返回true取消请求操作。
             *
             * FileChooserParams参数的枚举列表：
             *      MODE_OPEN 打开
             *      MODE_OPEN_MULTIPLE 选中多个文件打开
             *      MODE_OPEN_FOLDER 打开文件夹（暂不支持）
             *      MODE_SAVE 保存
             *
             * Android 5.0 及以上版本回调
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFileChooserCallbackAboveL = filePathCallback;
                openFileChooserNative();
                return true;
            }

            /**
             * Android 5.0 以下版本回调
             */
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                mFileChooserCallbackBelowL = uploadFile;
                openFileChooserNative();
            }
        };
    }

    private void openFileChooserNative() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 允许多选，API 18
        startActivityForResult(Intent.createChooser(intent, "File Browser"), REQUEST_CODE_FILE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FILE_CHOOSER: // 文件选择器的回调
                onFileChooserResult(resultCode, data);
                break;
            default:
                break;
        }
    }

    private void onFileChooserResult(int resultCode, Intent data) {
        if (mFileChooserCallbackBelowL == null && mFileChooserCallbackAboveL == null) {
            return;
        }
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            cancelFileChooserCallback();
            return;
        }

        // Android 5.0 以下版本的回调处理
        if (mFileChooserCallbackBelowL != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mFileChooserCallbackBelowL.onReceiveValue(data.getData());
            mFileChooserCallbackBelowL = null;
        }

        // Android 5.0 及以上版本的回调处理
        if (mFileChooserCallbackAboveL != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 允许多选时使用 -> intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // API 18
//            ClipData clipData = data.getClipData();
//            if (clipData != null) {
//                fileUris = new Uri[clipData.getItemCount()];
//                for (int i = 0; i < clipData.getItemCount(); i++) {
//                    ClipData.Item item = clipData.getItemAt(i);
//                    fileUris[i] = item.getUri();
//                }
//            }
            mFileChooserCallbackAboveL.onReceiveValue(new Uri[]{data.getData()});
            mFileChooserCallbackAboveL = null;
        }
    }

    private void cancelFileChooserCallback() {
        if (mFileChooserCallbackAboveL != null) {
            mFileChooserCallbackAboveL.onReceiveValue(null);
            mFileChooserCallbackAboveL = null;
        }
        if (mFileChooserCallbackBelowL != null) {
            mFileChooserCallbackBelowL.onReceiveValue(null);
            mFileChooserCallbackBelowL = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按钮的点击事件处理
        if (keyCode == KeyEvent.KEYCODE_BACK
                && mWebView != null
                && mWebView.canGoBack()
                && !mUrl.equals(mWebView.getUrl())) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        if (mWebView != null) {
            mWebView.onResume();
            mWebView.resumeTimers();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mClearHistoryFlag = true;
            mWebView.isCleaning = true;
            mWebView.stopLoading();
            mWebView.loadUrl(URL_EMPTY);
        }
        AppWebView.InternalContext.getInstance().setBaseContext(BaseApplication.getContext());
        super.onDestroy();
    }

    private void cleanWebView() {
        if (mWebView != null && mWebView.isCleaning) {
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.removeAllViews();
            mWebView.removeFromParent();
        }
    }
}