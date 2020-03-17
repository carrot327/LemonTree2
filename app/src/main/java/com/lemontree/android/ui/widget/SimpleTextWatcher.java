package com.lemontree.android.ui.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lemontree.android.R;
import com.lemontree.android.manager.BaseApplication;

public class SimpleTextWatcher implements TextWatcher {
    AutoCompleteTextView mAutoCompleteTextView;
    TextInputEditText mTextInputEditText;
    TextInputLayout mTextInputLayout;
    int stringResource;

    public SimpleTextWatcher(TextInputEditText mTextInputEditText, TextInputLayout mTextInputLayout) {
        this.mTextInputEditText = mTextInputEditText;
        this.mTextInputLayout = mTextInputLayout;
        this.stringResource = R.string.text_pls_input;
    }

    public SimpleTextWatcher(TextInputEditText mTextInputEditText, TextInputLayout mTextInputLayout, int stringResource) {
        this.mTextInputEditText = mTextInputEditText;
        this.mTextInputLayout = mTextInputLayout;
        this.stringResource = stringResource;
    }

    public SimpleTextWatcher(AutoCompleteTextView autoCompleteTextView, TextInputLayout mTextInputLayout) {
        this.mAutoCompleteTextView = autoCompleteTextView;
        this.mTextInputLayout = mTextInputLayout;
        this.stringResource = R.string.text_pls_input;
    }

    public SimpleTextWatcher(AutoCompleteTextView autoCompleteTextView, TextInputLayout mTextInputLayout, int stringResource) {
        this.mAutoCompleteTextView = autoCompleteTextView;
        this.mTextInputLayout = mTextInputLayout;
        this.stringResource = stringResource;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextInputEditText != null) {
            if (TextUtils.isEmpty(mTextInputEditText.getText().toString())) {
                mTextInputLayout.setError(BaseApplication.getApplication().getString(stringResource));
            } else {
                mTextInputLayout.setError("");
            }
        }else if (mAutoCompleteTextView!=null){
            if (TextUtils.isEmpty(mAutoCompleteTextView.getText().toString())) {
                mTextInputLayout.setError(BaseApplication.getApplication().getString(stringResource));
            } else {
                mTextInputLayout.setError("");
            }
        }
    }
}
