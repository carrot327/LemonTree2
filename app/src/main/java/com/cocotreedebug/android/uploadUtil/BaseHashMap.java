package com.cocotreedebug.android.uploadUtil;

import java.util.HashMap;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/20 14:07
 * 文件描述:
 */
public class BaseHashMap extends HashMap{

    public BaseHashMap(){
        super();
        this.put("app_version",  Tools.getAppVersion());
        this.put("app_name",  Tools.getAppName());
        this.put("app_clientid",  Tools.getChannel());
    }
}
