package com.cocotree.android.bean.response;


import com.cocotree.android.base.BaseResponseBean;

import java.util.List;

public class BorrowApplyInfoResBean extends BaseResponseBean {


    /**
     * user_id : 100001
     * app_version : 0.0.1
     * app_name : android
     * app_clientid : xiaomi
     * loanAmt : 1200
     * loanDays : 7
     * baseRate : 240
     * actAmt : 960
     * card_bank_code : 100
     * card_bank_name : 招商银行
     * timeLimit : [{"limit_day":"1 期（6 天）","status":"1"},{"limit_day":"2 期（27 天）","status":"0"},{"limit_day":"3 期（36 天）","status":"1"}]
     * customer_bank_card_id : 1
     * bank_card_no : 1
     */

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public String loanAmt;
    public String loanDays;
    public String baseRate;//baseRate的值等于adminFee+serviceFee
    public String actAmt;
    public String card_bank_code;
    public String card_bank_name;
    public int customer_bank_card_id;
    public String bank_card_no;
    public String phone;
    public String ktp;
    public String serviceFee;//服务费
    public String adminFee;//管理费
    public List<TimeLimitBean> timeLimit;

    public static class TimeLimitBean {
        /**
         * limit_day : 1 期（6 天）
         * status : 1
         */

        public String limit_day;
        public String status;

    }
}
