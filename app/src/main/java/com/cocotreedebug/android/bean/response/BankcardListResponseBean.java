package com.cocotreedebug.android.bean.response;

import com.cocotreedebug.android.base.BaseResponseBean;

import java.util.List;

public class BankcardListResponseBean extends BaseResponseBean {

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public List<BankCardList> bankCardList;

    public class BankCardList {
        public String card_bank_name;
        public String bank_card_no;
        public String id;
        public String type;
    }
}
