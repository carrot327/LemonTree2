package com.update.updatesdk;

import java.io.Serializable;

public class UpdateResBean implements Serializable {
    public static final String SUCCESS = "0000";
    public static final String RES_CODE_FAILED = "1000";
    public static final String RES_CODE_FORCE_UPDATE = "1001";
    public static final String RES_CODE_SUGGEST_UPDATE = "1002";
    public String res_code;
    public String res_msg;
    public String install_url;
}
