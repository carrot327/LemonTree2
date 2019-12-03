package com.lemontree.android.bean.request;

import com.lemontree.android.base.BaseRequestBean;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.uploadUtil.Tools;

public class CommonReqBean extends BaseRequestBean {
    public String app_version = Tools.getAppVersion();
    public String app_name = Tools.getAppName();
    public String app_clientid = Tools.getChannel();
    public String user_id = BaseApplication.mUserId;
    public String phone = BaseApplication.sPhoneNum;

}
