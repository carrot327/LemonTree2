package com.cocotreedebug.android.bean.response;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cocotreedebug.android.base.BaseResponseBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetOrderDetailsResBean extends BaseResponseBean {

    /**
     * load_days : 7
     * return_time : 2018-03-19
     * interest : 234
     * applicationTime : 2018-03-19
     * loanAmt : 50000
     * status : 6
     * order_id : 1223
     * app_version : 1.1
     * app_name : aaa
     * app_clientid : android
     */

    public int load_days;
    public String return_time;
    public String interest;
    public String applicationTime;
    public String loanAmt;
    public String status;
    public String order_id;
    public String app_version;
    public String app_name;
    public String app_clientid;

    public static List<GetOrderDetailsResBean> arrayGetOrderDetailsResBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<GetOrderDetailsResBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

}
