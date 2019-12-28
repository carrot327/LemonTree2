package com.cocotreedebug.android.bean.request;

import com.cocotreedebug.android.base.BaseRequestBean;
import com.cocotreedebug.android.manager.BaseApplication;
import com.cocotreedebug.android.uploadUtil.Tools;

public class CommonReqBean extends BaseRequestBean {
    public String app_version = Tools.getAppVersion();
    public String app_name = Tools.getAppName();
    public String app_clientid = Tools.getChannel();
    public String user_id = BaseApplication.mUserId;
    public String phone = BaseApplication.sPhoneNum;

}
