package com.kantong.android.bean.response;

import com.kantong.android.base.BaseResponseBean;

public class GetUserTypeState extends BaseResponseBean {

    public DataBean data;
    public String count;


    public static class DataBean {
        /**
         * type : 1
         * userId : 3832074
         * userName : james band
         */

        public int userState;
        public String userId;
        public String userName;

    }
}
