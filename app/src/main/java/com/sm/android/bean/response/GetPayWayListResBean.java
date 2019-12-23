package com.sm.android.bean.response;

import com.sm.android.base.BaseResponseBean;

import java.util.List;

public class GetPayWayListResBean extends BaseResponseBean {


    /**
     * user_id : 3772755
     * app_version : 0.0.1
     * app_name : h5
     * app_clientid : h5
     * order_amount : 1
     * should_return_amount : 1
     * load_days : 7 天
     * repay_type_list : [{"id":"376","card_bank_name":"中信银行","bank_card_no":"6217*********2248","type":"1"}]
     * jumpThridPayList : [{"id":"1002","card_bank_name":"支付宝","bank_card_no":"123","type":"2"}]
     */

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public String order_amount;
    public String should_return_amount;
    public String load_days;
    public List<RepayTypeListBean> repay_type_list;//还款类型：1 ALFAMART；2 ATM; 3 手机银行
    public List<JumpThridPayListBean> jumpThridPayList;

    public static class RepayTypeListBean {
        /**
         * id : 376
         * card_bank_name : 中信银行
         * bank_card_no : 6217*********2248
         * type : 1
         */

        public String id;
        public String card_bank_name;
        public String bank_card_no;
        public String type;////还款类型：1 ALFAMART；2 ATM; 3 手机银行
    }

    public static class JumpThridPayListBean {
        /**
         * id : 1002
         * card_bank_name : 支付宝
         * bank_card_no : 123
         * type : 2
         */

        public String id;
        public String card_bank_name;
        public String bank_card_no;
        public String type;


    }
}
