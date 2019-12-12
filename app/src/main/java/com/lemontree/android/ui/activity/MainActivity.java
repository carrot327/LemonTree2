package com.lemontree.android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.minchainx.permission.util.PermissionListener;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.lemontree.android.BuildConfig;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.TabResourceBean;
import com.lemontree.android.bean.enventbus.BackPressEvent;
import com.lemontree.android.bean.enventbus.LoginSuccessEvent;
import com.lemontree.android.bean.enventbus.NewMsgEvent;
import com.lemontree.android.bean.request.BankCardQueryReqBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.response.AuthStateResBean;
import com.lemontree.android.bean.response.BankcardListResponseBean;
import com.lemontree.android.bean.response.UnreadMsgStateResBean;
import com.lemontree.android.iview.IMainView;
import com.lemontree.android.manager.ActivityCollector;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.presenter.MainPresenter;
import com.lemontree.android.service.LocationService;
import com.lemontree.android.ui.fragment.ApplyFragment;
import com.lemontree.android.ui.fragment.HomeFragment;
import com.lemontree.android.ui.fragment.MineFragment;
import com.lemontree.android.ui.widget.HomeTabView;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.utils.IntentUtils;
import com.lemontree.android.utils.MultiClickHelper;
import com.lemontree.android.utils.UpdateUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView, HomeTabView.OnTabChangedListener {
    public static final int REQUEST_HOME_FRAGMENT_LOGIN = 0X01;
    public static final int REQUEST_MINE_FRAGMENT_LOGIN = 0x02;
    public static final int REQUEST_SHOP_FRAGMENT_LOGIN = 0x03;
    public static final int REQUEST_FIND_FRAGMENT_LOGIN = 0x04;

    public static final String TAB_INDEX = "tabIndex";
    public static final String TAB_HOME = "HOME";
    public static final String TAB_APPLY = "APPLY";
    public static final String TAB_MINE = "MINE";
    public static boolean sHasNewUnreadMsg;

    private MagicIndicator mMagicIndicator;
    private TabResourceBean tabResourceBean3Tab, tabResourceBean2Tab;
    private MultiClickHelper mMultiClickHelper = new MultiClickHelper(2, 2000);
    private int mCurrentIndex = -1;
    private List<Fragment> mListFragments = new ArrayList<>();
    private List<Fragment> mListFragments2Tab = new ArrayList<>();
    private List<Fragment> mListFragments3Tab = new ArrayList<>();
    public static boolean sHasGetAuthStatusList = false;
    public static boolean sHasGetBankCardList = false;

    public static boolean sHasBankCard;
    public static boolean sHasFacePassed;

    //首页滑条选择
    public static String sFormatSelectAmount;
    public static String sFormatSelectTime;
    public static String sFormatSelectInterest;
    private FrameLayout mainFrameLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeView() {
        mainFrameLayout = findViewById(R.id.frame_main_fragment_container);
        mListFragments = mListFragments3Tab;
        initIndicator(tabResourceBean3Tab);
        switchTab(0);
    }

    @Override
    protected void loadData() {
        UpdateUtil.checkUpdate(mContext);
//        checkHasUnreadMsg();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LocationService.getInstance().registerListener();
        checkStartPermission();

        if (!BuildConfig.DEBUG) {
            onCheckGooglePlayServices();
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("firebase", "getInstanceId failed", task.getException());
                                return;
                            }
                            String token = task.getResult().getToken();
                            Log.d("karl", token);
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            String tabIndex = getIntent().getStringExtra(TAB_INDEX);

            if (!TextUtils.isEmpty(tabIndex)) {
                switch (tabIndex) {
                    case TAB_HOME:
                    default:
                        switchTab(0);
                        break;
                    case TAB_APPLY:
                        if (mListFragments.get(1) instanceof ApplyFragment) {
                            switchTab(1);
                        }
                        break;
                    case TAB_MINE:
                        switchTab(mListFragments.size() - 1);
                        break;
                }
            }
        }
    }

    @Override
    protected void initializePrepareData() {
        initTabRes();
    }


    private void initTabRes() {
        HomeFragment homeFragment = new HomeFragment();
        ApplyFragment applyFragment = new ApplyFragment();
        MineFragment mineFragment = new MineFragment();

        tabResourceBean2Tab = new TabResourceBean();
        tabResourceBean3Tab = new TabResourceBean();

        mListFragments2Tab.add(homeFragment);
        mListFragments2Tab.add(mineFragment);

        mListFragments3Tab.add(homeFragment);
        mListFragments3Tab.add(applyFragment);
        mListFragments3Tab.add(mineFragment);

        tabResourceBean2Tab.iconNormal = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_mine};
        tabResourceBean2Tab.iconSelected = new int[]{R.drawable.icon_tab_home_selected, R.drawable.icon_tab_mine_selected};
        tabResourceBean2Tab.tabText = new String[]{"Pinjaman", "Saya"};
        tabResourceBean2Tab.textColorNormal = R.color.tab_text;
        tabResourceBean2Tab.textColorSelected = R.color.tab_text_hover;

        tabResourceBean3Tab.iconNormal = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_apply, R.drawable.icon_tab_mine};
        tabResourceBean3Tab.iconSelected = new int[]{R.drawable.icon_tab_home_selected, R.drawable.icon_tab_apply_selected, R.drawable.icon_tab_mine_selected};
        tabResourceBean3Tab.tabText = new String[]{"Pinjaman", "Informasi", "Saya"};
        tabResourceBean3Tab.textColorNormal = R.color.tab_text;
        tabResourceBean3Tab.textColorSelected = R.color.tab_text_hover;

    }

    private void initIndicator(TabResourceBean tabResourceBean) {
        mMagicIndicator = getContentView().findViewById(R.id.indicator_main);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setBackgroundColor(getResources().getColor(R.color.white));
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabResourceBean.tabText != null ? tabResourceBean.tabText.length : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                HomeTabView homeTabView = new HomeTabView(context, index, tabResourceBean);
                homeTabView.setOnTabChangedListener(MainActivity.this);
                homeTabView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchTab(index);
                    }
                });
                return homeTabView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
    }

    /**
     * 切换标签页
     */
    @Override
    public void switchTab(int index) {
        if (mListFragments == null || index >= mListFragments.size()) {
            return;
        }

        if (mListFragments.get(index) instanceof ApplyFragment) {
            if (!BaseApplication.sLoginState) {
                IntentUtils.startLoginActivity(mContext);
                return;
            }
        }

        // tab切换
        mMagicIndicator.onPageSelected(index);
        // fragment切换
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        //把不是当前tab的所有其他fragment都hide起来
        for (int i = 0; i < mListFragments.size(); i++) {
            if (i == index) {//终止本单次循环（非所有循环）
                continue;
            }
            fragment = mListFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        //然后展示当前点击的index的fragment（有则show，没有则add，然后commit）
        fragment = mListFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_main_fragment_container, fragment);
        }
        fragmentTransaction.commitNowAllowingStateLoss();
        mCurrentIndex = index;
        refreshImmersiveMode();
    }

    public void hideApplyTab() {
        if (mListFragments.size() != 2) {
            initIndicator(tabResourceBean2Tab);
            mListFragments = mListFragments2Tab;
            if (mCurrentIndex == 2) {
                switchTab(1);
            } else {
                switchTab(0);
            }
        }
    }

    public void showApplyTab() {
        if (mListFragments.size() != 3) {
            initIndicator(tabResourceBean3Tab);
            mListFragments = mListFragments3Tab;
            if (mListFragments.get(mCurrentIndex) instanceof MineFragment) {
                switchTab(2);
            } else {
                switchTab(0);
            }
        }
    }


    private void refreshImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mCurrentIndex == 0) {
                ImmersionBar.with(this)
                        .reset()
                        .fitsSystemWindows(true) // 解决状态栏和布局重叠问题，使用该属性，必须指定状态栏颜色
                        .statusBarColor(R.color.white)
                        .statusBarDarkFont(true)
                        .navigationBarEnable(false)
                        .init();
            } else {//我的
                ImmersionBar.with(this)
                        .fitsSystemWindows(true)
                        .statusBarColor(R.color.theme_color)
                        .statusBarDarkFont(false)
                        .navigationBarEnable(false)
                        .init();
            }
        }
    }

    private Map<String, File> imgMap = new HashMap<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_HOME_FRAGMENT_LOGIN:
                if (BaseApplication.sLoginState) {
                    switchTab(0);
                }
                break;
            case REQUEST_MINE_FRAGMENT_LOGIN:
                if (BaseApplication.sLoginState) {
                    switchTab(mListFragments.size() - 1);
                }
                break;
            case REQUEST_FIND_FRAGMENT_LOGIN://发现
            case REQUEST_SHOP_FRAGMENT_LOGIN://商城
                switchTab(mCurrentIndex);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        // 双击退出
        if (mMultiClickHelper.click()) {
            super.onBackPressed();
            ActivityCollector.finishAll();

            System.exit(0);
        } else {
            Toast.makeText(this, getString(R.string.logout_hint), Toast.LENGTH_SHORT).show();
        }
        EventBus.getDefault().post(new BackPressEvent());
    }

    @Override
    public void onChanged(int index) {

    }

    /**
     * 开启定位服务
     */
    private void startLocationService() {
        if (isGetLocationPermission()) {
            LocationService.getInstance().start();
        }
    }

    private void checkStartPermission() {
//        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(mContext, "2222", Toast.LENGTH_SHORT).show();
//
//        } else {
//            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
//            Toast.makeText(mContext, "未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理", Toast.LENGTH_SHORT).show();
//        }

        new Permission(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
//                Manifest.permission.READ_CONTACTS,
//                Manifest.permission.READ_SMS
        }, new PermissionListener() {
            //成功授权和失败授权回调中，都检查下是否给了定位权限
            @Override
            public void onGranted() {
                startLocationService();
            }

            @Override
            public void onDenied() {
                startLocationService();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccessEvent event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationService.getInstance().unregisterListener(); //注销掉监听
        LocationService.getInstance().stop(); //停止定位服务
        EventBus.getDefault().unregister(this);
    }

    /**
     * 新消息提示
     */
    public void checkHasUnreadMsg() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_NEW_MSG_REMIND)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<UnreadMsgStateResBean>() {
                    @Override
                    public void onFailure(Call call, Exception exception, int id) {

                    }

                    @Override
                    public void onSuccess(Call call, UnreadMsgStateResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if ("1".equals(response.status)) {
                                sHasNewUnreadMsg = true;
                            } else {
                                sHasNewUnreadMsg = false;
                            }
                            EventBus.getDefault().post(new NewMsgEvent());

                        }
                    }
                });
    }

    /**
     * 获取银行卡
     */
    public void getBankCardList() {
        BankCardQueryReqBean bean = new BankCardQueryReqBean();
        bean.type = "1";

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_BANKCARD_QUERY)
                .content(new Gson().toJson(bean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BankcardListResponseBean>() {

                    @Override
                    public void onSuccess(Call call, BankcardListResponseBean response, int id) {
                        sHasGetBankCardList = true;

                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if (response.bankCardList != null && response.bankCardList.size() > 0) {
                                sHasBankCard = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {

                    }
                });
    }

    /**
     * 获取认证状态
     */
    public void getAuthStatusList() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_GET_AUTH_STATE)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<AuthStateResBean>() {

                    @Override
                    public void onSuccess(Call call, AuthStateResBean response, int id) {
                        sHasGetAuthStatusList = true;

                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if ("1".equals(response.faceStatus)) {
                                sHasFacePassed = true;
                            } else {
                                sHasFacePassed = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }


    /**
     * 判断手机是否支持google play服务
     * @param context
     * @return
     */
    /**
     * 检查 Google Play 服务
     */
    private void onCheckGooglePlayServices() {
        // 验证是否已在此设备上安装并启用Google Play服务，以及此设备上安装的旧版本是否为此客户端所需的版本
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        Log.d("MainActivity", "code:" + code);
        if (code == ConnectionResult.SUCCESS) {
            // 支持Google服务
        } else {
            /**
             * 依靠 Play 服务 SDK 运行的应用在访问 Google Play 服务功能之前，应始终检查设备是否拥有兼容的 Google Play 服务 APK。
             * 我们建议您在以下两个位置进行检查：主 Activity 的 onCreate() 方法中，及其 onResume() 方法中。
             * onCreate() 中的检查可确保该应用在检查成功之前无法使用。
             * onResume() 中的检查可确保当用户通过一些其他方式返回正在运行的应用（比如通过返回按钮）时，检查仍将继续进行。
             * 如果设备没有兼容的 Google Play 服务版本，您的应用可以调用以下方法，以便让用户从 Play 商店下载 Google Play 服务。
             * 它将尝试在此设备上提供Google Play服务。如果Play服务已经可用，则Task可以立即完成返回。
             */
//            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

            // 或者使用以下代码

            /**
             * 通过isUserResolvableError来确定是否可以通过用户操作解决错误
             */
            if (GoogleApiAvailability.getInstance().isUserResolvableError(code)) {
                /* *
                 * 返回一个对话框，用于解决提供的errorCode。
                 * @param activity  用于创建对话框的父活动
                 * @param code      通过调用返回的错误代码
                 * @param activity  调用startActivityForResult时给出的requestCode*/

                GoogleApiAvailability.getInstance().getErrorDialog(this, code, 200).show();
            }

        }
    }
}
