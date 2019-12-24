package com.cocotree.android.bean.response;

import com.cocotree.android.base.BaseResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetBannerListResBean extends BaseResponseBean {


    /**
     * user_id : sd
     * app_version : 1.0
     * app_name : ios
     * app_clientid : appstore_iOS
     * bannerInfoList : [{"img_url":"http://file.baitiaoloan.com/banner/ios_banner1.png","product_name":"null","sort_no":1,"jump_url":"null"},{"img_url":"http://file.baitiaoloan.com/banner/ios_banner2.png","product_name":"null","sort_no":2,"jump_url":"null"}]
     */

    public String user_id;
    public String app_version;
    public String app_name;
    public String app_clientid;
    public List<BannerInfoListBean> bannerInfoList;

    public static List<GetBannerListResBean> arrayGetBannerListResBean2FromData(String str) {

        Type listType = new TypeToken<ArrayList<GetBannerListResBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static class BannerInfoListBean {
        /**
         * img_url : http://file.baitiaoloan.com/banner/ios_banner1.png
         * product_name : null
         * sort_no : 1
         * jump_url : null
         */

        public String img_url;
        public String product_name;
        public int sort_no;
        public long id;//新增一个id，用于埋点
        public String jump_url;

        public static List<BannerInfoListBean> arrayBannerInfoListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<BannerInfoListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }
    }
}
