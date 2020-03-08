package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyFirstActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
//    @BindView(R.id.textInputEditText_Name)
//    TextInputEditText textInputEditTextName;
    @BindView(R.id.outlinedTextField_Name)
    TextInputLayout outlinedTextFieldName;
    @BindView(R.id.textInputEditText_KTP)
    TextInputEditText textInputEditTextKTP;
    @BindView(R.id.outlinedTextField_KTP)
    TextInputLayout outlinedTextFieldKTP;
    @BindView(R.id.dropdown_tv_gender)
    AutoCompleteTextView dropdownTvGender;
    @BindView(R.id.dropdown_tv_education)
    AutoCompleteTextView dropdownTvEducation;
    @BindView(R.id.dropdown_tv_marry_state)
    AutoCompleteTextView dropdownTvMarryState;
    @BindView(R.id.dropdown_tv_children_number)
    AutoCompleteTextView dropdownTvChildrenNumber;
    @BindView(R.id.textInputEditText_DetailAddress)
    TextInputEditText textInputEditTextDetailAddress;

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFirstActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_first;
    }

    @Override
    protected void initializeView() {
        String[] GENDER = getResources().getStringArray(R.array.gender);
        String[] EDUCATION = getResources().getStringArray(R.array.education);
        String[] MARRY_STATE = getResources().getStringArray(R.array.maritalStatus);
        String[] CHILDREN_NUMBER = getResources().getStringArray(R.array.numberOfChildren);

        dropdownTvGender.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, GENDER));
        dropdownTvEducation.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, EDUCATION));
        dropdownTvMarryState.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, MARRY_STATE));
        dropdownTvChildrenNumber.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, CHILDREN_NUMBER));


    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    public boolean showMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.custom_menu, popup.getMenu());
        popup.show();
        return true;
    }
}
