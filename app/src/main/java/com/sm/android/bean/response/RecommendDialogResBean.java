package com.sm.android.bean.response;

import com.sm.android.base.BaseResponseBean;

import java.util.List;

public class RecommendDialogResBean extends BaseResponseBean {


    /**
     * status : 1
     * productList : [{"id":11,"no":"30006","typeNo":5,"sortNo":10,"productLogoUrl":"/mark/30006.png","productUrl":"http://suo.im/4T2o41","productName":"随意现金","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"只要身份证，就能借到钱,不打公司电话，不打亲人电话","createDate":1537338817000,"lastUpdateDate":1537338817000,"monthRate":"2%","rangeLimit":null,"loanSpeed":"3分钟","applyAmount":null,"applyCondition":null,"ifRecommend":null,"timeLimit":null}]
     */

    public String status;
    public List<ProductListBean> productList;


    public static class ProductListBean {
        /**
         * id : 11
         * no : 30006
         * typeNo : 5
         * sortNo : 10
         * productLogoUrl : /mark/30006.png
         * productUrl : http://suo.im/4T2o41
         * productName : 随意现金
         * mainTitle : 5000元
         * secondTitle : 最高可借额度（元）
         * buttonTitle : 立即借贷
         * productNote : 只要身份证，就能借到钱,不打公司电话，不打亲人电话
         * createDate : 1537338817000
         * lastUpdateDate : 1537338817000
         * monthRate : 2%
         * rangeLimit : null
         * loanSpeed : 3分钟
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
        public String ifRecommend;
        public String timeLimit;
    }
}
