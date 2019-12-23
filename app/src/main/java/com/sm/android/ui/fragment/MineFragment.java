package com.sm.android.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.sm.android.R;
import com.sm.android.base.BaseFragment;
import com.sm.android.bean.request.CommonReqBean;
import com.sm.android.bean.response.UserInfoReqBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.manager.ConstantValue;
import com.sm.android.manager.NetConstantValue;
import com.sm.android.network.OKHttpClientEngine;
import com.sm.android.ui.activity.FeedbackActivity;
import com.sm.android.ui.activity.MainActivity;
import com.sm.android.ui.activity.SettingActivity;
import com.sm.android.uploadUtil.UrlHostConfig;
import com.sm.android.utils.IntentUtils;
import com.sm.android.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sm.android.ui.activity.MainActivity.sHasNewUnreadMsg;

public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.rl_my_loan)
    RelativeLayout rlMyLoan;
    @BindView(R.id.rl_help_center)
    RelativeLayout rlHelpCenter;
    @BindView(R.id.rl_online_customer_service)
    RelativeLayout rlOnlineCustomerService;
    @BindView(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @BindView(R.id.rl_privacy_policy)
    RelativeLayout rlPrivacyPolicy;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.iv_titlebar_right)
    ImageView myNoticeMsg;
    @BindView(R.id.msg_red_dot)
    View msgRedDot;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initializeView(View view) {
        // 关闭懒加载
        enableLazyLoad(false);
        initView();
    }

    private void initView() {
        if (BaseApplication.sLoginState) {
            if (TextUtils.isEmpty(BaseApplication.sUserName)) {
                tvUsername.setText(SPUtils.getString(ConstantValue.PHONE_NUMBER, ""));
            } else {
                tvUsername.setText(BaseApplication.sUserName);
            }
            tvUsername.getPaint().setFlags(0);
            tvUsername.setClickable(false);
        } else {
            tvUsername.setText(getResources().getString(R.string.sign_up));
            tvUsername.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvUsername.setClickable(true);
        }
        if (sHasNewUnreadMsg) {
            msgRedDot.setVisibility(View.VISIBLE);
        } else {
            msgRedDot.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void loadData(boolean hasRequestData) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (TextUtils.isEmpty(BaseApplication.sUserName)) {
                getUserInfo();
            }
        }
    }

    @OnClick({R.id.rl_setting, R.id.iv_titlebar_right, R.id.iv_user_avatar, R.id.tv_username, R.id.rl_my_loan, R.id.rl_help_center, R.id.rl_online_customer_service, R.id.rl_about_us, R.id.rl_privacy_policy, R.id.msg_red_dot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_user_avatar:
            case R.id.tv_username:
                if (!BaseApplication.sLoginState) {
                    IntentUtils.startLoginActivityForResult(getActivity(), MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
                }
                break;
            case R.id.rl_my_loan:
                if (BaseApplication.sLoginState) {
                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_BORROW_LIST());
                } else {
                    IntentUtils.startLoginActivityForResult(getActivity(), MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
                }
                break;
            case R.id.rl_help_center:
                if (BaseApplication.sLoginState) {
                    startActivity(new Intent(mContext, FeedbackActivity.class));
                } else {
                    IntentUtils.startLoginActivityForResult(getActivity(), MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
                }
                break;
            case R.id.rl_online_customer_service:
                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_CUSTOMER_SERVICE());
                break;
            case R.id.rl_about_us:
                if (BaseApplication.sLoginState) {
//                    startActivity(new Intent(mContext, FeedbackActivity.class));
                } else {
                    IntentUtils.startLoginActivityForResult(getActivity(), MainActivity.REQUEST_MINE_FRAGMENT_LOGIN);
                }
                break;
            case R.id.rl_privacy_policy:

                break;
            case R.id.iv_titlebar_right:
                sHasNewUnreadMsg = false;
                msgRedDot.setVisibility(View.INVISIBLE);
                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_MSG());
                break;
        }
    }


    private void getUserInfo() {

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_GET_USER_INFO)
                .content(new Gson().toJson(new CommonReqBean()))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<UserInfoReqBean>() {
                    @Override
                    public void onSuccess(Call call, UserInfoReqBean response, int id) {
                        if (response != null) {
                            if (!TextUtils.isEmpty(response.customer_name)) {
                                BaseApplication.sUserName = response.customer_name;
                                SPUtils.putString(ConstantValue.KEY_LATEST_LOGIN_NAME, response.customer_name, false);
                                tvUsername.setText(response.customer_name);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }
}
