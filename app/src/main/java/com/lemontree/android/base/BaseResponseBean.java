package com.lemontree.android.base;


import java.io.Serializable;

public class BaseResponseBean implements Serializable {
    public static final String SUCCESS = "0000";
    public static final String RES_CODE_FAILED = "1000";
    public static final String RES_CODE_LOGIN_OUT_OF_DATE = "4000";
    public static final String RES_CODE_FORCE_UPDATE = "1001";
    public static final String RES_CODE_SUGGEST_UPDATE = "1002";
    public String res_code;
    public String res_msg;
}
