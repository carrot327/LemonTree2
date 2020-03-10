package com.lemontree.android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.ContactInfoReqBean;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.UploadDataBySingle;
import com.lemontree.android.utils.SPUtils;
import com.minchainx.permission.util.PermissionListener;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ApplyThirdActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_contact_name_1)
    TextInputEditText tvContactName1;
    @BindView(R.id.tv_relation_1)
    AutoCompleteTextView tvRelation1;
    @BindView(R.id.et_telephone_1)
    TextInputEditText etTelephone1;
    @BindView(R.id.tv_contact_name_2)
    TextInputEditText tvContactName2;
    @BindView(R.id.tv_relation_2)
    AutoCompleteTextView tvRelation2;
    @BindView(R.id.et_telephone_2)
    TextInputEditText etTelephone2;
    @BindView(R.id.btn_select_contact_1)
    Button btnSelectContact1;
    @BindView(R.id.btn_select_contact_2)
    Button btnSelectContact2;

    private final int PICK_CONTACT = 101;

    private boolean mHasUploadAddressBook;
    private int clickedViewId;


    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyThirdActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_third;
    }

    @Override
    protected void initializeView() {
        String[] RELATION = getResources().getStringArray(R.array.contact_relation);
        tvRelation1.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, RELATION));
        tvRelation2.setAdapter(new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, RELATION));
        tvContactName1.setEnabled(false);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.btn_select_contact_1, R.id.btn_select_contact_2, R.id.tv_contact_name_1, R.id.tv_contact_name_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                checkContent();
                break;
            case R.id.btn_select_contact_1:
            case R.id.btn_select_contact_2:
                clickedViewId = view.getId();
                new Permission(mContext, new String[]{Manifest.permission.READ_CONTACTS}, new PermissionListener() {
                    @Override
                    public void onGranted() {

                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);

                        //上传通讯录
                        if (!mHasUploadAddressBook) {
                            uploadContactsOnly();
                        }
                    }

                    @Override
                    public void onDenied() {

                    }
                });
                break;
        }
    }

    private void checkContent() {
        boolean hasError = false;
        if (TextUtils.isEmpty(tvRelation1.getText().toString())) {
            tvRelation1.setError("Silakan isi");
            hasError = true;
        }
        if (TextUtils.isEmpty(tvContactName1.getText().toString())) {
            tvContactName1.setError("Silakan isi");
            hasError = true;
        }
        if (TextUtils.isEmpty(etTelephone1.getText().toString())) {
            etTelephone1.setError("Silakan isi");
            hasError = true;
        }

        if (TextUtils.isEmpty(tvRelation2.getText().toString())) {
            tvRelation2.setError("Silakan isi");
            hasError = true;
        }
        if (TextUtils.isEmpty(tvContactName2.getText().toString())) {
            tvContactName2.setError("Silakan isi");
            hasError = true;
        }
        if (TextUtils.isEmpty(etTelephone2.getText().toString())) {
            etTelephone2.setError("Silakan isi");
            hasError = true;
        }

        if (!hasError) {
            submit();
        }
    }

    private void submit() {
        ContactInfoReqBean reqBean = new ContactInfoReqBean();
        ContactInfoReqBean.ContactBean contactBean = reqBean.new ContactBean();
        contactBean.relation_name = tvContactName1.getText().toString();
        contactBean.relation_phone = etTelephone1.getText().toString();
        contactBean.relation_type = tvRelation1.getText().toString();
        ContactInfoReqBean.ContactBean contactBean2 = reqBean.new ContactBean();
        contactBean2.relation_name = tvContactName2.getText().toString();
        contactBean2.relation_phone = etTelephone2.getText().toString();
        contactBean2.relation_type = tvRelation2.getText().toString();

        reqBean.relationship = new ArrayList<>();
        reqBean.relationship.add(contactBean);
        reqBean.relationship.add(contactBean2);

        Log.d("karl", "map:" + new Gson().toJson(reqBean));

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_COMPANY_INFO)
                .content(new Gson().toJson(reqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        showToast("success");
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                getContact(data.getData());
            }
        }
    }

    /**
     * 上传通讯录
     */
    private void uploadContactsOnly() {
        new UploadDataBySingle().uploadAddressBook(BaseApplication.mUserId, new UploadDataBySingle.UploadAddressBookListener() {
            @Override
            public void success() {
                mHasUploadAddressBook = true;
                SPUtils.putBoolean(ConstantValue.UPLOAD_CONTACT_SUCCESS, true);
                SPUtils.putLong(ConstantValue.UPLOAD_CONTACT_TIME, System.currentTimeMillis());
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    protected void initializeImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.theme_color));
        }
    }

    /**
     * 获取单个联系人
     */
    private void getContact(Uri contactData) {

        Cursor c = getContentResolver().query(contactData, null, null, null, null);

        Map<String, String> map = new HashMap<>();
        if (c != null && c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String phoneNumber = "";
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId,
                        null,
                        null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }
            Log.i("获取到的联系人", "name-->" + name + ";phoneNumber" + phoneNumber);
            if (R.id.btn_select_contact_1 == clickedViewId) {
                tvContactName1.setText(name);
                etTelephone1.setText(phoneNumber);
                if (!TextUtils.isEmpty(name)) {
                    tvContactName1.setEnabled(true);
                }
            } else {
                tvContactName2.setText(name);
                etTelephone2.setText(phoneNumber);
                if (!TextUtils.isEmpty(name)) {
                    tvContactName2.setEnabled(true);
                }
            }
        } else {
            showToast("get contact failed");
        }
    }

}
