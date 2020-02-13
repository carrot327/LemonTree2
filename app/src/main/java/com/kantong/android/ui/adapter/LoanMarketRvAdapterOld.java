package com.kantong.android.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 发现页超市
 */
public class LoanMarketRvAdapterOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

  /*  private List<CustomerProductResBean> mList;
    private Context mContext;

    public LoanMarketRvAdapterOld(Context context, List<CustomerProductResBean> mList) {
        this.mList = mList;
        this.mContext = context;
    }

    *//**
     * 创建ViewHolder
     *
     * @param viewGroup
     * @param i
     * @return
     *//*
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan_market_list, viewGroup, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    *//**
     * 通过ViewHolder对item中的控件进行控制（如：显示数据等等）
     *
     * @param viewHolder
     * @param i
     *//*
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        CustomerProductResBean itemBean = mList.get(i);
        Glide.with(mContext).
                load(itemBean.productLogoUrl).
                into(holder.ivIcon);
        holder.productName.setText(itemBean.productName);
        holder.productDesc.setText(itemBean.secondTitle);
        holder.productAmount.setText(itemBean.mainTitle);
        holder.productNote.setText(itemBean.productNote);
        holder.tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openWebViewActivity(mContext, itemBean.productUrl);
                markCustomerProduct(itemBean.id);
            }
        });
    }

    *//**
     * 返回列表长度
     *
     * @return
     *//*
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    *//**
     * 创建ViewHolder类，用来缓存item中的子控件，避免不必要的findViewById
     *//*
    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView productName;
        TextView productDesc;
        TextView productAmount;
        TextView productNote;
        TextView tvBtn;

        MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            productName = itemView.findViewById(R.id.tv_product_name);
            productDesc = itemView.findViewById(R.id.tv_product_desc);
            productAmount = itemView.findViewById(R.id.tv_product_amount);
            productNote = itemView.findViewById(R.id.tv_product_note);
            tvBtn = itemView.findViewById(R.id.tv_btn);
        }
    }

    *//*
     * mark导流商家产品获取
     *//*
    private void markCustomerProduct(int productId) {
        String url = UrlHostConfig.getFlowIOHost() + NetConstantValue.NET_REQUEST_URL_FLOWIO_MARK_CUSTOMERPRODUCT;
        StringBuilder wholeUrl = new StringBuilder();
        wholeUrl.append(url + "?")
                .append("app_clientid=" + Tools.getChannel())
                .append("&app_version=" + Tools.getAppVersion())
                .append("&app_name=android")
                .append("&id=" + productId)
                .append("&mobile=" + BaseApplication.sPhoneNum);
        Log.d("karl", "wholeUrl:" + wholeUrl);

        final Request request = new Request.Builder()
                .url(wholeUrl.toString())
                .get()
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }*/
}
