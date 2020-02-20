package com.lemontree.android.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.uploadUtil.CLog;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.UploadDataBySingle;
import com.lemontree.android.uploadUtil.UploadNecessaryData;
import com.lemontree.android.utils.IntentUtils;
import com.lemontree.android.utils.SPUtils;
import com.minchainx.permission.util.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends BaseActivity implements BridgeHandler {
    private final String TAG = "WebViewActivity";

    BridgeWebView mWebView;
    ProgressBar mProgressBar;

    String mUrl;

    private final int PICK_FILE_RESULT_CODE = 0;
    private final int OCR_AUTHENTICATE = 100;
    private final int PICK_CONTACT = 101;
    private final int LIVENESS_REQUEST_CODE = 102;//活体识别
    private CallBackFunction singleContactCallBack;
    private CallBackFunction mCallBackFunction;
    private CallBackFunction mLivenessCallBackFunction;
    private CallBackFunction type2or3Function;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageArray;
    private JSONObject type2or3Obj;

    private boolean mHasUploadAddressBook;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initializeView() {
        mWebView = findViewById(R.id.wvWebView);
        mProgressBar = findViewById(R.id.pbWebView);
        initWebViewSetting();
        initWebViewListener();
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initializePrepareData() {
        mUrl = getIntent().getStringExtra("url");
    }

    @Override
    protected void loadData() {
    }

    private void initWebViewSetting() {
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
        //注册register
        mWebView.registerHandler("toAppHandler", this);
        mWebView.registerHandler("close", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish();
            }
        });
        mWebView.requestFocusFromTouch();
        mWebView.registerHandler("toAppHandlerContacts", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                singleContactCallBack = function;
                try {
                    if (TextUtils.isEmpty(data)) return;
                    JSONObject obj = new JSONObject(data);
                    if (obj.optInt("type") == 1) {

                        new Permission(WebViewActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, new PermissionListener() {
                            @Override
                            public void onGranted() {

                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, PICK_CONTACT);

                                //上传通讯录
                                if (!mHasUploadAddressBook) {
                                    uploadContactsOnly();
                                }
                            }

                            @Override
                            public void onDenied() {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void handler(String data, CallBackFunction function) {

        mCallBackFunction = function;
        CLog.e("karl handler", "从js端获取到的数据--->" + data);
        try {
            if (TextUtils.isEmpty(data)) return;

            JSONObject obj = new JSONObject(data);
            // 1 orc 认证 2 抓取到短信、通讯录等 3 重新抓取到短信、通讯录等 4 返回首页
            if (obj.optInt("type") == 1) {//orc 认证  跳OCR认证页面
            } else if (obj.optInt("type") == 2) {//2 点击获取额度时
                handleType2(obj, function);
            } else if (obj.optInt("type") == 3) {//3 借款时候
            } else if (obj.optInt("type") == 4 || obj.optInt("type") == 5) {//4 5 返回首页
                IntentUtils.gotoMainActivity(mContext, MainActivity.TAB_HOME);
                finish();
            } else if (obj.optInt("type") == 7) {//借款成功
            } else if (obj.optInt("type") == 8) {//还款成功
            } else if (obj.optInt("type") == 12) {//四要素成功，关闭上一个ocr页面
            } else if (obj.optInt("type") == 13) {//进行活体识别
                startActivity(StartLivenessActivity.createIntent(mContext));
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initWebViewListener() {
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

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
                mProgressBar.setProgress(newProgress);
                Log.d("WebViewActivity2", "newProgress:" + newProgress);
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
                mUploadMessage = uploadMsg;
                pickFile();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessageArray = filePathCallback;
                pickFile();
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
                final SslErrorHandler mHandler;
                mHandler = handler;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("ssl Verifikasi sertifikat gagal");
                builder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.proceed();
                    }
                });
                builder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.cancel();
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                            mHandler.cancel();
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                Log.d("WebViewActivity2", "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                Log.d("WebViewActivity2", "onPageFinished");

            }
        });
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, PICK_FILE_RESULT_CODE);
    }

    //-------------------------------------------华丽的分割线------------------------------------------

    /**
     * 2是认证的时候调
     */
    private void handleType2(JSONObject obj, CallBackFunction function) {
        this.type2or3Obj = obj;
        this.type2or3Function = function;
        requestPermissions();
    }

    /**
     * 开始申请权限
     */
    public void requestPermissions() {
        new Permission(mContext, new String[]{
                Manifest.permission.READ_CONTACTS
        }, new PermissionListener() {
            @Override
            public void onGranted() {
                uploadAccordingPermissions();
            }

            @Override
            public void onDenied() {
                uploadAccordingPermissions();
            }
        });
    }


    private void uploadAccordingPermissions() {
        if (isGetNecessaryPermission()) {
            uploadNecessaryData(type2or3Obj, type2or3Function, true);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("isSucceed", "1");
            if (type2or3Function != null) {
                type2or3Function.onCallBack(new Gson().toJson(map));//回传数据给web
            }
        }
    }

    private boolean isGetNecessaryPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 上传通讯录
     */
    private void uploadContactsOnly() {
        new UploadDataBySingle().uploadAddressBook(BaseApplication.mUserId, new UploadDataBySingle.UploadAddressBookListener() {
            @Override
            public void success() {
                mHasUploadAddressBook = true;
                SPUtils.putBoolean(ConstantValue.UPLOAD_CONTACT_SUCCESS, true);
                SPUtils.putLong(ConstantValue.UPLOAD_CONTACT_TIME, System.currentTimeMillis());
            }

            @Override
            public void error() {

            }
        });
    }

    /**
     * 开始申请权限
     */
    public void prepareUpdateNecessaryData() {
        String[] startPermission = new String[]{
                Manifest.permission.READ_CONTACTS
        };
        new Permission(this, startPermission, new PermissionListener() {
            @Override
            public void onGranted() {
                uploadNecessaryData(type2or3Obj, type2or3Function, true);
            }

            @Override
            public void onDenied() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_FILE_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadMessageArray) {
                return;
            }
            if (null != mUploadMessage && null == mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                if (result != null) {
                    mUploadMessage.onReceiveValue(result);
                }
                mUploadMessage = null;
            }

            if (null == mUploadMessage && null != mUploadMessageArray) {
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                if (result != null) {
                    mUploadMessageArray.onReceiveValue(new Uri[]{result});
                } else {
                    mUploadMessageArray.onReceiveValue(null);
                }
                mUploadMessageArray = null;
            }

        } else if (requestCode == OCR_AUTHENTICATE) {

            if (resultCode == 1000) {
                if (mCallBackFunction != null) {
                    mCallBackFunction.onCallBack("");
                }
            }
        } else if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                getContact(intent.getData());
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("isSucceed", "0");
                map.put("username", "");
                map.put("mobile", "");
                String str = new Gson().toJson(map);
                if (singleContactCallBack != null) {
                    singleContactCallBack.onCallBack(str);
                }
                CLog.e("传的参数", "str-->" + str);
            }
        }
    }

    /**
     * 获取单个联系人
     */
    private void getContact(Uri contactData) {

        Cursor c = getContentResolver().query(contactData, null, null, null, null);

        Map<String, String> map = new HashMap<>();
        if (c != null && c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String phoneNumber = "";
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId,
                        null,
                        null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }
            CLog.e("获取到的联系人", "name-->" + name + ";phoneNumber" + phoneNumber);
            map.put("isSucceed", "1");
            map.put("username", name);
            map.put("mobile", phoneNumber.replace(" ", "").trim());
            String str = new Gson().toJson(map);
            CLog.e("传的参数", "str-->" + str);
            if (singleContactCallBack != null) {
                singleContactCallBack.onCallBack(str);
            }
        } else {

            map.put("isSucceed", "0");
            map.put("username", "");
            map.put("mobile", "");
            String str = new Gson().toJson(map);
            CLog.e("传的参数", "str-->" + str);
            if (singleContactCallBack != null) {
                singleContactCallBack.onCallBack(str);
            }
        }
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 上传数据
     */
    private void uploadNecessaryData(JSONObject obj, CallBackFunction function, boolean isGranted) {
        final ProgressDialog dialog = new ProgressDialog(WebViewActivity.this);
        dialog.setMessage(getString(R.string.dialog_loading));
        if (!mContext.isFinishing()) {
            dialog.show();
        }
        new UploadNecessaryData().upload(BaseApplication.mUserId, new UploadNecessaryData.UploadDataListener() {
            @Override
            public void success() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Map<String, String> map = new HashMap<>();
                        map.put("isSucceed", "1");
                        function.onCallBack(new Gson().toJson(map));//回传数据给web
                    }
                });
            }

            @Override
            public void error() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        CLog.e("karl", "所有数据传输失败...");

                        dialog.dismiss();
                        Map<String, String> map = new HashMap<>();
//                        map.put("isSucceed", "0");   失败情况也当成功处理 2019-04-19 18:27:14
                        map.put("isSucceed", "1");
                        function.onCallBack(new Gson().toJson(map));
                    }
                });
            }
        }, isGranted);
    }

    @Override
    protected void initializeImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.theme_color));
        }
    }
}
