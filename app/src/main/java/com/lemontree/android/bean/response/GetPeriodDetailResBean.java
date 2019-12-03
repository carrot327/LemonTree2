package com.lemontree.android.bean.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemontree.android.base.BaseResponseBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetPeriodDetailResBean extends BaseResponseBean {
    /**
     * user_id : null
     * app_version : null
     * app_name : null
     * app_clientid : null
     * list : [{"period":"1","status":"2","amount":"1000"},{"period":"2","status":"2","amount":"2000"},{"period":"3","status":"2","amount":"1"},{"period":"4","status":"0","amount":"2000"},{"period":"5","status":"0","amount":"2000"}]
     */

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public List<ListBean> list;

    public static List<GetPeriodDetailResBean> arrayGetPeriodDetailResBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<GetPeriodDetailResBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class ListBean {
        /**
         * period : 1
         * status : 2
         * amount : 1000
         */

        public String period;
        public String status;
        public String amount;

        public static List<ListBean> arrayListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

    }
    //参考GetBannerListResBean
}
