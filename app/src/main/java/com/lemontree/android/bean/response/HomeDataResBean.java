package com.lemontree.android.bean.response;

import com.lemontree.android.base.BaseResponseBean;

public class HomeDataResBean extends BaseResponseBean {
    public String type;
    public String user_id;
    public String order_Id;
    public String finalRepaymentDate;
    public String Date;
    public String amtShow;//eg. "8000.00"
    public String repayAmt;
    public String repayAllAmt;
    public String overdueDay;
    public String repay_switch;
    public String accessAmt;//下期可借金额
    public int is_diversion;//是否导流  1导流
    public String find_page_url;//是否导流  1导流
    public String amt_type;//1 单期   2 分期
    public String loanTerm;
    public String loan_day;
    public String freeServiceFee;
    public String repaymentTerm;
    public String minAmtRange;//可选择金额范围
    public String maxAmtRange;//可选择金额范围
    public String maxLoanTime;//可选择时间范围
    public String postpone;//剩余申请延期次数
    public String popEntrance;//是否展示，1 弹 0 不弹
}
