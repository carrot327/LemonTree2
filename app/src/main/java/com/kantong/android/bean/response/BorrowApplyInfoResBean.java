package com.kantong.android.bean.response;


import com.kantong.android.base.BaseResponseBean;

import java.util.List;

public class BorrowApplyInfoResBean extends BaseResponseBean {

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
    public String new_old_sign;//是否是旧户    1新户   2旧户
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
