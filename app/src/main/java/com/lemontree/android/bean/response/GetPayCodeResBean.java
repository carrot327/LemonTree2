package com.lemontree.android.bean.response;


import com.lemontree.android.base.BaseResponseBean;

public class GetPayCodeResBean extends BaseResponseBean {
    public String tobeAmount;//原始金额
    public String amount;//当前待还金额（一定有值）
    public String pay_code;//还款码

}
