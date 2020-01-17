package com.lemontree.android.bean.response;

import com.lemontree.android.base.BaseResponseBean;

public class CouponResBean extends BaseResponseBean {
    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public String amt_status;//是否可以提高额度
    public String premium_rate;//优惠百分比
    public String active_time;//优惠券到期时间
    public String loan_number;//贷款成功次数
    public String couponCutAmount;//使用券时被减掉的金额
    public String couponStatus;//0 未激活   1已激活   2已使用
}
