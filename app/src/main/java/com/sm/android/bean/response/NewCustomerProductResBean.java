package com.sm.android.bean.response;

import com.sm.android.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

public class NewCustomerProductResBean extends BaseResponseBean {
    public List<ProductListBean> data;

    public static class ProductListBean implements Serializable {
        /**
         * id : 1
         * no : 30001
         * typeNo : 3
         * sortNo : 1
         * productLogoUrl : https://yn-sakumanis-ocr.oss-ap-southeast-1.aliyuncs.com/logo_ek.png
         * productUrl : http://161.117.188.77:8083/?app_clientid=sakumanis_1202&v=216-1
         * productName : Easy Kredit
         * mainTitle : Rp.20000000
         * secondTitle : aaaa
         * buttonTitle : bbbb
         * productNote : cccc
         * createDate : 1581945984000
         * lastUpdateDate : 1581945986000
         * monthRate : null
         * rangeLimit : null
         * loanSpeed : null
         * applyAmount : null
         * applyCondition : null
         * ifRecommend : null
         * timeLimit : null
         */

        public int id;
        public String no;
        public int typeNo;
        public int sortNo;
        public String productLogoUrl;
        public String productUrl;
        public String productName;
        public String mainTitle;
        public String secondTitle;
        public String buttonTitle;
        public String productNote;
        public long createDate;
        public long lastUpdateDate;
        public String monthRate;
        public String rangeLimit;
        public String loanSpeed;
        public String applyAmount;
        public String applyCondition;
        public int ifRecommend;
        public String timeLimit;
    }
}
