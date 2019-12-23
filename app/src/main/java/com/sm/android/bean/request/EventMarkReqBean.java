package com.sm.android.bean.request;

import com.sm.android.base.BaseRequestBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.uploadUtil.Tools;

public class EventMarkReqBean extends BaseRequestBean {
    public String appversion = Tools.getAppVersion();
    public String loanchannel = "android";
    public String ext1 = Tools.getChannel();//暂时用ext字段传输channel
    public String customerid = BaseApplication.mUserId;
    public String phone = BaseApplication.sPhoneNum;
    public String eventid;
    public String eventtime;
    public String deviceid;
}
