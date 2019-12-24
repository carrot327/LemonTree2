package com.cocotree.android.bean.response;

import com.cocotree.android.base.BaseRequestBean;

/**
 * Created by wayne on 15/7/13.
 */
public class WebViewBean extends BaseRequestBean {

    private String id;
    private String title;
    private String page;
    private String value;
    private String pageTag;
    private String productId;
    private String needSessionId;
    private String type;
    private String url;
    public Data data;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPageTag() {
        return pageTag;
    }

    public void setPageTag(String pageTag) {
        this.pageTag = pageTag;
    }

    public String getNeedSessionId() {
        return needSessionId;
    }

    public void setNeedSessionId(String needSessionId) {
        this.needSessionId = needSessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public String getPage() {
        return page;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   /* public String toJson() {
        return GsonUtils.beanTojson(this);
    }*/

    @Override
    public String toString() {
        return "WebViewBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", page='" + page + '\'' +
                ", value='" + value + '\'' +
                ", pageTag='" + pageTag + '\'' +
                ", productId='" + productId + '\'' +
                ", needSessionId='" + needSessionId + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data extends BaseRequestBean {

        public String cb_id;
        public String share_title;
        public String share_desc;
        public String share_url;
        public String share_icon;
        /**
         * 分享类型 1:普通分享 2:二维码 3:图片分享 4:二维码弹窗
         */
        public int share_type;
        /**
         * 脱敏手机号
         */
        public String safe_mobile;
        public String share_sms;
        public String name;
        public String code;
        public String tab;
        public String link;
        public String buy_num;
        public String fp_id;
        public String fp_title;
        /**
         * 0:失败 1:成功 2:取消
         */
        public String state;
        /**
         * 表示目前流程
         * 'bindCard':绑卡
         * 'setPwd': 设置密码
         * 'withdraw': 提现
         * 'autoInvest': 自动投标
         * 'autoDebt':自动债转
         * 'delectCard': 删除银行卡
         * 'resetMobile': 修改手机号
         * 'pay': 投资购买
         * 'compensatory':代偿绑卡
         */
        public String step;
        /**
         * app在URL内appDefined传过来的值
         */
        public String appDefined;
        /**
         * 1:精选; 6:月月升
         */
        public String fp_type;
        public String tel;
        public String url;
        public boolean needLogin;
        /**
         * 键值
         */
        public String key;
        /**
         * 是否显示原生返回按钮 true显示 false不显示
         */
        public boolean show;
        /**
         * true:允许原生滚动，false：禁止原生滚动
         */
        public boolean nativeScroll;

        // 麦子统计相关字段
        public String log_time;
        public String start_time;
        public String end_time;
        public String action_type;
        public String page_id;
        public String page_info;
        public String page_url;
        public String view_path;

        public String investAmount;

        public int type;//续投设置完成后的通知。  0-刷新返回   1-刷新

        public String getCbId() {
            return cb_id;
        }

        public void setCbId(String cb_id) {
            this.cb_id = cb_id;
        }

        public String getShareTitle() {
            return share_title;
        }

        public void setShareTitle(String share_title) {
            this.share_title = share_title;
        }

        public String getShareDesc() {
            return share_desc;
        }

        public void setShareDesc(String share_desc) {
            this.share_desc = share_desc;
        }

        public String getShareUrl() {
            return share_url;
        }

        public void setShareUrl(String share_url) {
            this.share_url = share_url;
        }

        public String getShareIcon() {
            return share_icon;
        }

        public void setShareIcon(String share_icon) {
            this.share_icon = share_icon;
        }

        public int getShareType() {
            return share_type;
        }

        public void setShareType(int share_type) {
            this.share_type = share_type;
        }

        public String getShareSms() {
            return share_sms;
        }

        public void setShareSms(String share_sms) {
            this.share_sms = share_sms;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getBuyNum() {
            return buy_num;
        }

        public void setBuyNum(String buy_num) {
            this.buy_num = buy_num;
        }

        public String getFpId() {
            return fp_id;
        }

        public void setFpId(String fp_id) {
            this.fp_id = fp_id;
        }

        public String getFp_title() {
            return fp_title;
        }

        public void setFp_title(String fp_title) {
            this.fp_title = fp_title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getFpType() {
            return fp_type;
        }

        public void setFpType(String fp_type) {
            this.fp_type = fp_type;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public boolean isNeedLogin() {
            return needLogin;
        }

        public void setNeedLogin(boolean needLogin) {
            this.needLogin = needLogin;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "cb_id='" + cb_id + '\'' +
                    ", share_title='" + share_title + '\'' +
                    ", share_desc='" + share_desc + '\'' +
                    ", share_url='" + share_url + '\'' +
                    ", share_icon='" + share_icon + '\'' +
                    ", share_type=" + share_type +
                    ", share_sms='" + share_sms + '\'' +
                    ", name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    ", tab='" + tab + '\'' +
                    ", link='" + link + '\'' +
                    ", buy_num='" + buy_num + '\'' +
                    ", fp_id='" + fp_id + '\'' +
                    ", fp_title='" + fp_title + '\'' +
                    ", state='" + state + '\'' +
                    ", step='" + step + '\'' +
                    ", appDefined='" + appDefined + '\'' +
                    ", fp_type='" + fp_type + '\'' +
                    ", tel='" + tel + '\'' +
                    ", url='" + url + '\'' +
                    ", needLogin=" + needLogin +
                    ", key='" + key + '\'' +
                    ", show=" + show +
                    ", nativeScroll=" + nativeScroll +
                    ", log_time='" + log_time + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", action_type='" + action_type + '\'' +
                    ", page_id='" + page_id + '\'' +
                    ", page_info='" + page_info + '\'' +
                    ", page_url='" + page_url + '\'' +
                    ", view_path='" + view_path + '\'' +
                    '}';
        }

        public static class PageInfo extends BaseRequestBean {

            public String title;
            public String category;
            public String product_id;
            public String pos;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getPos() {
                return pos;
            }

            public void setPos(String pos) {
                this.pos = pos;
            }

            @Override
            public String toString() {
                return "PageInfo{" +
                        "title='" + title + '\'' +
                        ", category='" + category + '\'' +
                        ", product_id='" + product_id + '\'' +
                        ", pos='" + pos + '\'' +
                        '}';
            }
        }
    }
}
