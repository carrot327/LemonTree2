package com.cocotree.android.utils;

import android.text.TextUtils;
import android.webkit.WebView;

import com.cocotree.android.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linqi
 * @description
 */
public class UrlUtils {

    public static String getUrlPath(String urlText) {
        String path = "";
        if (!TextUtils.isEmpty(urlText)) {
            urlText = urlText.trim();
            String[] urlSplitArr = urlText.split("[?]");
            if (urlSplitArr != null && urlSplitArr.length > 0) {
                path = urlSplitArr[0];
            }
        }
        return path;
    }

    public static Map<String, String> getQueryMap(String urlText) {
        Map<String, String> map = new HashMap<String, String>();
        if (!TextUtils.isEmpty(urlText)) {
            urlText = urlText.trim();
            String[] urlSplitArr = urlText.split("[?]");
            if (urlSplitArr != null && urlSplitArr.length > 1) {
                String queryText = urlSplitArr[1];
                if (!TextUtils.isEmpty(queryText)) {
                    String[] queryArr = queryText.split("[&]");
                    if (queryArr != null) {
                        String[] paramArr;
                        for (String str : queryArr) {
                            if (!TextUtils.isEmpty(str)) {
                                paramArr = str.split("[=]");
                                if (paramArr != null && paramArr.length > 1
                                        && !TextUtils.isEmpty(paramArr[0])
                                        && !TextUtils.isEmpty(paramArr[1])) {
                                    map.put(paramArr[0], paramArr[1]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    public static String generateUrl(String urlPath, Map<String, String> queryMap) {
        String url = "";
        if (!TextUtils.isEmpty(urlPath)) {
            url = getUrlPath(urlPath);
            if (queryMap != null && queryMap.size() > 0) {
                url += "?";
                boolean isFirst = true;
                for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        url += "&";
                    }
                    url += key + "=" + value;
                }
            }
        }
        return url;
    }

    public static String replaceParams(String url, String key, String value) {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put(key, value);
        return replaceParams(url, replaceMap);
    }

    public static String replaceParams(String url, Map<String, String> replaceMap) {
        String rtn = "";
        if (!TextUtils.isEmpty(url)) {
            String urlPath = getUrlPath(url);
            Map<String, String> queryMap = getQueryMap(url);
            if (replaceMap != null && replaceMap.size() > 0) {
                for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        queryMap.put(key, value);
                    }
                }
            }
            rtn = generateUrl(urlPath, queryMap);
        }
        return rtn;
    }

    public static boolean isSameUrlPath(String urlText1, String urlText2) {
        String urlPath1 = getUrlPath(urlText1);
        String urlPath2 = getUrlPath(urlText2);
        return !TextUtils.isEmpty(urlPath1) && !TextUtils.isEmpty(urlPath2) && urlPath1.equals(urlPath2);
    }

    public static void reload(WebView webView, String targetUrl) {
        if (webView != null) {
            String url = webView.getUrl();
            String originalUrl = webView.getOriginalUrl();
            if (isBBSUrl(url) || isBBSUrl(originalUrl)) {
                webView.loadUrl(targetUrl);
                webView.loadUrl("javascript:window.location.reload(true)");
                webView.loadUrl("javascript:window.location.replace('" + targetUrl + "')");
            } else if (isSameUrlPath(url, originalUrl)) {
                webView.loadUrl(targetUrl);
                webView.loadUrl("javascript:window.location.reload(true)");
            } else {
                webView.loadUrl("javascript:window.location.replace('" + targetUrl + "')");
            }
        }
    }

    public static boolean isBBSUrl(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("https://bbs.nonobank.com");
    }

    public static String handleUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Map<String, String> replaceMap = new HashMap();
//        replaceMap.put("terminal", NetConstantValue.TERMINAL);
        replaceMap.put("appVersion", BuildConfig.VERSION_NAME);
        url = UrlUtils.replaceParams(url, replaceMap);
        LogUtils.i("karl_h5", "handleUrl(h5url) -> " + url);
        return url;
    }
}
