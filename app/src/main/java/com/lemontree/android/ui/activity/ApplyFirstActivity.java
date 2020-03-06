package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

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
    @BindView(R.id.textInputEditText_Name)
    TextInputEditText textInputEditTextName;
    @BindView(R.id.outlinedTextField_Name)
    TextInputLayout outlinedTextFieldName;
    @BindView(R.id.textInputEditText_KTP)
    TextInputEditText textInputEditTextKTP;
    @BindView(R.id.outlinedTextField_KTP)
    TextInputLayout outlinedTextFieldKTP;
    @BindView(R.id.spinner_gender)
    Spinner spinnerGender;
    @BindView(R.id.spinner_education)
    Spinner spinnerEducation;
    @BindView(R.id.spinner_marital_status)
    Spinner spinnerMaritalStatus;
    @BindView(R.id.spinner_children_number)
    Spinner spinnerChildrenNumber;

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFirstActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_first;
    }

    @Override
    protected void initializeView() {
        spinnerGender.setSelection(-1,true);
        spinnerEducation.setSelection(0,false);
//        spinnerMaritalStatus.setSelection(1);
//        spinnerChildrenNumber.setSelection(1);
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
