package com.sm.android.ui.widget;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;

import com.sm.android.manager.BaseApplication;
import com.sm.android.utils.NetUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import java.io.File;

/**
 *
 * @author evanyu
 * @date 18/5/3
 */
public class AppWebView extends BridgeWebView {

    public static final String TAG = AppWebView.class.getSimpleName();

    /** 本地缓存路径 */
    public static final String CACHE_PATH;
    /** 是否正在清理WebView */
    public boolean isCleaning;

    static {
        CACHE_PATH = new File(BaseApplication.getContext().getCacheDir(), "webview").getAbsolutePath();
    }

    public AppWebView(Context context) {
        super(context);
        init();
    }

    private static class InstanceHolder {
        // 使用Application Context创建WebView对象，可以避免大部分WebView常见的内存泄漏问题
        //static final AppWebView sInstance = new AppWebView(InternalContext.getInstance().getMutableContext());
        static final AppWebView sInstance = new AppWebView(BaseApplication.getContext());
    }

    @NonNull
    public static AppWebView getDefault() {
        return InstanceHolder.sInstance;
    }

    private void init() {
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        initWebSetting();
        // 设置初始缩放比例
        this.setInitialScale(25);

        if (Build.VERSION.SDK_INT >= 19) {
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void initWebSetting() {
        WebSettings settings = this.getSettings();
        /*
         * WebView缓存机制
         * 1.浏览器缓存（WebView自带，内部自动处理）
         * 2.Application Cache
         * 3.DomStorage
         * 4.Web SQL Database
         * 5.IndexedDB
         * 6.File System（暂时不支持）
         */
        // Application Cache
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(20 * 1024 * 1024);
        settings.setAppCachePath(CACHE_PATH);
        // DomStorage
        settings.setDomStorageEnabled(true);
        // Web SQL Database（Android官方已不再推荐使用，取而代之的是IndexedDB）
//        settings.setDatabaseEnabled(true);
//        settings.setDatabasePath(getWebViewCachePath());
        // IndexedDB 缓存机制，设置支持JS后自动开启 IndexedDB
        settings.setJavaScriptEnabled(true);
        // 设置WebView的缓存模式
        if (NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            // 根据cache-control决定是否从网络上取数据
//            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            // 没网，离线加载，优先加载缓存(即使已经过期)
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 资源访问
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true); // 是否可访问本地文件，默认值 true
        // 是否支持viewport属性（自适应屏幕）
        // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);
        // 布局算法
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        // 自动加载图片
        settings.setLoadsImagesAutomatically(true);
        // 是否支持缩放
        settings.setSupportZoom(false);
//        settings.setBuiltInZoomControls(true);  // 是否使用内置缩放机制
//        settings.setDisplayZoomControls(false); // 是否显示内置缩放控件
        // 默认编码模式，默认值: UTF-8
//        settings.setDefaultTextEncodingName("GBK");
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 是否使用overview mode加载页面，默认值 false
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(false);
        settings.setSavePassword(false);
        // 定位功能
//        settings.setGeolocationEnabled(true);
//        settings.setGeolocationDatabasePath(getWebViewCachePath());

        // 5.0及以上版本允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 是否在离开屏幕时光栅化(会增加内存消耗)，默认值 false
            settings.setOffscreenPreRaster(true);
        }

        // set userAgent
//        String userAgentStr = settings.getUserAgentString();
//        userAgentStr = userAgentStr + "|" + MapUtils.toJson(getUserAgentParams());
//        LogUtils.d(TAG, "UserAgent -> " + userAgentStr);
//        settings.setUserAgentString(userAgentStr);
    }

/*    private Map<String, String> getUserAgentParams() {
        Map<String, String> userAgentMap = new HashMap<>();
        userAgentMap.put("deviceIdentifier", AndroidIDUtils.getImei(BaseApplication.getContext()));
        userAgentMap.put("appVersion", BuildConfig.VERSION_NAME);
        userAgentMap.put("phoneBrand", StringUtils.toString(Build.BRAND)); // 手机品牌
        userAgentMap.put("networkType", AppInfoUtils.getNetworkType(BaseApplication.getContext()));
        return userAgentMap;
    }*/

    public void removeFromParent() {
        /*
         * in android 5.1(sdk:21) we should invoke this to avoid memory leak
         * see (https://coolpers.github.io/webview/memory/leak/2015/07/16/android-5.1-webview-memory-leak.html)
         */
        ViewParent parent = this.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this);
        }
    }

    public static class InternalContext {

        private static InternalContext sInstance = new InternalContext();
        private MutableContextWrapper mMutableContext;

        public static InternalContext getInstance() {
            return sInstance;
        }

        public void setBaseContext(Context context) {
            if (mMutableContext == null) {
                mMutableContext = new MutableContextWrapper(context);
            } else {
                mMutableContext.setBaseContext(context);
            }
        }

        protected MutableContextWrapper getMutableContext() {
            return mMutableContext;
        }
    }
}