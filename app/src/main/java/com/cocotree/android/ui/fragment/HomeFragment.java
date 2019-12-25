package com.cocotree.android.ui.fragment;

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

import com.cocotree.android.BuildConfig;
import com.cocotree.android.R;
import com.cocotree.android.base.BaseDialog;
import com.cocotree.android.base.BaseFragment;
import com.cocotree.android.base.BaseResponseBean;
import com.cocotree.android.bean.enventbus.BackPressEvent;
import com.cocotree.android.bean.enventbus.NewMsgEvent;
import com.cocotree.android.bean.response.BorrowApplyInfoResBean;
import com.cocotree.android.bean.response.GetPayWayListResBean;
import com.cocotree.android.bean.response.HomeDataResBean;
import com.cocotree.android.iview.IHomeView;
import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.manager.ConstantValue;
import com.cocotree.android.manager.DialogFactory;
import com.cocotree.android.presenter.HomePresenter;
import com.cocotree.android.ui.activity.MainActivity;
import com.cocotree.android.ui.activity.PartPayActivity;
import com.cocotree.android.ui.activity.ProtocolBorrowActivity;
import com.cocotree.android.ui.activity.StartLivenessActivity;
import com.cocotree.android.uploadUtil.UrlHostConfig;
import com.cocotree.android.utils.CurrencyFormatUtils;
import com.cocotree.android.utils.IntentUtils;
import com.cocotree.android.utils.LogoutUtil;
import com.cocotree.android.utils.MyTimeUtils;
import com.cocotree.android.utils.UIUtils;
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
import static com.cocotree.android.ui.activity.MainActivity.TAB_APPLY;
import static com.cocotree.android.ui.activity.MainActivity.sFormatSelectAmount;
import static com.cocotree.android.ui.activity.MainActivity.sFormatSelectInterest;
import static com.cocotree.android.ui.activity.MainActivity.sFormatSelectTime;
import static com.cocotree.android.ui.activity.MainActivity.sHasBankCard;
import static com.cocotree.android.ui.activity.MainActivity.sHasFacePassed;
import static com.cocotree.android.ui.activity.MainActivity.sHasGetAuthStatusList;
import static com.cocotree.android.ui.activity.MainActivity.sHasGetBankCardList;
import static com.cocotree.android.ui.activity.MainActivity.sHasNewUnreadMsg;

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
    @BindView(R.id.tv_select_time)
    TextView mSbIndicatorTime;
    @BindView(R.id.seek_bar_want_time)
    SeekBar mSeekBarTime;
    @BindView(R.id.tv_apply_info_name)
    TextView tvApplyInfoName;
    @BindView(R.id.tv_delay_time)
    TextView tvDelayTime;
    @BindView(R.id.tv_apply_info_due)
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
    @BindView(R.id.tv_total_borrow_amount_delay)
    TextView tvTotalBorrowAmountDelay;
    @BindView(R.id.tv_pay_deadline_delay)
    TextView tvPayDeadlineDelay;
    @BindView(R.id.tv_top_text)
    TextView tvTopText;
    @BindView(R.id.tv_min_amt)
    TextView tvMinAmt;
    @BindView(R.id.tv_max_amt)
    TextView tvMaxAmt;
    @BindView(R.id.tv_min_time)
    TextView tvMinTime;
    @BindView(R.id.tv_max_time)
    TextView tvMaxTime;
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


    private static final String VIEW_SEEK_BAR = "viewSeekBar";//home_layout_seek_bar
    private static final String VIEW_BORROW = "viewBorrow";//home_layout_borrow
    private static final String VIEW_PAY_AT_TIME = "viewPayAtTime";//home_layout_pay
    private static final String VIEW_PAY_EXTENT = "viewPayDelay";//home_layout_delay_pay

    private static final int EXTENSION_DAYS = 7;//展期天数

    private String DEFAULT_SHOW_VIEW = VIEW_SEEK_BAR;

    public static int mSelectAmount = 1000000;
    public static int mSelectTime = 7;
    public static int mSelectType = 1;//1为7天   2为14天
    private HomeDataResBean mHomeData = new HomeDataResBean();
    private String[] mPayWayList;
    private String mCurrentView;
    private boolean isRefuse;

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
        mSeekBarTime.setProgress(mSelectTime);
        mSeekBarAmount.setOnSeekBarChangeListener(seekBarAmountListener);
        mSeekBarAmount2.setOnSeekBarChangeListener(seekBarAmountListener2);
        mSeekBarTime.setOnSeekBarChangeListener(seekBarTimeListener);

        mSbIndicatorAmount.setText(formatNumber(400000));
        mSbIndicatorTime.setText("7 hari");
        tvPartPayEntry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvDelayPayEntry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnHome.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (BuildConfig.DEBUG) {
//                showSubmitSuccessDialog();
//                showPayWayDialog(ConstantValue.NORMAL_PAY);

//                startActivity(StartLivenessActivity.createIntent(mContext));

//                IntentUtils.openWebViewActivity(mContext, "//url");

//                mPresenter.requestPermissions();

//                IntentUtils.openWebViewActivity(mContext, UrlHostConfig.H5_CONTACT());
                }
                return true;
            }
        });
    }

    private boolean isNeedShowApplyPage() {
        return "9".equals(mHomeData.type) || "4".equals(mHomeData.type) || "2".equals(mHomeData.type) || "6".equals(mHomeData.type);
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

    @OnClick({R.id.iv_titlebar_right, R.id.iv_home_back, R.id.btn_home, R.id.ll_delay_pay_entry, R.id.tv_borrow_protocol, R.id.ll_part_pay})
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
        }
    }

    /**
     * 设置首页数据 #aaa
     */
    public void setHomeData(HomeDataResBean response) {
        mHomeData = response;
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
                        showHomeView(VIEW_BORROW);
                        showApplyInfoLayout();
                        mPresenter.getBorrowApplyInfo();
                        tvTopText.setText(getResources().getText(R.string.top_text_status_9));
                        break;
                    case "4"://可借款
                        showHomeView(VIEW_BORROW);
                        showApplyInfoLayout();
                        setSeekBarValue();
                        mPresenter.getBorrowApplyInfo();
                        if (View.VISIBLE == applyInfoPage.getVisibility()) {
                            btnHome.setText(R.string.btn_text_confirm);//点击方法 showSubmitSuccessDialog
                        }

                        if (!isRefuse) {
                            if (Double.parseDouble(response.amtShow) > 0) {
                                tvTopText.setText(R.string.top_text_status_4);
                                btnHome.setEnabled(true);
                            } else {
                                tvTopText.setText(R.string.top_text_status_4_no_amount);
                                btnHome.setEnabled(false);
                            }
                        } else {
                            tvTopText.setText(R.string.text_review_refused);
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
                        break;
                    case "8"://已逾期
                        showHomeView(VIEW_PAY_AT_TIME);
                        setOverdueLayoutView();
                        setPayCommonView();
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
     * 设置滑条数值范围
     */
    private void setSeekBarValue() {
        //设置金额
        if (!TextUtils.isEmpty(mHomeData.maxAmtRange)) {
            if ((BaseApplication.sPhoneNum != null && BaseApplication.sPhoneNum.contains("81287566687")) || "3832085".equals(BaseApplication.mUserId)) {//晶晶
                mHomeData.maxAmtRange = "60000";
                mSelectAmount = 20000;
                tvMinAmt.setText("Rp.20,000");
            }
            mSeekBarAmount2.setMax(Integer.parseInt(mHomeData.maxAmtRange));
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

//            tvApplyInfoDue.setText(data.loanDays + " hari");// 默认为7天。
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

    @Override
    public void showExtendPageData(String extentFee) {
        // TODO: 2019-12-09 展期固定天数（后期应通过接口调用）
        int extendDay = EXTENSION_DAYS;
        if (mHomeData.overdueDay != null) {
            extendDay = EXTENSION_DAYS + Integer.parseInt(mHomeData.overdueDay);
        }
        tvDelayTime.setText(extendDay + "");
        if (mHomeData.repayAmt == null) {
            mHomeData.repayAmt = "0";
        }
        if (!TextUtils.isEmpty(mHomeData.repayAmt)) {
            tvTotalBorrowAmountDelay.setText(formatIndMoney(Integer.parseInt(mHomeData.repayAmt) + Integer.parseInt(extentFee) + ""));
        }
        String nextPayDate = "";
        try {
            if (mHomeData.finalRepaymentDate != null) {
                nextPayDate = MyTimeUtils.plusDay(extendDay, mHomeData.finalRepaymentDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvPayDeadlineDelay.setText(nextPayDate);
        //设置展期费用
        tvDelayInterest.setText(String.format("Rp.%s", formatNumber(Integer.parseInt(extentFee))));
    }

    @Override
    public void showLoanInfoLayout() {
        applyInfoPage.setVisibility(View.GONE);
        loanInfoPage.setVisibility(View.VISIBLE);
    }

    private void showApplyInfoLayout() {
        applyInfoPage.setVisibility(View.VISIBLE);
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
        tvTopText.setText(R.string.text_review_refused);

        tvApplyInfoName.setText("-");
        tvApplyInfoAmount.setText("-");
        tvApplyInfoBankName.setText("-");
        tvApplyInfoBankCardNumber.setText("-");
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
            //200,000  400,000  800,000  1000,000  1200,000  1500,000
            int currentProgress = seekBar.getProgress();
            if (0 <= currentProgress && currentProgress < 300000) {
                mSelectAmount = 200000;
            } else if (300000 <= currentProgress && currentProgress < 600000) {
                mSelectAmount = 400000;
            } else if (600000 <= currentProgress && currentProgress < 900000) {
                mSelectAmount = 800000;
            } else if (900000 <= currentProgress && currentProgress < 1100000) {
                mSelectAmount = 1000000;
            } else if (1100000 <= currentProgress && currentProgress < 1350000) {
                mSelectAmount = 1200000;
            } else if (1350000 <= currentProgress && currentProgress < 1550000) {
                mSelectAmount = 1500000;
            } else if (1550000 <= currentProgress) {
                mSelectAmount = 1600000;
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
            //300,000  400,000  600,000  800,000  1000,000  1200,000  1500,000
            int currentProgress = seekBar2.getProgress();

            if ((BaseApplication.sPhoneNum != null && BaseApplication.sPhoneNum.contains("81287566687")) || "3832085".equals(BaseApplication.mUserId)) {//晶晶
                //20000  40000  60000
                if (0 <= currentProgress && currentProgress < 30000) {
                    mSelectAmount = 20000;
                } else if (30000 <= currentProgress && currentProgress < 50000) {
                    mSelectAmount = 40000;
                } else if (50000 <= currentProgress && currentProgress < 70000) {
                    mSelectAmount = 60000;
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
                } else if (1350000 <= currentProgress) {
                    mSelectAmount = 1500000;
                }
            }
            seekBar2.setProgress(mSelectAmount);
            mSbIndicatorAmount2.setText("Rp." + formatNumber(mSelectAmount));
        }
    };

    /**
     * 选择借款期限
     */
    private SeekBar.OnSeekBarChangeListener seekBarTimeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSbIndicatorTime.setText(progress + " hari");
            mSelectTime = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mRefreshLayout.setEnableRefresh(false);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mRefreshLayout.setEnableRefresh(true);
            //3  5  7  10  14
            int currentProgress = seekBar.getProgress();
            if (0 <= currentProgress && currentProgress < 4) {
                mSelectTime = 3;
            } else if (4 <= currentProgress && currentProgress < 6) {
                mSelectTime = 5;
            } else if (6 <= currentProgress && currentProgress < 8) {
                mSelectTime = 7;
            } else if (8 <= currentProgress && currentProgress < 12) {
                mSelectTime = 10;
            } else if (12 <= currentProgress && currentProgress <= 14) {
                mSelectTime = 14;
            }
            seekBar.setProgress(mSelectTime);
            mSbIndicatorTime.setText(mSelectTime + " hari");
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

}
