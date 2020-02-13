package com.kantong.android.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kantong.android.BuildConfig;
import com.kantong.android.R;
import com.kantong.android.base.BaseDialog;
import com.kantong.android.base.BaseFragment;
import com.kantong.android.base.BaseResponseBean;
import com.kantong.android.bean.enventbus.BackPressEvent;
import com.kantong.android.bean.enventbus.NewMsgEvent;
import com.kantong.android.bean.response.BorrowApplyInfoResBean;
import com.kantong.android.bean.response.CouponResBean;
import com.kantong.android.bean.response.GetExtendFeeResBean;
import com.kantong.android.bean.response.GetPayWayListResBean;
import com.kantong.android.bean.response.HomeDataResBean;
import com.kantong.android.iview.IHomeView;
import com.kantong.android.manager.BaseApplication;
import com.kantong.android.manager.ConstantValue;
import com.kantong.android.manager.DialogFactory;
import com.kantong.android.presenter.HomePresenter;
import com.kantong.android.ui.activity.MainActivity;
import com.kantong.android.ui.activity.PartPayActivity;
import com.kantong.android.ui.activity.ProtocolBorrowActivity;
import com.kantong.android.ui.activity.StartLivenessActivity;
import com.kantong.android.uploadUtil.UrlHostConfig;
import com.kantong.android.utils.CurrencyFormatUtils;
import com.kantong.android.utils.IntentUtils;
import com.kantong.android.utils.LogoutUtil;
import com.kantong.android.utils.MyTimeUtils;
import com.kantong.android.utils.SPUtils;
import com.kantong.android.utils.UIUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.Gravity.CENTER;
import static com.kantong.android.manager.BaseApplication.isOpenGodMode;
import static com.kantong.android.ui.activity.MainActivity.TAB_APPLY;
import static com.kantong.android.ui.activity.MainActivity.sFormatSelectAmount;
import static com.kantong.android.ui.activity.MainActivity.sFormatSelectInterest;
import static com.kantong.android.ui.activity.MainActivity.sFormatSelectTime;
import static com.kantong.android.ui.activity.MainActivity.sHasBankCard;
import static com.kantong.android.ui.activity.MainActivity.sHasFacePassed;
import static com.kantong.android.ui.activity.MainActivity.sHasGetAuthStatusList;
import static com.kantong.android.ui.activity.MainActivity.sHasGetBankCardList;
import static com.kantong.android.ui.activity.MainActivity.sHasNewUnreadMsg;
import static com.kantong.android.uploadUtil.Tools.isGooglePlayChannel;
import static com.kantong.android.uploadUtil.Tools.isNotGooglePlayChannel;

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView {
    @BindView(R.id.home_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.iv_titlebar_right)
    ImageView btnTitleBarRight;
    @BindView(R.id.iv_home_back)
    ImageView btnTitleBarBack;
    @BindView(R.id.isb_progress_amount)
    TextView mSbIndicatorAmount;
    @BindView(R.id.isb_progress_amount2)
    TextView mSbIndicatorAmount2;
    @BindView(R.id.seek_bar_want_amount)
    SeekBar mSeekBarAmount;
    @BindView(R.id.seek_bar_want_amount2)
    SeekBar mSeekBarAmount2;
    @BindView(R.id.tv_apply_info_name)
    TextView tvApplyInfoName;
    @BindView(R.id.tv_delay_time)
    TextView tvDelayTime;
    @BindView(R.id.tv_apply_info_time)
    TextView tvApplyInfoDue;
    @BindView(R.id.tv_delay_interest)
    TextView tvDelayInterest;
    @BindView(R.id.tv_apply_info_amount)
    TextView tvApplyInfoAmount;
    @BindView(R.id.tv_apply_info_bank_name)
    TextView tvApplyInfoBankName;
    @BindView(R.id.tv_apply_info_bank_card_number)
    TextView tvApplyInfoBankCardNumber;
    @BindView(R.id.tv_loan_info_phone_num)
    TextView tvLoanInfoPhoneNum;
    @BindView(R.id.tv_loan_info_ktp)
    TextView tvLoanInfoKtp;
    @BindView(R.id.tv_loan_info_loan_amount)
    TextView tvLoanInfoLoanAmount;
    @BindView(R.id.tv_loan_info_due)
    TextView tvLoanInfoDue;
    @BindView(R.id.tv_loan_info_bank_name)
    TextView tvLoanInfoBankName;
    @BindView(R.id.tv_loan_info_bank_card_number)
    TextView tvLoanInfoBankCardNumber;
    @BindView(R.id.tv_loan_info_interest)
    TextView tvLoanInfoInterest;
    @BindView(R.id.tv_loan_info_total_get_amount)
    TextView tvLoanInfoTotalGetAmount;
    @BindView(R.id.tv_left_day)
    TextView tvLeftDay;
    @BindView(R.id.tv_time_text)
    TextView tvTimeText;
    @BindView(R.id.tv_below_hint)
    TextView tvBelowHint;
    @BindView(R.id.tv_total_borrow_amount)
    TextView tvTotalBorrowAmount;
    @BindView(R.id.tv_pay_deadline)
    TextView tvPayDeadline;
    @BindView(R.id.tv_pay_deadline_delay)
    TextView tvPayDeadlineDelay;
    @BindView(R.id.tv_top_text)
    TextView tvTopText;
    @BindView(R.id.tv_min_amt)
    TextView tvMinAmt;
    @BindView(R.id.tv_max_amt)
    TextView tvMaxAmt;
    @BindView(R.id.msg_red_dot)
    View msgRedDot;
    @BindView(R.id.btn_home)
    Button btnHome;
    @BindView(R.id.include_home_layout_seek_bar)
    LinearLayout includeSeekBar;
    @BindView(R.id.include_home_layout_borrow)
    LinearLayout includeBorrow;
    @BindView(R.id.include_home_layout_pay)
    LinearLayout includePayAtTime;
    @BindView(R.id.include_home_layout_pay_or_delay)
    LinearLayout extendPage;
    @BindView(R.id.include_home_borrow_apply_info)
    LinearLayout applyInfoPage;
    @BindView(R.id.include_home_borrow_loan_info)
    LinearLayout loanInfoPage;
    @BindView(R.id.ll_borrow_protocol)
    LinearLayout llBorrowProtocol;
    @BindView(R.id.tv_borrow_protocol)
    TextView tvBorrowProtocol;
    @BindView(R.id.ll_part_pay)
    LinearLayout llPartPayEntry;
    @BindView(R.id.ll_delay_pay_entry)
    LinearLayout llDelayPayEntry;
    @BindView(R.id.tv_delay_pay_entry)
    TextView tvDelayPayEntry;
    @BindView(R.id.tv_part_pay_entry)
    TextView tvPartPayEntry;
    @BindView(R.id.rl_seek_bar_2)
    RelativeLayout rlSeekBar2;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.rl_coupon_borrow)
    RelativeLayout rlCouponBorrow;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_coupon_borrow)
    TextView tvCouponBorrow;
    @BindView(R.id.tv_final_pay_amount)
    TextView tvFinalPayAmount;
    @BindView(R.id.tv_top_right_content)
    TextView tvBorrowTimeRangeTint;
    @BindView(R.id.iv_question_loan_time)
    ImageView ivLoanTimeQuestion;


    private static final String VIEW_SEEK_BAR = "viewSeekBar";//home_layout_seek_bar
    private static final String VIEW_BORROW = "viewBorrow";//home_layout_borrow
    private static final String VIEW_PAY_AT_TIME = "viewPayAtTime";//home_layout_pay
    private static final String VIEW_PAY_EXTENT = "viewPayDelay";//home_layout_delay_pay

    private static final int EXTENSION_DAYS = 7;//展期天数

    private String DEFAULT_SHOW_VIEW = VIEW_SEEK_BAR;

    public static int mSelectAmount = 1000000;
    public static int mSelectType = 1;//默认为1。  1为7天   2为14天  3为9天
    private HomeDataResBean mHomeData;
    private String[] mPayWayList;
    private String mCurrentView;
    private boolean isRefuse;
    private CouponResBean mCouponData;
    private String mAfterCutAmount;
    private String mOriginPayAmount;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(mContext, this, this);
    }

    @Override
    protected void initializeView(View view) {
        enableLazyLoad(false);
        showHomeView(DEFAULT_SHOW_VIEW);

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getHomeMainData();
            }
        });
        mSeekBarAmount.setProgress(mSelectAmount);
        mSeekBarAmount.setOnSeekBarChangeListener(seekBarAmountListener);
        mSeekBarAmount2.setOnSeekBarChangeListener(seekBarAmountListener2);

        mSbIndicatorAmount.setText(formatNumber(mSelectAmount));
        tvPartPayEntry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvDelayPayEntry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnHome.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (BuildConfig.DEBUG) {
                    startActivity(StartLivenessActivity.createIntent(mContext));
//                    IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_UPLOAD());
//                    DialogFactory.createNoticeDialog(mContext, "Maaf, berdasarkan informasi Anda, kami saat ini hanya dapat memberi Anda pinjaman 9 hari.").show();
                }
                return true;
            }
        });
    }

    @Override
    protected void loadData(boolean hasRequestData) {
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            mPresenter.getHomeMainData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getHomeMainData();
        }
    }

    @OnClick({R.id.iv_titlebar_right, R.id.iv_home_back, R.id.btn_home, R.id.ll_delay_pay_entry, R.id.tv_borrow_protocol, R.id.ll_part_pay, R.id.rl_coupon, R.id.rl_coupon_borrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_back:
                if (VIEW_PAY_EXTENT.equals(mCurrentView)) {
                    showHomeView(VIEW_PAY_AT_TIME);
                    if ("5".equals(mHomeData.type) || "8".equals(mHomeData.type)) {//逾期
                        llDelayPayEntry.setVisibility(View.VISIBLE);
                        partPayVisibleControl();
                    }
                    btnHome.setText(R.string.btn_text_go_pay);//去还款
                }
                break;
            case R.id.iv_titlebar_right:
                sHasNewUnreadMsg = false;
                msgRedDot.setVisibility(View.INVISIBLE);
                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_MSG());
                break;
            case R.id.btn_home:
                handleButtonClick();
                break;
            case R.id.ll_delay_pay_entry://进入展期页 ccc
                showHomeView(VIEW_PAY_EXTENT);
                mPresenter.getExtendFee();
                llDelayPayEntry.setVisibility(View.GONE);
                llPartPayEntry.setVisibility(View.GONE);
                btnHome.setText(R.string.btn_text_extend_pay);
                break;
            case R.id.ll_part_pay://部分还款
                Intent intent = PartPayActivity.createIntent(mContext);
                intent.putExtra("totalPayAmount", mHomeData.repayAmt);
                intent.putExtra("payWayArr", mPayWayList);
                startActivity(intent);
                break;
            case R.id.tv_borrow_protocol:
                startActivity(new Intent(mContext, ProtocolBorrowActivity.class));
                break;
            case R.id.rl_coupon:
                if (mCouponData != null && "1".equals(mCouponData.couponStatus)) {
                    DialogFactory.createSelectCouponDialog(mContext, tvCoupon, tvFinalPayAmount, mOriginPayAmount, mAfterCutAmount, mCouponData).show();
                } else {//0 未激活  2已使用   或逾期
                    DialogFactory.createNoticeDialog(mContext, "Jika anda bisa bayarkan pinjaman anda sebelum tanggal jatuh tempo, anda akan mendapatkan kupon diskon untuk pembayaran pinjaman berikutnya. Dan limit pinjaman anda juga akan naik.").show();
                }
                break;
            case R.id.rl_coupon_borrow:
                if (mCouponData != null && !TextUtils.isEmpty(mCouponData.premium_rate)) {
                    DialogFactory.createNoticeDialog(mContext, "Setelah melunasi pinjaman kali ini anda akan mendapatkan satu kupon yang bisa potong " + mCouponData.premium_rate + "% dari jumlah pinjaman.").show();
                } else {
                    DialogFactory.createNoticeDialog(mContext, "Jika anda bisa bayarkan pinjaman anda sebelum tanggal jatuh tempo, anda akan mendapatkan kupon diskon untuk pembayaran pinjaman berikutnya. Dan limit pinjaman anda juga akan naik.").show();
                }
                break;
        }
    }

    /**
     * 设置首页数据 #aaa
     */
    public void setHomeData(HomeDataResBean response) {
        mHomeData = response;
        checkIsNewOrderId(response);
        btnHome.setEnabled(true);
        llDelayPayEntry.setVisibility(View.GONE);
        llPartPayEntry.setVisibility(View.GONE);
        if ("0000".equals(response.res_code)) {
            String type = response.type;
            if (!TextUtils.isEmpty(type)) {
                setApplyTabVisible(type);
                switch (type) {
                    case "1":
                    case "11"://防止重复借款
                    default:
                        showHomeView(DEFAULT_SHOW_VIEW);
                        btnHome.setText(R.string.text_apply_loan);
                        break;
                    case "3"://认证成功
                        showHomeView(VIEW_SEEK_BAR);
                        ((MainActivity) getActivity()).getAuthStatusList();
                        ((MainActivity) getActivity()).getBankCardList();
                        break;
                    case "9"://额度计算中
                        setBorrowPageBaseInfo();
                        tvTopText.setText(getResources().getText(R.string.top_text_status_9));
                        break;
                    case "4"://可借款
                        setBorrowPageBaseInfo();
                        setSeekBarValue();
                        if (View.VISIBLE == applyInfoPage.getVisibility()) {
                            btnHome.setText(R.string.btn_text_confirm);//点击方法 showSubmitSuccessDialog
                        }

                        if (isRefuse) {
                            tvTopText.setText(R.string.top_text_status_4_no_amount);
                            hideSeekBar2();
                        } else {
                            if (Double.parseDouble(response.amtShow) > 0) {
                                tvTopText.setText(R.string.top_text_status_4);
                                btnHome.setEnabled(true);
                            } else {
                                hideSeekBar2();
                                tvTopText.setText(R.string.top_text_status_4_no_amount);
                                btnHome.setEnabled(false);
                            }
                        }
                        if (!"0".equals(response.loan_number)) {//第一次可借款时，无弹框。
                            mPresenter.getCouponInfo(false);
                        }
                        break;
                    case "2"://审核中
                        showHomeView(VIEW_BORROW);
                        mPresenter.getOrderDetails();
                        tvTopText.setText(R.string.top_text_check);
                        btnHome.setText(R.string.btn_text_shenhe);
                        break;
                    case "6"://放款中
                        showHomeView(VIEW_BORROW);
                        mPresenter.getOrderDetails();
                        tvTopText.setText(R.string.top_text_fangkuan);
                        btnHome.setText(R.string.text_refresh);
                        break;
                    case "5"://未逾期 待还款
                        showHomeView(VIEW_PAY_AT_TIME);
                        setPayLayoutView();
                        setPayCommonView();
                        mPresenter.getCouponInfo(false);
                        break;
                    case "8"://已逾期
                        showHomeView(VIEW_PAY_AT_TIME);
                        setOverdueLayoutView();
                        setPayCommonView();
                        SPUtils.putInt(ConstantValue.IS_SELECT_COUPON, 0);
                        tvCoupon.setText("Kupon x0");
                        tvFinalPayAmount.setText(formatIndMoney(mHomeData.repayAmt));
                        break;
                    case "7"://还款中
                        showHomeView(VIEW_PAY_AT_TIME);
                        break;
                }
            }
        } else if (BaseResponseBean.RES_CODE_LOGIN_OUT_OF_DATE.equals(response.res_code)) {//重新登陆
            showToast(getString(R.string.toast_text_relogin));
            LogoutUtil.logout();
            IntentUtils.startLoginActivity(mContext);
        } else {
            showHomeView(VIEW_SEEK_BAR);
            showToast(response.res_msg);
        }
    }


    /**
     * 检查是否是新订单
     */
    private void checkIsNewOrderId(HomeDataResBean response) {
        if (!TextUtils.isEmpty(response.order_Id)) {
            if (!response.order_Id.equals(SPUtils.getString(ConstantValue.LAST_ORDER_ID, ""))) {
                SPUtils.putString(ConstantValue.LAST_ORDER_ID, response.order_Id);
                SPUtils.putBoolean(ConstantValue.OPERATION_NORMAL_DIALOG_HAS_SHOWED, false);
                SPUtils.putBoolean(ConstantValue.COUPON_DIALOG_HAS_SHOWED, false);
                SPUtils.putInt(ConstantValue.IS_SELECT_COUPON, 1);
            }
        }
    }

    private void setBorrowPageBaseInfo() {
        showHomeView(VIEW_BORROW);
        showApplyInfoLayout();
        mPresenter.getBorrowApplyInfo();
    }

    private void hideSeekBar2() {
        rlSeekBar2.setVisibility(View.INVISIBLE);
        mSbIndicatorAmount2.setText("Rp.0");
    }

    /**
     * 设置滑条数值范围
     */
    private void setSeekBarValue() {
        if (!TextUtils.isEmpty(mHomeData.maxAmtRange)) {
            if (isOpenGodMode && (BaseApplication.sPhoneNum != null && BaseApplication.sPhoneNum.contains("81287566687")) || "3832085".equals(BaseApplication.mUserId)) {//晶晶
                mHomeData.maxAmtRange = "90000";
//                mSelectAmount = 20000;
                tvMinAmt.setText("Rp.20,000");
            }
            int maxAmountRange = Integer.parseInt(mHomeData.maxAmtRange);
            mSeekBarAmount2.setMax(maxAmountRange);
            if (mSelectAmount > maxAmountRange) {
                mSelectAmount = maxAmountRange;
            }
            mSeekBarAmount2.setProgress(mSelectAmount);
            mSbIndicatorAmount2.setText("Rp." + formatNumber(mSelectAmount));//500RMB
            tvMaxAmt.setText(formatIndMoney(mHomeData.maxAmtRange));
        }
    }

    private void setApplyTabVisible(String type) {
        if ("1".equals(type) || "3".equals(type)) {
            ((MainActivity) getActivity()).showApplyTab();
        } else {
            ((MainActivity) getActivity()).hideApplyTab();
        }
    }

    private void setPayCommonView() {
        mPresenter.getPayWayList();//回调 setPayWayData

        extendEntryVisibleControl();
        partPayVisibleControl();
        if (isExtendPageVisible()) {
            btnHome.setText(R.string.btn_text_go_delay_pay);//延长到期日
        } else {
            btnHome.setText(R.string.btn_text_go_pay);//去还款    openWebViewActivity
        }
    }

    private boolean isExtendPageVisible() {
        return View.VISIBLE == extendPage.getVisibility();
    }

    /**
     * 展期入口显示隐藏控制
     */
    private void extendEntryVisibleControl() {
        if (View.VISIBLE == includePayAtTime.getVisibility()//正常还款页（或逾期页）
                && "1".equals(mHomeData.popEntrance)) {
            llDelayPayEntry.setVisibility(View.VISIBLE);
        } else {
            llDelayPayEntry.setVisibility(View.GONE);
        }
    }

    /**
     * 部分还款显示隐藏控制
     */
    private void partPayVisibleControl() {
        if ("1".equals(mHomeData.freeServiceFee)) {//已减免，不再展示部分还款入口
            llPartPayEntry.setVisibility(View.INVISIBLE);
        } else {
            llPartPayEntry.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理首页点击 #bbb
     */
    private void handleButtonClick() {
        if (BaseApplication.sLoginState) {
            if (!TextUtils.isEmpty(mHomeData.type)) {
                switch (mHomeData.type) {
                    case "1":
                    default:
                        IntentUtils.gotoMainActivity(mContext, TAB_APPLY);
                        break;
                    case "3"://认证成功
                        // 检查顺序：银行卡-》活体-》信息确认
                        if (sHasGetBankCardList && sHasGetAuthStatusList) {
                            if (!sHasBankCard) {
                                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_BANK_CARD_LIST(
                                        sFormatSelectAmount, sFormatSelectTime, sFormatSelectInterest));
                            } else if (!sHasFacePassed) {
                                startActivity(StartLivenessActivity.createIntent(mContext));
                            } else {
                                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_INFO_CONFIRM());
                            }
                        } else {
                            mRefreshLayout.autoRefresh(0);
                            new Handler().postDelayed(() -> btnHome.performClick(), 1000);
                        }

                        break;
                    case "4"://可借款
                        if (isRefuse) {
                            showRefuseDialog();
                        } else {
                            if (Double.parseDouble(mHomeData.amtShow) > 0) {
                                if (applyInfoPage.getVisibility() == View.VISIBLE) {
                                    //申请信息页面显示时，点击按钮，触发弹框
                                    mPresenter.getBorrowApplyInfo(mSelectAmount, mSelectType);
                                } else if (loanInfoPage.getVisibility() == View.VISIBLE) {
                                    //Info Pinjaman 页面显示时，点击按钮，刷新页面
                                    mRefreshLayout.autoRefresh(100);
                                }
                            } else {
                                showToast(getResources().getString(R.string.top_text_status_4_no_amount));
                            }
                        }
                        break;
                    case "2"://审核中
                    case "9"://额度计算中
                    case "6"://放款中
                        mRefreshLayout.autoRefresh(100);
                        break;
                    case "5"://未逾期 待还款
                    case "8"://已逾期
                        if (isExtendPageVisible()) {
                            showPayWayDialog(ConstantValue.EXTEND_PAY, mPayWayList);
                        } else {
                            showPayWayDialog(ConstantValue.NORMAL_PAY, mPayWayList);
                        }
                        break;
                    case "11"://防止重复借款
                        showRefuseDialog();
                        break;
                }
            } else {
                showToast("No type found");
            }
        } else {
            IntentUtils.startLoginActivityForResult(getActivity(), MainActivity.REQUEST_HOME_FRAGMENT_LOGIN);
        }

    }

    private void showRefuseDialog() {
        DialogFactory.createOneButtonCommonDialog(mContext, "Prompt",
                getResources().getString(R.string.text_review_refused),
                "OK", (dialog, view) -> {
                    dialog.dismiss();
                }).show();
    }

    private void showPayWayDialog(String from, String[] payWayList) {
        if (payWayList != null && payWayList.length > 0) {
            DialogFactory.payWaySelectMaterialDialog(mContext, getResources().getString(R.string.text_refresh_and_try_again), payWayList, from, "").show();
        } else {
            showToast(getResources().getString(R.string.toast_text_refresh));
        }
    }

    /**
     * 设置还款页面数据
     */
    private void setPayLayoutView() {
        String finalDate = mHomeData.finalRepaymentDate;
        int diffTime = 0;
        if (!TextUtils.isEmpty(finalDate)) {
            try {
                diffTime = MyTimeUtils.daysBetween(MyTimeUtils.getCurrentData(), finalDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.removeRule(0);
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlParams.setMargins(0, UIUtils.dip2px(40), 0, 0);
        tvLeftDay.setLayoutParams(rlParams);
        tvLeftDay.setTextSize(54f);
        tvTimeText.setVisibility(View.VISIBLE);
        tvBelowHint.setVisibility(View.VISIBLE);
        tvLeftDay.setText(diffTime + "");
        tvTotalBorrowAmount.setText(formatIndMoney(mHomeData.repayAmt));//待还金额
        tvPayDeadline.setText(mHomeData.finalRepaymentDate);//最后还款日期

    }

    /**
     * 设置逾期页面数据
     */
    private void setOverdueLayoutView() {
        tvLeftDay.setText(getString(R.string.home_text_has_delay) + "\n" + mHomeData.overdueDay + "\n" + getString(R.string.day));
        tvTotalBorrowAmount.setText(formatIndMoney(mHomeData.repayAmt));//待还金额
        tvPayDeadline.setText(mHomeData.finalRepaymentDate);//最后还款日期

        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvLeftDay.setGravity(CENTER);
        tvLeftDay.setLayoutParams(rlParams);
        tvTimeText.setVisibility(View.GONE);
        tvBelowHint.setVisibility(View.GONE);
        tvLeftDay.setTextSize(22f);
    }

    private void showHomeView(String contentView) {
        mCurrentView = contentView;
        includeSeekBar.setVisibility(View.GONE);
        includeBorrow.setVisibility(View.GONE);
        includePayAtTime.setVisibility(View.GONE);
        extendPage.setVisibility(View.GONE);
        mRefreshLayout.setEnableRefresh(true);
        btnTitleBarBack.setVisibility(View.GONE);
        llBorrowProtocol.setVisibility(View.INVISIBLE);

        if (VIEW_SEEK_BAR.equals(contentView)) {
            includeSeekBar.setVisibility(View.VISIBLE);
        } else if (VIEW_BORROW.equals(contentView)) {
            includeBorrow.setVisibility(View.VISIBLE);
            llBorrowProtocol.setVisibility(View.INVISIBLE);//暂时隐藏
        } else if (VIEW_PAY_AT_TIME.equals(contentView)) {
            includePayAtTime.setVisibility(View.VISIBLE);
        } else if (VIEW_PAY_EXTENT.equals(contentView)) {
            extendPage.setVisibility(View.VISIBLE);
            mRefreshLayout.setEnableRefresh(false);
            btnTitleBarBack.setVisibility(View.VISIBLE);
        }
        if (isGooglePlayChannel()) {
            tvBorrowTimeRangeTint.setVisibility(View.VISIBLE);
            tvBorrowTimeRangeTint.setText("91-180 Hari");
        } else {
            tvBorrowTimeRangeTint.setVisibility(View.INVISIBLE);
//            tvBorrowTimeRangeTint.setText("7-9 Hari");
        }
    }

    private void showSubmitSuccessDialog(String manageFee, String serviceFee) {
        String manage_fee = formatIndMoney(manageFee);
        String service_fee = formatIndMoney(serviceFee);
        String dialogMsg = MessageFormat.format("Catatan:\n" +
                "Jumlah pinjaman akan secara otomatis dikurangi oleh biaya manajemen ({0}) dan biaya administrasi ({1}) sebelum dicairkan ke akun bank Anda.", manage_fee, service_fee);
        DialogFactory.createCommonDialog(mContext, getString(R.string.text_submit_success), dialogMsg, getString(R.string.text_cancel), new BaseDialog.OnClickListener() {
            @Override
            public void onClick(BaseDialog dialog, View view) {
                dialog.dismiss();
            }
        }, getString(R.string.text_ok), new BaseDialog.OnClickListener() {
            @Override
            public void onClick(BaseDialog dialog, View view) {
                mPresenter.requestPermissions();
                dialog.dismiss();
            }
        }).show();
    }

    private String formatIndMoney(String fee) {
        if (!TextUtils.isEmpty(fee)) {
            String originFee = fee;
            if (fee.contains(".")) {
                String[] split = fee.split("\\.");
                originFee = split[0];
            }
            return String.format("Rp. %s", formatNumber(Integer.parseInt(originFee)));
        } else {
            return String.format("Rp. %s", "0");
        }
    }

    /**
     * 刷新首页
     */
    @Override
    public void stopRefresh() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void refreshHomeData() {
        mRefreshLayout.autoRefresh(100);
    }

    //展示借款信息
    @Override
    public void setBorrowInfo(BorrowApplyInfoResBean data) {
        showApplyInfoLayout();
        isRefuse = false;
        // 赋值(apply和loan一起赋值)
        tvApplyInfoName.setText(BaseApplication.sUserName);
        if (!TextUtils.isEmpty(mHomeData.amtShow) && Double.parseDouble(mHomeData.amtShow) > 0) {
            tvApplyInfoAmount.setText(formatIndMoney(data.loanAmt));
        } else {
            tvApplyInfoAmount.setText(formatIndMoney("0"));
        }
        if ("1".equals(data.new_old_sign)) {
            tvApplyInfoDue.setText("7 hari");// 新户7天
            mSelectType = 1;
        } else if ("2".equals(data.new_old_sign)) {//旧户
            tvApplyInfoDue.setText("9 hari´");// 旧户9天
            mSelectType = 3;
        }
        if (isNotGooglePlayChannel()) {
            ivLoanTimeQuestion.setVisibility(View.INVISIBLE);
        } else {
            ivLoanTimeQuestion.setVisibility(View.VISIBLE);
            ivLoanTimeQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFactory.createNoticeDialog(mContext, "Maaf, berdasarkan informasi Anda, kami saat ini hanya dapat memberi Anda pinjaman 9 hari.").show();
                }
            });
        }

        tvApplyInfoBankName.setText(data.card_bank_name);
        tvApplyInfoBankCardNumber.setText(data.bank_card_no);
    }

    //展示订单信息
    @Override
    public void setOrderInfo(BorrowApplyInfoResBean data) {
        showLoanInfoLayout();

        tvLoanInfoBankCardNumber.setText(data.bank_card_no);
        tvLoanInfoBankName.setText(data.card_bank_name);
        tvLoanInfoInterest.setText(formatIndMoney(data.baseRate));
        tvLoanInfoKtp.setText(data.ktp);
        tvLoanInfoPhoneNum.setText(data.phone);
        tvLoanInfoDue.setText(data.loanDays + " hari");
        tvLoanInfoLoanAmount.setText(formatIndMoney(data.loanAmt));
        tvLoanInfoTotalGetAmount.setText(formatIndMoney(data.actAmt));
    }

    @Override
    public void setSubmitDialogData(BorrowApplyInfoResBean borrowData) {
        showSubmitSuccessDialog(borrowData.adminFee, borrowData.serviceFee);
    }


    /**
     * 设置展期页面数据
     *
     * @param data 展期接口数据
     */
    @Override
    public void showExtendPageData(GetExtendFeeResBean data) {
        tvDelayTime.setText(data.extendDays);
        tvPayDeadlineDelay.setText(MyTimeUtils.timeStamp2Date(data.shouldReturnTime));
        tvDelayInterest.setText(String.format("Rp.%s", formatNumber(Integer.parseInt(data.extendFee))));
    }

    @Override
    public void showLoanInfoLayout() {
        applyInfoPage.setVisibility(View.GONE);
        loanInfoPage.setVisibility(View.VISIBLE);
    }

    private void showApplyInfoLayout() {
        applyInfoPage.setVisibility(View.VISIBLE);
        rlSeekBar2.setVisibility(View.VISIBLE);
        loanInfoPage.setVisibility(View.GONE);
    }

    @Override
    public void setPayWayData(GetPayWayListResBean data) {
        if (data.repay_type_list != null) {
            int size = data.repay_type_list.size();
            mPayWayList = new String[size];

            for (int i = 0; i < size; i++) {
                if (data.repay_type_list.get(i) != null) {
                    mPayWayList[i] = data.repay_type_list.get(i).type;
                }
            }
        }
    }

    @Override
    public void setRefuseState() {
        isRefuse = true;
        tvTopText.setText(R.string.top_text_status_4_no_amount);
        tvApplyInfoName.setText("-");
        tvApplyInfoAmount.setText("-");
        tvApplyInfoBankName.setText("-");
        tvApplyInfoBankCardNumber.setText("-");
    }

    @Override
    public void handleCouponInfo(CouponResBean data) {
        mCouponData = data;
        mOriginPayAmount = formatIndMoney(mHomeData.repayAmt);
        if (!TextUtils.isEmpty(mHomeData.repayAmt) && !TextUtils.isEmpty(data.couponCutAmount)) {
            mAfterCutAmount = formatNumber((int) (Double.parseDouble(mHomeData.repayAmt) - Double.parseDouble(data.couponCutAmount)));
        }
        if ("4".equals(mHomeData.type) && !SPUtils.getBoolean(ConstantValue.OPERATION_NORMAL_DIALOG_HAS_SHOWED, false)) {
            SPUtils.putBoolean(ConstantValue.OPERATION_NORMAL_DIALOG_HAS_SHOWED, true);
            DialogFactory.createNoticeDialog(mContext, "Jumlah pinjaman maksimum mencapai \nRp." + mHomeData.maxAmtRange).show();
        } else if ("5".equals(mHomeData.type) && !SPUtils.getBoolean(ConstantValue.COUPON_DIALOG_HAS_SHOWED, false) && "1".equals(data.couponStatus)) {
            SPUtils.putBoolean(ConstantValue.COUPON_DIALOG_HAS_SHOWED, true);
            DialogFactory.createCouponDialog(mContext, data).show();
        }

        if (View.VISIBLE == includeBorrow.getVisibility()) {
            if ("0".equals(data.couponStatus)) {//未激活
                rlCouponBorrow.setVisibility(View.VISIBLE);
                tvCouponBorrow.setText("Kupon x1");
            } else {
                rlCouponBorrow.setVisibility(View.GONE);
            }
        } else {
            if ("1".equals(data.couponStatus)) {//1 优惠券已激活
                if (0 == SPUtils.getInt(ConstantValue.IS_SELECT_COUPON, 1)) {//未选择优惠券
                    tvCoupon.setText("Kupon x1");
                    tvFinalPayAmount.setText(mOriginPayAmount);
                } else {
                    tvCoupon.setText("- Rp." + data.couponCutAmount);
                    tvFinalPayAmount.setText("Rp." + mAfterCutAmount);
                }
            } else if ("0".equals(data.couponStatus) || "2".equals(data.couponStatus) || "8".equals(mHomeData.type)) {//0 未激活  2已使用
                tvCoupon.setText("Kupon x0");
                tvFinalPayAmount.setText(mOriginPayAmount);
            }
        }
    }

    @Override
    public void noCoupon(CouponResBean data) {
        mCouponData = data;
        tvCoupon.setText("Kupon x0");
        tvCouponBorrow.setText("Kupon x0");
        tvFinalPayAmount.setText(formatIndMoney(mHomeData.repayAmt));
    }

    private String formatNumber(int selectInterest) {
        return CurrencyFormatUtils.formatDecimal(String.valueOf(selectInterest));
    }

    /**
     * 选择借款金额
     */
    private SeekBar.OnSeekBarChangeListener seekBarAmountListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSbIndicatorAmount.setText(formatNumber(progress));
            mSelectAmount = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //监听用户开始拖动进度条的时候
            mRefreshLayout.setEnableRefresh(false);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //监听用户结束拖动进度条的时候
            mRefreshLayout.setEnableRefresh(true);
            //300,000  400,000  600,000  800,000  1000,000  1200,000  1500,000
            int currentProgress = seekBar.getProgress();
            if (0 <= currentProgress && currentProgress < 350000) {
                mSelectAmount = 300000;
            } else if (350000 <= currentProgress && currentProgress < 500000) {
                mSelectAmount = 400000;
            } else if (500000 <= currentProgress && currentProgress < 700000) {
                mSelectAmount = 600000;
            } else if (700000 <= currentProgress && currentProgress < 900000) {
                mSelectAmount = 800000;
            } else if (900000 <= currentProgress && currentProgress < 1100000) {
                mSelectAmount = 1000000;
            } else if (1100000 <= currentProgress && currentProgress < 1350000) {
                mSelectAmount = 1200000;
            } else if (1350000 <= currentProgress && currentProgress < 1550000) {
                mSelectAmount = 1500000;
            } else if (1550000 <= currentProgress && currentProgress < 1650000) {
                mSelectAmount = 1600000;
            } else if (1650000 <= currentProgress && currentProgress < 1950000) {
                mSelectAmount = 1800000;
            } else if (1950000 <= currentProgress) {
                mSelectAmount = 2000000;
            }
            seekBar.setProgress(mSelectAmount);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarAmountListener2 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSbIndicatorAmount2.setText("Rp." + formatNumber(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //监听用户开始拖动进度条的时候
            mRefreshLayout.setEnableRefresh(false);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar2) {
            //监听用户结束拖动进度条的时候
            mRefreshLayout.setEnableRefresh(true);
            //300,000  400,000  600,000  800,000  1000,000  1200,000  1500,000   1600,000  1800,000  2000,000
            int currentProgress = seekBar2.getProgress();

            if (isOpenGodMode && (BaseApplication.sPhoneNum != null && BaseApplication.sPhoneNum.contains("81287566687")) || "3832085".equals(BaseApplication.mUserId)) {//晶晶
                //20000  40000  60000
                if (0 <= currentProgress && currentProgress < 30000) {
                    mSelectAmount = 20000;
                } else if (30000 <= currentProgress && currentProgress < 50000) {
                    mSelectAmount = 40000;
                } else if (50000 <= currentProgress && currentProgress < 70000) {
                    mSelectAmount = 60000;
                } else if (70000 <= currentProgress && currentProgress < 90000) {
                    mSelectAmount = 80000;
                } else if (90000 == currentProgress) {
                    mSelectAmount = 90000;
                }
            } else {
                if (0 <= currentProgress && currentProgress < 350000) {
                    mSelectAmount = 300000;
                } else if (350000 <= currentProgress && currentProgress < 500000) {
                    mSelectAmount = 400000;
                } else if (500000 <= currentProgress && currentProgress < 700000) {
                    mSelectAmount = 600000;
                } else if (700000 <= currentProgress && currentProgress < 900000) {
                    mSelectAmount = 800000;
                } else if (900000 <= currentProgress && currentProgress < 1100000) {
                    mSelectAmount = 1000000;
                } else if (1100000 <= currentProgress && currentProgress < 1350000) {
                    mSelectAmount = 1200000;
                } else if (1350000 <= currentProgress && currentProgress < 1550000) {
                    mSelectAmount = 1500000;
                } else if (1550000 <= currentProgress && currentProgress < 1650000) {
                    mSelectAmount = 1600000;
                } else if (1650000 <= currentProgress && currentProgress < 1950000) {
                    mSelectAmount = 1800000;
                } else if (1950000 <= currentProgress) {
                    mSelectAmount = 2000000;
                }
            }
            seekBar2.setProgress(mSelectAmount);
            mSbIndicatorAmount2.setText("Rp." + formatNumber(mSelectAmount));
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showMsgDot(NewMsgEvent event) {
        if (sHasNewUnreadMsg) {
            msgRedDot.setVisibility(View.VISIBLE);
        } else {
            msgRedDot.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackPressed(BackPressEvent event) {
        if (VIEW_PAY_EXTENT.equals(mCurrentView)) {
            showHomeView(VIEW_PAY_AT_TIME);//当前已赋值,无需设置数据
            if (mHomeData != null) {
                if ("5".equals(mHomeData.type) || "8".equals(mHomeData.type)) {//逾期
                    llDelayPayEntry.setVisibility(View.VISIBLE);
                    partPayVisibleControl();
                }
            }
            btnHome.setText(R.string.btn_text_go_pay);//去还款
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * 检查还款日是否是红日
     */
    public boolean checkRepayDayIsHoliday() {

        return false;
    }
}
