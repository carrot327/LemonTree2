package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;

public class ApplyFirstActivity2 extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFirstActivity2.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_first2;
    }

    @Override
    protected void initializeView() {
        findViewById(R.id.mt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        findViewById(R.id.et).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        String[] COUNTRIES = new String[]{"Item 1", "Item 2", "Item 3", "Item 4"};
        String[] COUNTRIES2 = getResources().getStringArray(R.array.gender);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, COUNTRIES2));

        TextInputLayout textInputLayout = findViewById(R.id.outlinedTextField);
        textInputLayout.setError("Error message");
//        textInputLayout.set

        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
    }

    @Override
    protected void loadData() {
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
