package com.kantong.android.bean.response;

import com.kantong.android.base.BaseResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewCustomerProductResBean extends BaseResponseBean {


    /**
     * title : 纯信用手机借款，2分钟极速借款
     * productList : [{"id":9,"no":"30004","typeNo":3,"sortNo":2,"productLogoUrl":"/mark/30004.png","productUrl":"http://suo.im/5mMcrU","productName":"周享贷","mainTitle":"10000","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"纯信用手机借款，2分钟极速借款","createDate":1533633322000,"lastUpdateDate":1533633322000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":1},{"id":16,"no":"30011","typeNo":3,"sortNo":12,"productLogoUrl":"/mark/30011.png","productUrl":"https://api.txzcqb.com/pop/weixin/12345678901/ddtta8","productName":"亿周转","mainTitle":"30000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"极速秒下款，大额等你拿","createDate":1538045364000,"lastUpdateDate":1538045364000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":1},{"id":27,"no":"30001","typeNo":3,"sortNo":1,"productLogoUrl":"/mark/30001.jpg","productUrl":"https://www.mixiaohuahua.com/pro/loanRegister.html?resource=llcs-zq","productName":"米小花","mainTitle":"30000","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"口子多，下款款","createDate":1544092304000,"lastUpdateDate":1544092304000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":11,"no":"30006","typeNo":3,"sortNo":10,"productLogoUrl":"/mark/30006.png","productUrl":"http://suo.im/4T2o41","productName":"随意现金","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"只要身份证，就能借到钱,不打公司电话，不打亲人电话","createDate":1537338817000,"lastUpdateDate":1537338817000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":15,"no":"30010","typeNo":3,"sortNo":13,"productLogoUrl":"/mark/30010.png","productUrl":"https://at.umeng.com/b4rmia","productName":"贷你飞","mainTitle":"20000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"全平台最快放款","createDate":1538045364000,"lastUpdateDate":1538045364000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":4,"no":"30016","typeNo":3,"sortNo":15,"productLogoUrl":"/mark/30016.png","productUrl":"http://suo.im/5mGcuM","productName":"老哥帮","mainTitle":"30000","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"口子多，下款款","createDate":1533021615000,"lastUpdateDate":1533021615000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":18,"no":"30013","typeNo":3,"sortNo":15,"productLogoUrl":"/mark/30013.png","productUrl":"https://ggad.51wwjf.com/register/channel?channel_id=21&theme=index","productName":"机机熊","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"18秒拿2000元.","createDate":1538987606000,"lastUpdateDate":1538987606000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":12,"no":"30007","typeNo":3,"sortNo":16,"productLogoUrl":"/mark/30007.png","productUrl":"http://www.80houkeji.com/?id=1431","productName":"租租回收","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"线上自动审核,高效便捷的手机回租平台","createDate":1538045360000,"lastUpdateDate":1538045360000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":14,"no":"30009","typeNo":3,"sortNo":17,"productLogoUrl":"/mark/30009.png","productUrl":"http://zdjk.wdxianjin.cn/index.html?channel=cps01","productName":"子弹借款","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"自动审批，实测通过率>85%、信用好、利息低，提额上不封顶","createDate":1538045364000,"lastUpdateDate":1538045364000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null},{"id":21,"no":"30015","typeNo":3,"sortNo":17,"productLogoUrl":"/mark/30015.png","productUrl":"http://xhj.vacuumlid.com/api/comm/register?agentId=510257430","productName":"小花椒","mainTitle":"5000元","secondTitle":"最高可借额度（元）","buttonTitle":"立即借贷","productNote":"芝麻600分急速放款！","createDate":1539227064000,"lastUpdateDate":1539227064000,"monthRate":null,"rangeLimit":null,"loanSpeed":null,"applyAmount":null,"applyCondition":null,"ifRecommend":null}]
     */

    public String title;
    public List<ProductListBean> productList;

    public static List<NewCustomerProductResBean> arrayNewCustomerProductResBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<NewCustomerProductResBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }


    public static class ProductListBean implements Serializable {
        /**
         * id : 9
         * no : 30004
         * typeNo : 3
         * sortNo : 2
         * productLogoUrl : /mark/30004.png
         * productUrl : http://suo.im/5mMcrU
         * productName : 周享贷
         * mainTitle : 10000
         * secondTitle : 最高可借额度（元）
         * buttonTitle : 立即借贷
         * productNote : 纯信用手机借款，2分钟极速借款
         * createDate : 1533633322000
         * lastUpdateDate : 1533633322000
         * monthRate : null
         * rangeLimit : null
         * loanSpeed : null
         * applyAmount : null
         * applyCondition : null
         * ifRecommend : 1
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
        public String rangeLimit;//额度范围
        public String timeLimit;//借款期限
        public String loanSpeed;
        public String applyAmount;//申请人数
        public String applyCondition;
        public int ifRecommend;

        public static List<ProductListBean> arrayProductListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ProductListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }
    }
}
