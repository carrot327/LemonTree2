package com.cocotreedebug.android.bean.request;

import com.cocotreedebug.android.base.BaseRequestBean;

public class BankcardListReqBean extends BaseRequestBean {
    public String user_id;
    public String type;//查询类型：0 全部；1 借记卡；2 其他（除借记卡）
    public String app_version;
    public String app_name;
    public String app_clientid;

}
