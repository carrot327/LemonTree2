package com.sm.android.bean.response;

import com.sm.android.base.BaseResponseBean;

public class HomeDataResBean extends BaseResponseBean {
    public String type;
    public String user_id;
    public String order_Id;
    public String finalRepaymentDate;
    public String Date;
    public String amtShow;//eg. "8000.00"
    public String repayAmt;//待还金额
    public String repayAllAmt;
    public String overdueDay;
    public String repay_switch;
    public String accessAmt;//下期可借金额
    public int is_diversion;//是否导流  1导流
    public String find_page_url;//是否导流  1导流
    public String amt_type;//1 单期   2 分期
    public String loanTerm;
    public String loan_day;
    public String repaymentTerm;
    public String minAmtRange;//可选择金额范围
    public String maxAmtRange;//可选择金额范围
    public String maxLoanTime;//可选择时间范围
    public String postpone;//剩余申请延期次数
    public String loan_number;//已借款次数
    public String freeServiceFee;//部分还款入口是否展示
    public String popEntrance;//展期是否展示，1 弹 0 不弹
    public int showRecommendProduct;//是否展示推荐页   1展示  0不展示
}
