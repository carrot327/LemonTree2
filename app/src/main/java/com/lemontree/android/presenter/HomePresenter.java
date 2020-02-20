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
import com.lemontree.android.R;
import com.lemontree.android.base.BasePresenter;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.request.GetBorrowInfoReqBean;
import com.lemontree.android.bean.request.GoBorrowReqBean;
import com.lemontree.android.bean.request.HomeDataRequestBean;
import com.lemontree.android.bean.response.BorrowApplyInfoResBean;
import com.lemontree.android.bean.response.BorrowResBean;
import com.lemontree.android.bean.response.CouponResBean;
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
import com.minchainx.permission.util.PermissionListener;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import okhttp3.Call;

import static com.lemontree.android.network.OKHttpClientEngine.getNetworkClient;
import static com.lemontree.android.ui.fragment.HomeFragment.mSelectAmount;
import static com.lemontree.android.ui.fragment.HomeFragment.mSelectType;
import static com.lemontree.android.utils.UIUtils.showToast;

public class HomePresenter extends BasePresenter<IHomeView> {
    private Context mContext;
    private BorrowApplyInfoResBean mBorrowApplyInfoResBean;
    private final ProgressDialog mDialog;


    public HomePresenter(Context context, IHomeView view, Fragment fragment) {
        super(context, view, fragment);
        this.mContext = context;
        loadData(true);
        mDialog = new ProgressDialog(mContext);
    }

    public void loadData(boolean showLoading) {
        getHomeMainData();
    }

    /**
     * 获取首页主借款数据
     */
    public void getHomeMainData() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading...");
        dialog.show();
        HomeDataRequestBean homeTabRequestBean = new HomeDataRequestBean();
        homeTabRequestBean.orderid = "";

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_HOMEPAGE_TAB)
                .content(new Gson().toJson(homeTabRequestBean))
                .build()
                .execute(getNetworkClient(), new GenericCallback<HomeDataResBean>() {

                    @Override
                    public void onSuccess(Call call, HomeDataResBean response, int id) {
                        dialog.dismiss();
                        if (mView != null) {
                            mView.stopRefresh();
                        }
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (mView != null) {
                                    mView.setHomeData(response);
                                }
                            } else {
//                                if (BuildConfig.DEBUG)
//                                    showToast("Code:" + response.res_code + "," + response.res_msg + "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        dialog.dismiss();
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
                                mView.setRefuseState();
                            }
                        }
                    }
                });
    }

    /**
     * 获取订单信息
     */
    public void getOrderDetails() {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_GET_ORDER_DETAILS)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<BorrowApplyInfoResBean>() {
                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(Call call, BorrowApplyInfoResBean response, int id) {
                        mDialog.dismiss();
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (mView != null) {
                                    mView.setOrderInfo(response);
                                }
                            } else {
                                mView.setRefuseState();
                            }
                        }
                    }
                });
    }

    public void getBorrowApplyInfo(int loanAmount, int selectType) {
        GetBorrowInfoReqBean bean = new GetBorrowInfoReqBean();
        bean.loan_amount = loanAmount;
        bean.borrow_type = selectType;

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_BORROW_INFO)
                .content(new Gson().toJson(bean))
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
                                    mView.setSubmitDialogData(response);
                                }
                                mBorrowApplyInfoResBean = response;
                            } else {
                                mView.setRefuseState();
                            }
                        }
                    }
                });
    }

    /**
     * 去借款
     */
    public void goBorrow(int loanAmount, int borrowType) {
        if (mBorrowApplyInfoResBean != null) {
            GoBorrowReqBean bean = new GoBorrowReqBean();
            bean.customer_bank_card_id = mBorrowApplyInfoResBean.customer_bank_card_id;
            bean.loan_amount = loanAmount;
            bean.borrow_type = borrowType;
            NetworkLiteHelper
                    .postJson()
                    .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_CONFIRM_BORROW)
                    .content(new Gson().toJson(bean))
                    .build()
                    .execute(getNetworkClient(), new GenericCallback<BorrowResBean>() {
                        @Override
                        public void onFailure(Call call, Exception exception, int id) {
                            mDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(Call call, BorrowResBean response, int id) {
                            if (response != null) {
                                if (BaseResponseBean.SUCCESS.equals(response.res_code) && mView != null) {
                                    getOrderDetails();
                                } else {
                                    showToast(response.res_msg + "");
                                    mDialog.dismiss();
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
            uploadNecessaryData();
        } else {
            showToast(R.string.allow_permission_and_try_again);
        }
    }

    private boolean isGetNecessaryPermission() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 上传数据
     */
    private void uploadNecessaryData() {
        mDialog.setMessage("Loading...");
        mDialog.show();
        new UploadNecessaryData().upload(BaseApplication.mUserId, new UploadNecessaryData.UploadDataListener() {
            @Override
            public void success() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        goBorrow(mSelectAmount, mSelectType);
                    }
                });
            }

            @Override
            public void error() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //失败情况也当成功处理
                        goBorrow(mSelectAmount, mSelectType);
                    }
                });
            }
        }, true);
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
                            mView.showExtendPageData(response);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        CProgressDialogUtils.cancelProgressDialog((Activity) mContext);
                        showToast("Harap kembali dan coba lagi");
                    }
                });
    }

    /**
     * 获取优惠券信息
     */
    public void getCouponInfo(boolean isNoNeedShowDialog) {
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_GET_COUPON_INFO)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(getNetworkClient(), new GenericCallback<CouponResBean>() {

                    @Override
                    public void onSuccess(Call call, CouponResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if (mView != null) {
                                mView.handleCouponInfo(response);
                            }
                        } else {
                            mView.noCoupon(response);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }
}
