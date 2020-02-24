package com.sm.android.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sm.android.R;
import com.sm.android.bean.response.NewCustomerProductResBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.ui.activity.MainActivity;
import com.sm.android.ui.activity.ProductDetailActivity;
import com.sm.android.utils.IntentUtils;

import java.io.Serializable;
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
        if (1 == itemBean.ifRecommend) {
            holder.ivRecommend.setVisibility(View.VISIBLE);
        } else {
            holder.ivRecommend.setVisibility(View.INVISIBLE);
        }
        holder.productName.setText(itemBean.productName);
        holder.amountRange.setText(itemBean.rangeLimit);
        holder.speed.setText(itemBean.loanSpeed);
        holder.rate.setText("日费率" + itemBean.monthRate);
        holder.tvBtn.setText("Pinjam");

        holder.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.PRODUCT_DATA, itemBean);
                mContext.startActivity(intent);
            }
        });
        holder.tvBtn.setOnClickListener(new View.OnClickListener() {
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
        RelativeLayout rvItem;
        ImageView ivIcon;
        ImageView ivRecommend;
        TextView productName;
        TextView amountRange;
        TextView speed;
        TextView rate;
        TextView tvBtn;

        MyViewHolder(View itemView) {
            super(itemView);
            rvItem = itemView.findViewById(R.id.rv_item);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            ivRecommend = itemView.findViewById(R.id.iv_tag_matched);
            productName = itemView.findViewById(R.id.tv_product_name);
            amountRange = itemView.findViewById(R.id.tv_product_amount);
            speed = itemView.findViewById(R.id.tv_speed);
            rate = itemView.findViewById(R.id.tv_month_rate);
            tvBtn = itemView.findViewById(R.id.tv_btn);
        }
    }
}
