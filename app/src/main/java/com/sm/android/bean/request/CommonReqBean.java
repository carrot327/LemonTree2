package com.sm.android.bean.request;

import com.sm.android.base.BaseRequestBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.uploadUtil.Tools;

public class CommonReqBean extends BaseRequestBean {
    public String app_version = Tools.getAppVersion();
    public String app_name = Tools.getAppName();
    public String app_clientid = Tools.getChannel();
    public String user_id = BaseApplication.mUserId;
    public String phone = BaseApplication.sPhoneNum;

}
