package com.lemontree.android.bean.request;

import java.util.List;

public class ContactInfoReqBean extends CommonReqBean {

    public List<ContactBean> relationship;

    public class ContactBean {
        public String relation_name;
        public String relation_type;
        public String relation_phone;
    }
}
