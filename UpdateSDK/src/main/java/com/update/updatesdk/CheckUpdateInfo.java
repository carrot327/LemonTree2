package com.update.updatesdk;

import java.io.Serializable;

/**
 * Created by karl on 2017/10/31.
 */

public class CheckUpdateInfo implements Serializable {

    public boolean succeed;
    public String errorCode;
    public String errorMessage;
    public VersionInfo data;

    public class VersionInfo implements Serializable{
        public int upgrade;
        public String title;
        public String desc;
        public String link;
    }
}
