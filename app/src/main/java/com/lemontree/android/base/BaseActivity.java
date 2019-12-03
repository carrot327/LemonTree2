package com.lemontree.android.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.gyf.barlibrary.ImmersionBar;
import com.minchainx.permission.util.Permission;
import com.lemontree.android.R;
import com.lemontree.android.manager.ActivityCollector;
import com.lemontree.android.ui.widget.AppTitleBar;
import com.lemontree.android.uploadUtil.CheckPermission;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/10 10:40
 * 文件描述:
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    //首先声明权限授权
    public static final int PERMISSION_DENIEG = 1999;//权限不足，权限被拒绝的时候
    public static final int PERMISSION_REQUEST_CODE = 123;//系统授权管理页面时的结果参数
    public static final String PACKAGE_URL_SCHEME = "package:";//权限方案
    private static final String TAG = "BaseActivity";
    public CheckPermission checkPermission;//检测权限类的权限检测器
    private boolean isrequestCheck = true;//判断是否需要系统权限检测。防止和系统提示框重叠
    private FirebaseAnalytics mFirebaseAnalytics;
    protected BaseActivity mContext = this;

    protected P mPresenter;

    protected FrameLayout mBaseContentContainer;

    private View mContentView;

    private AppTitleBar mTitleBar;

//    private Unbinder mUnbinder;

    protected abstract int getLayoutResId();

    protected abstract void initializeView();

    protected abstract void loadData();

    protected void handlerBeforeSetContentView() {
    }

    protected void initializePrepareData() {
        // empty
    }

    /**
     * 初始化沉浸式模式（状态栏、导航栏等样式）
     */

    protected void initializeImmersiveMode() {
        setImmersiveMode(R.color.white, true);
    }

    protected void setImmersiveMode(@ColorRes int statusBarColor, boolean statusBarDarkFont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImmersionBar.with(this)
                    .fitsSystemWindows(true) // 解决状态栏和布局重叠问题，使用该属性，必须指定状态栏颜色
                    .statusBarColor(statusBarColor)
                    .statusBarDarkFont(statusBarDarkFont) // 深色状态栏字体（默认为浅色）
                    .navigationBarEnable(false)
                    .init();
        }
    }

    protected P getPresenter() {
        if (this.mPresenter == null) {
            this.mPresenter = createPresenter();
        }
        return this.mPresenter;
    }

    protected P createPresenter() {
        return null;
    }

    protected View getContentView() {
        return mContentView;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        handlerBeforeSetContentView();
        this.setContentView(this.getLayoutResId());
        ButterKnife.bind(this);
        initializePrepareData();
        initializeView();
        initializeTitleBar();
        initializeImmersiveMode();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void setContentView(int resId) {
        LayoutInflater inflater = getLayoutInflater();
        mContentView = inflater.inflate(resId, null);
        mTitleBar = mContentView.findViewById(R.id.app_titlebar);
        View layoutView;
        if (isUseDefaultTitleBar() && mTitleBar == null) {
            layoutView = inflater.inflate(R.layout.base_content_container_with_titlebar, null);
        } else {
            layoutView = inflater.inflate(R.layout.base_content_container, null);
        }
        mBaseContentContainer = layoutView.findViewById(R.id.base_content_container);
        mBaseContentContainer.addView(mContentView);
        super.setContentView(layoutView);
    }

    public void initializeTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = findViewById(R.id.app_titlebar);
        }
        if (mTitleBar != null) {
            mTitleBar.setOnBtnBackClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            initTitleBar(mTitleBar);
        }
    }


    protected void initTitleBar(AppTitleBar titleBar) {
        // empty
    }

    protected boolean isUseDefaultTitleBar() {
        return false;
    }

    public String[] getPermissions() {
        return null;
    }

    /**
     * 用于权限管理
     * 如果全部授权的话，则直接通过进入
     * 如果权限拒绝，缺失权限时，则使用dialog提示
     *
     * @param requestCode  请求代码
     * @param permissions  权限参数
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE == requestCode && hasAllPermissionGranted(grantResults)) //判断请求码与请求结果是否一致
        {
            isrequestCheck = true;//需要检测权限，直接进入，否则提示对话框进行设置
            afterGetAllGrantedPermission();
        } else {
            //提示对话框设置
            isrequestCheck = false;
            showMissingPermissionDialog();//dialog
        }
    }

    /*
     * 当获取到所需权限后，进行相关业务操作
     */
    public void afterGetAllGrantedPermission() {


    }

    //显示对话框提示用户缺少权限
    public void showMissingPermissionDialog() {


    }

    //获取全部权限
    public boolean hasAllPermissionGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    //请求权限去兼容版本
    public void requestPermissions(String... permission) {
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
    }

    public String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA
    };

    /**
     * 当权限不足时，需要弹出的对话框
     */
    public void continueDialog(String... permissions) {
        List<String> permissionNames = Permission.transformText(this, permissions);
        String message = getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));

        Log.e("权限申请", "msg===>" + message);

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.runtime_title_dialog)
                .setMessage(message)
                .setPositiveButton(com.minchainx.permission.R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        requestPermissions(permissions);
                    }
                })
                .setNegativeButton(R.string.cancel2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public AppTitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    public void finishActivity() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    public boolean isGetLocationPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
