package com.cocotreedebug.android.bean.request;

import com.cocotreedebug.android.base.BaseRequestBean;
import com.cocotreedebug.android.manager.BaseApplication;
import com.cocotreedebug.android.uploadUtil.Tools;

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
