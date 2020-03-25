package com.lemontree.android.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemontree.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuideExpandableLVAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;


    //    private List<String> mGuideContentList = new ArrayList<>();
//    private List<String> mChildContentList = new ArrayList<>();
    private Map<String, String> map;
    private List<Map<String, String>> mList;

    public GuideExpandableLVAdapter(Context mContext, List<String> guideContentList, List<String> childContentList) {
//        mGuideContentList = guideContentList;
//        mChildContentList = childContentList;
        mInflater = LayoutInflater.from(mContext);
    }

    public GuideExpandableLVAdapter(Context mContext, Map<String, String> map) {
        this.map = map;
        mInflater = LayoutInflater.from(mContext);
    }

    public GuideExpandableLVAdapter(Context mContext, List<Map<String, String>> list) {
        this.mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    /*一级列表个数*/
    @Override
    public int getGroupCount() {
        return map.size();
    }

    /*每个二级列表内item的个数*/
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /*一级列表中单个item*/
    @Override
    public Object getGroup(int groupPosition) {
        return map;
    }

    /*二级列表中单个item*/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return map;
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /*每个item的id是否固定，一般为true*/
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /* 填充一级列表
     * isExpanded 是否已经展开
     * */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("karl", "getGroupView-" + groupPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_expandablelistview_group, null);
        }
        TextView tv_group = convertView.findViewById(R.id.tv_group);
        ImageView iv_group = convertView.findViewById(R.id.iv_group_arrow);

        iv_group.setVisibility(View.VISIBLE);

        List<String> groupKey = new ArrayList<>();
        for (Map.Entry<String, String> key : map.entrySet()) {
            groupKey.add(key.getKey());
//            Log.d("GuideExpandableLVAdapte", "key:" + key.getKey());
        }

        tv_group.setText(groupKey.get(groupPosition));

        //控制是否展开图标
        if (isExpanded) {
            iv_group.setImageResource(R.drawable.icon_arrow_down);
        } else {
            iv_group.setImageResource(R.drawable.icon_arrow_right);
        }
        return convertView;
    }

    /* 填充二级列表*/
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_expandablelistview_child, null);
        }
        List<String> childValue = new ArrayList<>();
        for (Map.Entry<String, String> key : map.entrySet()) {
            childValue.add(key.getValue());
        }
        TextView tv = convertView.findViewById(R.id.tv_child);
        tv.setText(Html.fromHtml(childValue.get(groupPosition)));

        return convertView;
    }

    /*二级列表中每个能否被选中，如果有点击事件一定要设为true*/
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}