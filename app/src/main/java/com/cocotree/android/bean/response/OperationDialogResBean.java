package com.cocotree.android.bean.response;

import com.cocotree.android.base.BaseResponseBean;

public class OperationDialogResBean extends BaseResponseBean {
    public String app_version;
    public String app_name;
    public String app_clientid;
    public String mobile;
    public String hz;//展示频率( 0.不展示    1.只展示一次  2.每次启动展示  3.每天展示一次   4.无数据)
    public String content;
    public String image_url;//首页弹框背景图片
    public String redirect_url;
}
