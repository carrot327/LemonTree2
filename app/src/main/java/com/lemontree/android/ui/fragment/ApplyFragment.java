package com.lemontree.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseFragment;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.BankCardQueryReqBean;
import com.lemontree.android.bean.request.CommonReqBean;
import com.lemontree.android.bean.response.AuthStateResBean;
import com.lemontree.android.bean.response.BankcardListResponseBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.ui.activity.ApplyFirstActivity;
import com.lemontree.android.ui.activity.BankInfoActivity;
import com.lemontree.android.ui.activity.InfoGetReadyActivity;
import com.lemontree.android.ui.activity.StartLivenessActivity;
import com.lemontree.android.uploadUtil.UrlHostConfig;
import com.lemontree.android.utils.IntentUtils;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class ApplyFragment extends BaseFragment {
    @BindView(R.id.apply_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.view_tag1)
    View viewTag1;
    @BindView(R.id.iv_user_info)
    ImageView ivUserInfo;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.view_tag2)
    View viewTag2;
    @BindView(R.id.iv_company)
    ImageView ivCompany;
    @BindView(R.id.rl_company_info)
    RelativeLayout rlCompanyInfo;
    @BindView(R.id.view_tag3)
    View viewTag3;
    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.rl_contact_info)
    RelativeLayout rlContactInfo;
    @BindView(R.id.view_tag4)
    View viewTag4;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.rl_photo)
    RelativeLayout rlPhoto;
    @BindView(R.id.btn_confirm)
    Button btnLoginConfirm;
    @BindView(R.id.iv_arrow_apply_1)
    ImageView ivArrowApply1;
    @BindView(R.id.iv_arrow_apply_2)
    ImageView ivArrowApply2;
    @BindView(R.id.iv_arrow_apply_3)
    ImageView ivArrowApply3;
    @BindView(R.id.iv_arrow_apply_4)
    ImageView ivArrowApply4;
    Unbinder unbinder;

    private int mBaseStatus;
    private int mCompanyStatus;
    private int mOcrStatus;
    private int mRelationStatus;
    private boolean mHasBankCard;
    private boolean mHasFacePassed;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_apply;
    }

    @Override
    protected void initializeView(View view) {
        enableLazyLoad(false);

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData(false);
            }
        });

    }

    @Override
    protected void loadData(boolean hasRequestData) {
        getAuthStatusList();
        getBankCardList();
    }

    @OnClick({R.id.rl_user_info, R.id.rl_company_info, R.id.rl_contact_info, R.id.rl_photo, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_info:
                if (mBaseStatus == 0) {
//                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_USER_INFO());
                    startActivity(ApplyFirstActivity.createIntent(mContext));

                } else {
                    showToast(getString(R.string.apply_toast_text_has_done));
                }
                break;
            case R.id.rl_company_info:
                if (mBaseStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_1));
                } else if (mCompanyStatus == 0) {
                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_COMPANY());
                } else {
                    showToast(getString(R.string.apply_toast_text_has_done));
                }
                break;
            case R.id.rl_contact_info:
                if (mBaseStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_1));
                } else if (mCompanyStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_2));
                } else if (mRelationStatus == 0) {
                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_CONTACT());
                } else {
                    showToast(getString(R.string.apply_toast_text_has_done));
                }
                break;
            case R.id.rl_photo:
                if (mBaseStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_1));
                } else if (mCompanyStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_2));
                } else if (mRelationStatus == 0) {
                    showToast(getString(R.string.apply_toast_text_3));
                } else if (mOcrStatus == 0) {
                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_UPLOAD());
                } else {
                    showToast(getString(R.string.apply_toast_text_has_done));
                }
                break;
            case R.id.btn_confirm:
                // 检查顺序：银行卡-》活体-》信息确认
                if (!mHasBankCard) {
                    startActivity(BankInfoActivity.createIntent(mContext));
                } else if (!mHasFacePassed) {
                    startActivity(StartLivenessActivity.createIntent(mContext));
                } else {
                    startActivity(InfoGetReadyActivity.createIntent(mContext));
                }
                break;
        }
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
                        mRefreshLayout.finishRefresh();
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if ("1".equals(response.faceStatus)) {
                                mHasFacePassed = true;
                            } else {
                                mHasFacePassed = false;
                            }

                            if (response.authStatusList.size() > 0) {
                                mBaseStatus = 0;
                                mCompanyStatus = 0;
                                mRelationStatus = 0;
                                mOcrStatus = 0;
                                for (int i = 0; i < response.authStatusList.size(); i++) {
                                    mBaseStatus += response.authStatusList.get(i).baseStatus;
                                    mCompanyStatus += response.authStatusList.get(i).companyStatus;
                                    mRelationStatus += response.authStatusList.get(i).relationStatus;
                                    mOcrStatus += response.authStatusList.get(i).ocrStatus;
                                }
                                Log.d("ApplyFragment", "mBaseStatus:" + mBaseStatus);
                                Log.d("ApplyFragment", "mCompanyStatus:" + mCompanyStatus);
                                Log.d("ApplyFragment", "mRelationStatus:" + mRelationStatus);
                                Log.d("ApplyFragment", "mOcrStatus:" + mOcrStatus);

                                if (mBaseStatus > 0) {
                                    viewTag1.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_yellow));
                                    ivUserInfo.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
                                    ivArrowApply1.setVisibility(View.INVISIBLE);
                                } else {
                                    viewTag1.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_grey));
                                    ivUserInfo.setImageDrawable(getResources().getDrawable(R.drawable.icon_user_disable));
                                    ivArrowApply1.setVisibility(View.VISIBLE);
                                }

                                if (mCompanyStatus > 0) {
                                    viewTag2.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_green));
                                    ivCompany.setImageDrawable(getResources().getDrawable(R.drawable.icon_company));
                                    ivArrowApply2.setVisibility(View.INVISIBLE);
                                } else {
                                    viewTag2.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_grey));
                                    ivCompany.setImageDrawable(getResources().getDrawable(R.drawable.icon_company_disable));
                                    ivArrowApply2.setVisibility(View.VISIBLE);
                                }

                                if (mRelationStatus > 0) {
                                    viewTag3.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_blue));
                                    ivContact.setImageDrawable(getResources().getDrawable(R.drawable.icon_contact));
                                    ivArrowApply3.setVisibility(View.INVISIBLE);
                                } else {
                                    viewTag3.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_grey));
                                    ivContact.setImageDrawable(getResources().getDrawable(R.drawable.icon_contact_disable));
                                    ivArrowApply3.setVisibility(View.VISIBLE);
                                }

                                if (mOcrStatus > 0) {
                                    viewTag4.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_red));
                                    ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_upload));
                                    ivArrowApply4.setVisibility(View.INVISIBLE);
                                } else {
                                    viewTag4.setBackground(getResources().getDrawable(R.drawable.shape_bg_circle_corner_grey));
                                    ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_upload_disable));
                                    ivArrowApply4.setVisibility(View.VISIBLE);
                                }

                                if (mBaseStatus > 0 && mCompanyStatus > 0 && mRelationStatus > 0 && mOcrStatus > 0) {
                                    btnLoginConfirm.setEnabled(true);
                                } else {
                                    btnLoginConfirm.setEnabled(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        mRefreshLayout.finishRefresh();
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
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if (response.bankCardList != null && response.bankCardList.size() > 0) {
                                mHasBankCard = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            loadData(false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            mRefreshLayout.autoRefresh(100);
////            loadData(false);
//        }
    }
}
