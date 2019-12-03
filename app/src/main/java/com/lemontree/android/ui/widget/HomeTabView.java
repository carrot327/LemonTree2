package com.lemontree.android.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemontree.android.R;
import com.lemontree.android.bean.TabResourceBean;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

@SuppressLint("ViewConstructor")
public class HomeTabView extends CommonPagerTitleView {

    private ImageView mTabIcon;
    private TextView mTvTitle;
    private TextView mTvMsgCount;
    private int mHomeTabIndex = -1;
    private boolean mSelected = false;
    private TabResourceBean mTabRes;
    private OnTabChangedListener mOnTabChangedListener;

    public void setOnTabChangedListener(OnTabChangedListener onTabChangedListener) {
        this.mOnTabChangedListener = onTabChangedListener;
    }

    public interface OnTabChangedListener {
        void onChanged(int index);
    }

    public HomeTabView(Context context, int homeTabIndex, TabResourceBean tabRes) {
        super(context);
        initView(homeTabIndex, tabRes);
    }

    private void initView(int homeTabIndex, TabResourceBean tabRes) {
        View tabContentView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_bottom_tab, null);
        mTabIcon = tabContentView.findViewById(R.id.lav_tab_icon);
        mTvTitle = tabContentView.findViewById(R.id.tv_home_tab_title);
        mTvMsgCount = tabContentView.findViewById(R.id.tv_home_tab_msg_count);

        mTabRes = tabRes;
        mTvTitle.setText(mTabRes.tabText[homeTabIndex]);
        initTabIcon(homeTabIndex);
        setContentView(tabContentView);
    }

    /**
     * 初始化首页各个tab的icon资源
     */
    private void initTabIcon(int homeTabIndex) {
        mHomeTabIndex = homeTabIndex;
        if (homeTabIndex == 0) {
            mTabIcon.setImageDrawable(getResources().getDrawable(mTabRes.iconSelected[0]));
            mTvTitle.setTextColor(getResources().getColor(mTabRes.textColorSelected));
        } else {
            mTabIcon.setImageDrawable(getResources().getDrawable(mTabRes.iconNormal[homeTabIndex]));
            mTvTitle.setTextColor(getResources().getColor(mTabRes.textColorNormal));
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
        if (mSelected) {
            return;
        }
        mSelected = true;
        mTvTitle.setTextColor(getResources().getColor(mTabRes.textColorSelected));
        mTabIcon.setImageDrawable(getResources().getDrawable(mTabRes.iconSelected[index]));
        mOnTabChangedListener.onChanged(index);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
        if (!mSelected) {
            return;
        }
        mSelected = false;
        mTvTitle.setTextColor(getResources().getColor(mTabRes.textColorNormal));
        mTabIcon.setImageDrawable(getResources().getDrawable(mTabRes.iconNormal[index]));
    }

    public void setMessageCount(int msgCount) {
        if (mTvMsgCount != null) {
            if (msgCount > 0) {
                mTvMsgCount.setText(String.valueOf(msgCount));
                mTvMsgCount.setVisibility(View.VISIBLE);
            } else {
                mTvMsgCount.setText("0");
                mTvMsgCount.setVisibility(View.INVISIBLE);
            }
        }
    }

}
