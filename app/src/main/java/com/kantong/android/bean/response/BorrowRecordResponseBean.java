package com.kantong.android.bean.response;

import com.kantong.android.base.BaseResponseBean;

import java.util.List;

public class BorrowRecordResponseBean extends BaseResponseBean {

    public String app_version;
    public String app_name;
    public String app_clientid;
    public List<BorrowRecordListBean> appPayOrderRes;

    public class BorrowRecordListBean {
        public String applicationTime;
        public String loanAmt;
        public String order_id;
        public String status;
    }
}
