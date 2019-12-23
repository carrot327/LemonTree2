package com.sm.android.bean.request;

public class GoBorrowReqBean extends CommonReqBean {
    public long customer_bank_card_id;
    public int loan_amount;
    public int borrow_type;//期限 1:7天,2:14天, 3:9天,4:15天,5:20天,6:5天
}
