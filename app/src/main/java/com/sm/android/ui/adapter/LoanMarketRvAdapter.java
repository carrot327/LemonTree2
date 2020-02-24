package com.sm.android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sm.android.R;
import com.sm.android.bean.response.NewCustomerProductResBean;
import com.sm.android.ui.activity.ProductDetailActivity;
import com.sm.android.utils.StringFormatUtils;

import java.util.List;

/**
 * 发现页超市
 */
public class LoanMarketRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewCustomerProductResBean.ProductListBean> mList;
    private Context mContext;

    public LoanMarketRvAdapter(Context context, List<NewCustomerProductResBean.ProductListBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan_market_list, viewGroup, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    /**
     * 通过ViewHolder对item中的控件进行控制（如：显示数据等等）
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        NewCustomerProductResBean.ProductListBean itemBean = mList.get(i);
        Glide.with(mContext)
                .load(itemBean.productLogoUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.ivIcon);
        holder.productName.setText(itemBean.productName);
        holder.amountRange.setText(StringFormatUtils.formatIndMoney(itemBean.rangeLimit));
        holder.rate.setText(itemBean.monthRate + "%/hari");//eg. 0.05%/hari
//        holder.reviewSpeed.setText(itemBean.loanSpeed + "hari");//直接写为1天

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.PRODUCT_DATA, itemBean);
                mContext.startActivity(intent);
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.PRODUCT_DATA, itemBean);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 返回列表长度
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        ImageView ivIcon;
        TextView productName;
        TextView amountRange;
        TextView reviewSpeed;
        TextView rate;
        Button btn;

        MyViewHolder(View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.rv_item);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            productName = itemView.findViewById(R.id.tv_product_name);
            amountRange = itemView.findViewById(R.id.tv_product_amount);
            reviewSpeed = itemView.findViewById(R.id.tv_product_review_speed);
            rate = itemView.findViewById(R.id.tv_product_interest);
            btn = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
