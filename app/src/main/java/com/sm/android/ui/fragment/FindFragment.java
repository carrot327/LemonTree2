package com.sm.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sm.android.R;
import com.sm.android.base.BaseFragment;
import com.sm.android.base.BaseResponseBean;
import com.sm.android.bean.response.NewCustomerProductResBean;
import com.sm.android.manager.NetConstantValue;
import com.sm.android.network.OKHttpClientEngine;
import com.sm.android.ui.adapter.LoanMarketRvAdapter;
import com.sm.android.uploadUtil.UrlHostConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FindFragment extends BaseFragment {
    @BindView(R.id.rv_loan_market)
    RecyclerView rvDaoliu;
    @BindView(R.id.refresh_layout_loan_market)
    SmartRefreshLayout refreshLayoutLoanMarket;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initializeView(View view) {
        // 关闭懒加载
        enableLazyLoad(false);
        refreshLayoutLoanMarket.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getDaoliuProduct();
            }
        });
    }

    @Override
    protected void loadData(boolean hasRequestData) {
        getDaoliuProduct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /*
     *导流商家产品获取
     */
    /*private void getDaoliuProduct() {
        String url = UrlHostConfig.getFlowIOHost() + NetConstantValue.NET_REQUEST_URL_FLOWIO_GET_CUSTOMERPRODUCT;
        StringBuilder wholeUrl = new StringBuilder();
        wholeUrl.append(url + "?")
                .append("app_clientid=" + Tools.getChannel())
                .append("&app_version=" + Tools.getAppVersion())
                .append("&app_name=android")
                .append("&mobile=" + BaseApplication.sPhoneNum);
        Log.d("karl", "wholeUrl:" + wholeUrl);

        final Request request = new Request.Builder()
                .url(wholeUrl.toString())
                .get()
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                refreshLayoutLoanMarket.finishRefresh(200);
                if (response != null && response.isSuccessful()) {
                    //解析数据
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(response.body().string()).getAsJsonArray();
                    Gson gson = new Gson();
                    List<NewCustomerProductResBean.ProductListBean> customerProductList = new ArrayList<>();
                    for (JsonElement noticeResponse : jsonArray) {
                        NewCustomerProductResBean.ProductListBean customerProductResBean = gson.fromJson(noticeResponse, NewCustomerProductResBean.ProductListBean.class);
                        customerProductList.add(customerProductResBean);
                    }

                    //setAdapter
                    LoanMarketRvAdapter adapter = new LoanMarketRvAdapter(mContext, customerProductList);

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvDaoliu.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }*/

    /*
     *导流商家产品获取
     */
    private void getDaoliuProduct() {
        NetworkLiteHelper
                .get()
                .url(UrlHostConfig.appendUrlWithParamsOnly(UrlHostConfig.getFlowIOHost() + NetConstantValue.NET_REQUEST_URL_FLOWIO_GET_CUSTOMERPRODUCT))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<NewCustomerProductResBean>() {
                    @Override
                    public void onSuccess(Call call, NewCustomerProductResBean response, int id) {
                        refreshLayoutLoanMarket.finishRefresh(200);
                        if (response != null) {
                            if (BaseResponseBean.SUCCESS.equals(response.res_code)) {
                                if (response.data != null && response.data.size() > 0) {
                                    LoanMarketRvAdapter adapter = new LoanMarketRvAdapter(mContext, response.data);
                                    rvDaoliu.setLayoutManager(new LinearLayoutManager(mContext));
                                    rvDaoliu.setAdapter(adapter);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });

    }
}
