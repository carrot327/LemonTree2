package com.sm.android.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.sm.android.R;
import com.sm.android.base.BaseActivity;
import com.sm.android.base.BaseResponseBean;
import com.sm.android.bean.enventbus.LoginSuccessEvent;
import com.sm.android.bean.request.CheckTuringCodeReqBean;
import com.sm.android.bean.request.CommonReqBean;
import com.sm.android.bean.request.LoginReqBean;
import com.sm.android.bean.request.NeedTuringCheckReqBean;
import com.sm.android.bean.request.SendVerifyCodeReqBean;
import com.sm.android.bean.response.GetUserTypeState;
import com.sm.android.bean.response.IfNeedTuringCheckResBean;
import com.sm.android.bean.response.LoginResponseBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.manager.ConstantValue;
import com.sm.android.manager.NetConstantValue;
import com.sm.android.network.OKHttpClientEngine;
import com.sm.android.ui.widget.TuringCodeDialog;
import com.sm.android.uploadUtil.Tools;
import com.sm.android.utils.IntentUtils;
import com.sm.android.utils.SPUtils;
import com.sm.android.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_username_delete)
    ImageView usernameDelete;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_msg_verify_code)
    EditText etMsgVerifyCode;
    @BindView(R.id.tv_msg_verify_code)
    TextView tvMsgVerifyCode;
    @BindView(R.id.tv_registration_protocol)
    TextView tvRegistrationProtocol;
    @BindView(R.id.rl_msg_verify_code)
    RelativeLayout rlMsg;
    @BindView(R.id.btn_login_confirm)
    Button btnLoginConfirm;

    private String mPhoneNum;

    private boolean isSendingMsg;
    private String countDown;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initializeView() {
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPhoneNum = etPhoneNum.getText().toString().trim();
                setUsernameDeleteButtonState(!TextUtils.isEmpty(mPhoneNum));

                if (rlMsg.getVisibility() == View.VISIBLE) {
                    if (etPhoneNum.getText().toString().trim().length() >= 9
                            && etPhoneNum.getText().toString().trim().length() <= 13
                            && etMsgVerifyCode.getText().toString().trim().length() >= 4) {
                        btnLoginConfirm.setEnabled(true);
                    } else {
                        btnLoginConfirm.setEnabled(false);
                    }
                } else {
                    if (etPhoneNum.getText().toString().trim().length() >= 9
                            && etPhoneNum.getText().toString().trim().length() <= 13) {
                        btnLoginConfirm.setEnabled(true);
                    } else {
                        btnLoginConfirm.setEnabled(false);
                    }
                }
            }
        });
        etMsgVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPhoneNum.getText().toString().trim().length() >= 9
                        && etMsgVerifyCode.getText().toString().trim().length() >= 4) {
                    btnLoginConfirm.setEnabled(true);
                } else {
                    btnLoginConfirm.setEnabled(false);
                }
            }
        });

        tvRegistrationProtocol.setText(
                StringUtils.modifyTextColor(
                        getString(R.string.login_text_privacy),
                        getString(R.string.login_text_Privasi),
                        getResources().getColor(R.color.text_blue_deep)));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initializeImmersiveMode() {
        // 不使用ImmersionBar，以防导致输入框不会自动上移
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.theme_color));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tv_msg_verify_code, R.id.btn_login_confirm, R.id.tv_registration_protocol, R.id.iv_username_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_msg_verify_code:
                //先检查是否需要弹出turing验证
                if (isMobileNO(etPhoneNum.getText().toString().trim())) {
                    getIsNeedTuringCheck();
                } else {
                    showToast(getString(R.string.toast_input_phone_num));
                }
                break;
            case R.id.btn_login_confirm:
                hideInput(etMsgVerifyCode);
                //用户输入手机号后，根据手机号请求新增的一个接口，判断是否是待还款或者逾期状态
                // 如果是，则直接跳首页，并且设置状态为登录。
                // 如果不是，那么显示验证码输入框。
                if (mPhoneNum.startsWith("0")) {
                    mPhoneNum = mPhoneNum.replaceFirst("^0*", "");
                }
                if (rlMsg.getVisibility() == View.VISIBLE) {
                    if (etMsgVerifyCode.getText().toString().trim().length() >= 4) {
                        login();
                    }else {
                        showToast("Silakan masukkan kode verifikasi");
                    }
                } else {
                    checkIsUserAtPayState();
                }
                break;
            case R.id.tv_registration_protocol:
                startActivity(new Intent(this, ProtocolRegisterActivity.class));
                break;
            case R.id.iv_username_delete:
                if (!etPhoneNum.isFocused()) {
                    etPhoneNum.requestFocus();
                }
                etPhoneNum.setText("");
                setUsernameDeleteButtonState(true);
                break;
        }
    }

    /**
     * 获取用户当前type状态
     */
    private void checkIsUserAtPayState() {
        CommonReqBean bean = new CommonReqBean();
        bean.phone = mPhoneNum;

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + NetConstantValue.GET_USER_TYPE_STATE)
                .content(new Gson().toJson(bean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<GetUserTypeState>() {
                    @Override
                    public void onSuccess(Call call, GetUserTypeState response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code) && response.data != null) {
                            if (5 == response.data.userState || 6 == response.data.userState) {
                                Toast.makeText(mContext, R.string.toast_login_success, Toast.LENGTH_SHORT).show();

                                SPUtils.putString(ConstantValue.KEY_LATEST_LOGIN_NAME, response.data.userName, false);
                                SPUtils.putString(ConstantValue.KEY_USER_ID, response.data.userId, true);
                                SPUtils.putString(ConstantValue.PHONE_NUMBER, mPhoneNum, false);
                                SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
                                BaseApplication.sLoginState = true;
                                BaseApplication.sUserName = response.data.userName;
                                BaseApplication.mUserId = response.data.userId;
                                BaseApplication.sPhoneNum = mPhoneNum;
                                //跳到首页
                                EventBus.getDefault().post(new LoginSuccessEvent());
                                IntentUtils.gotoMainActivity(mContext, MainActivity.TAB_HOME);
                            } else {
                                rlMsg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rlMsg.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        rlMsg.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void setUsernameDeleteButtonState(boolean ifShow) {
        if (ifShow) {
            usernameDelete.setVisibility(View.VISIBLE);
        } else {
            usernameDelete.setVisibility(View.GONE);
        }
    }

    TuringCodeDialog turingCodeDialog;

    /*检查是否需要图形码验证*/
    private void getIsNeedTuringCheck() {
        NeedTuringCheckReqBean bean = new NeedTuringCheckReqBean();
        bean.mobile = etPhoneNum.getText().toString().trim();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_IS_NEED_TURING_CHECK)
                .content(new Gson().toJson(bean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<IfNeedTuringCheckResBean>() {
                    @Override
                    public void onSuccess(Call call, IfNeedTuringCheckResBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (1 == response.isgraphicverification) {// 1-需要图灵验证
                                    turingCodeDialog = new TuringCodeDialog(mContext, bean.mobile, new TuringCodeDialog.TuringDialogListener() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog, String verifyCode) {
                                            checkTuringCode(verifyCode);
                                        }

                                        @Override
                                        public void onCancelClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                                    if (!isFinishing()) {
                                        turingCodeDialog.show();
                                    }
                                } else {
                                    getVerifyCode();
                                }
                            } else {
                                Toast.makeText(mContext, response.res_msg + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.err_network_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        Toast.makeText(mContext, getString(R.string.err_network_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*检查图形验证码*/
    private void checkTuringCode(String verifyCode) {
        CheckTuringCodeReqBean checkTuringCodeReBean = new CheckTuringCodeReqBean();
        checkTuringCodeReBean.mobile = etPhoneNum.getText().toString().trim();
        checkTuringCodeReBean.verifycode = verifyCode;
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_CHECK_TURING_CODE)
                .content(new Gson().toJson(checkTuringCodeReBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                //如果成功，则请求【短信验证码】接口
                                String phoneNUm = etPhoneNum.getText().toString().trim();
                                if (isMobileNO(phoneNUm)) {
                                    if (turingCodeDialog != null && turingCodeDialog.isShowing()) {
                                        turingCodeDialog.dismiss();
                                    }
                                    getVerifyCode();
                                } else {
                                    Toast.makeText(mContext, R.string.toast_pls_input_right_phone_num, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, response.res_msg + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.err_network_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        Toast.makeText(mContext, getString(R.string.err_network_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //获取短信验证码
    private void getVerifyCode() {
        SendVerifyCodeReqBean sendVerifyCodeReqBean = new SendVerifyCodeReqBean();
        sendVerifyCodeReqBean.mobile = mPhoneNum;
        sendVerifyCodeReqBean.ip = Tools.getIpAddress();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_VERIFY_CODE)
                .content(new Gson().toJson(sendVerifyCodeReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                Toast.makeText(mContext, R.string.toast_msg_send_success, Toast.LENGTH_SHORT).show();
                                countDown = "";
                                timeCounter();
                            } else {
                                Toast.makeText(mContext, response.res_msg + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, R.string.toast_msg_send_failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        Toast.makeText(mContext, R.string.toast_msg_send_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     *    * 判断字符串是否符合手机号码格式
     *    * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     *    * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     *    * 电信号段: 133,149,153,170,173,177,180,181,189
     *    * @param str
     *    * @return 待检测的字符串
     *    
     **/
    public static boolean isMobileNO(String mobileNums) {
// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        /*String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);*/
//        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums) || mobileNums.length() < 9)
            return false;
        else
            return true;
    }

    private void timeCounter() {
        tvMsgVerifyCode.setEnabled(false);
        if (TextUtils.isEmpty(countDown)) {
            countDownTimer = new CountDownTimer(60000 * 3, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (tvMsgVerifyCode == null) {
                        return;
                    }
                    tvMsgVerifyCode.setText(getString(R.string.login_text_send_again)
                            + "(" + millisUntilFinished / 1000 + "s)");
                }

                @Override
                public void onFinish() {
                    if (tvMsgVerifyCode == null) {
                        return;
                    }
                    tvMsgVerifyCode.setText(getString(R.string.login_text_send_again));
                    tvMsgVerifyCode.setEnabled(true);
                    countDownTimer.cancel();
                }
            }.start();
        } else {
            countDownTimer = new CountDownTimer(Integer.parseInt(countDown) * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (tvMsgVerifyCode == null) {
                        return;
                    }
                    tvMsgVerifyCode.setText(getString(R.string.text_send_again) + "(" + millisUntilFinished / 1000 + "s)");
                }

                @Override
                public void onFinish() {
                    if (tvMsgVerifyCode == null) {
                        return;
                    }
                    tvMsgVerifyCode.setText(getString(R.string.login_text_send_again));
                    tvMsgVerifyCode.setEnabled(true);
                    countDownTimer.cancel();
                }
            }.start();
        }
    }

    private void login() {
        LoginReqBean loginReqBean = new LoginReqBean();
        loginReqBean.mobile = mPhoneNum;
        loginReqBean.verifycode = etMsgVerifyCode.getText().toString().trim();

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_LOGIN)
                .content(new Gson().toJson(loginReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<LoginResponseBean>() {
                    @Override
                    public void onSuccess(Call call, LoginResponseBean response, int id) {
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                Toast.makeText(mContext, R.string.toast_login_success, Toast.LENGTH_SHORT).show();

                                SPUtils.putString(ConstantValue.KEY_LATEST_LOGIN_NAME, response.login_name, false);
                                SPUtils.putString(ConstantValue.KEY_USER_ID, response.user_id, true);
                                SPUtils.putString(ConstantValue.PHONE_NUMBER, mPhoneNum, false);
                                SPUtils.putBoolean(ConstantValue.LOGIN_STATE, true);
                                BaseApplication.sLoginState = true;
                                BaseApplication.sUserName = response.login_name;
                                BaseApplication.mUserId = response.user_id;
                                BaseApplication.sPhoneNum = mPhoneNum;

                                EventBus.getDefault().post(new LoginSuccessEvent());
                                finish();
                            } else {
                                Toast.makeText(mContext, response.res_msg + "", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, R.string.network_get_failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                        Toast.makeText(mContext, R.string.toast_login_failed_try_again, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 强制隐藏输入法键盘
     *
     * @param view EditText
     */
    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
