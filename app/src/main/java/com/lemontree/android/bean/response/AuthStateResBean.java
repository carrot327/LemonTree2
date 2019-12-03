package com.lemontree.android.bean.response;

import com.lemontree.android.base.BaseResponseBean;

import java.util.List;

public class AuthStateResBean extends BaseResponseBean {


    /**
     * user_id : 3832003
     * app_version : 1.0
     * app_name : android
     * app_clientid : web
     * authStatusList : [{"baseStatus":1},{"companyStatus":1},{"relationStatus":1},{"ocrStatus":1}]
     * hasAmt : 2
     * isWhiteList : 1
     * isWhiteAmtList : 1
     * faceStatus : 0
     */

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public int hasAmt;
    public int isWhiteList;
    public int isWhiteAmtList;
    public String faceStatus;
    public List<AuthStatusListBean> authStatusList;


    public static class AuthStatusListBean {
        /**
         * baseStatus : 1
         * companyStatus : 1
         * relationStatus : 1
         * ocrStatus : 1
         */

        public int baseStatus;
        public int companyStatus;
        public int relationStatus;
        public int ocrStatus;

    }
}
