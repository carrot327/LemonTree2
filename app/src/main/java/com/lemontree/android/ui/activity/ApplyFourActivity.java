package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.manager.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyFourActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.iv_show_ktp)
    ImageView ivShowKtp;
    @BindView(R.id.iv_pick_ktp)
    ImageView ivPickPhoto;
    @BindView(R.id.iv_take_photo_ktp)
    ImageView ivTakePhoto;
    @BindView(R.id.iv_show_hold_ktp)
    ImageView ivShowHoldKtp;
    @BindView(R.id.iv_pick_hold_ktp)
    ImageView ivPickPhoto2;
    @BindView(R.id.iv_take_photo_hold_ktp)
    ImageView ivTakePhoto2;
    @BindView(R.id.iv_show_staff_card)
    ImageView ivShowStaffCard;
    @BindView(R.id.iv_pick_staff_card)
    ImageView ivStaffCardPick;
    @BindView(R.id.iv_take_photo_staff_card)
    ImageView ivStaffCardTakePhoto;
    @BindView(R.id.iv_show_gongzidan)
    ImageView ivShowGongzidan;
    @BindView(R.id.iv_pick_gongzidan)
    ImageView ivPickGongzidan;
    @BindView(R.id.iv_take_photo_gongzidan)
    ImageView ivTakePhotoGongzidan;
    @BindView(R.id.iv_show_yingyezhizhao)
    ImageView ivShowYingyezhizhao;
    @BindView(R.id.iv_pick_yingyezhizhao)
    ImageView ivPickYingyezhizhao;
    @BindView(R.id.iv_take_photo_yingyezhizhao)
    ImageView ivTakePhotoYingyezhizhao;
    @BindView(R.id.iv_show_gongzuochangjing)
    ImageView ivShowGongzuochangjing;
    @BindView(R.id.iv_pick_gongzuochangjing)
    ImageView ivPickGongzuochangjing;
    @BindView(R.id.iv_take_photo_gongzuochangjing)
    ImageView ivTakePhotoGongzuochangjing;

    //---拍照---
    //相册请求码
    private static final int ALBUM_REQUEST_CODE_KTP = 101;
    private static final int ALBUM_REQUEST_CODE_KTP_HOLD = 102;
    private static final int ALBUM_REQUEST_CODE_STAFF_CARD = 103;// 员工卡
    private static final int ALBUM_REQUEST_CODE_GZD = 104;//工资单
    private static final int ALBUM_REQUEST_CODE_GZXC = 105;//工作现场
    private static final int ALBUM_REQUEST_CODE_YYZC = 106;//营业执照

    private static final int CAMERA_REQUEST_CODE_KTP = 201;
    private static final int CAMERA_REQUEST_CODE_KTP_HOLD = 202;
    private static final int CAMERA_REQUEST_CODE_STAFF_CARD = 203;
    private static final int CAMERA_REQUEST_CODE_GZD = 204;
    private static final int CAMERA_REQUEST_CODE_GZXC = 205;
    private static final int CAMERA_REQUEST_CODE_YYZC = 206;

    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFourActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_four;
    }

    @Override
    protected void initializeView() {

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.iv_pick_ktp, R.id.iv_take_photo_ktp, R.id.iv_pick_hold_ktp,
            R.id.iv_take_photo_hold_ktp, R.id.iv_pick_staff_card, R.id.iv_take_photo_staff_card, R.id.iv_pick_gongzidan,
            R.id.iv_take_photo_gongzidan, R.id.iv_pick_yingyezhizhao, R.id.iv_take_photo_yingyezhizhao,
            R.id.iv_pick_gongzuochangjing, R.id.iv_take_photo_gongzuochangjing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                break;


            case R.id.iv_pick_ktp:
                goPhotoAlbum(ALBUM_REQUEST_CODE_KTP);
                break;
            case R.id.iv_pick_hold_ktp:
                goPhotoAlbum(ALBUM_REQUEST_CODE_KTP_HOLD);
                break;
            case R.id.iv_pick_staff_card:
                goPhotoAlbum(ALBUM_REQUEST_CODE_STAFF_CARD);
                break;
            case R.id.iv_pick_gongzidan:
                goPhotoAlbum(ALBUM_REQUEST_CODE_GZD);
                break;
            case R.id.iv_pick_gongzuochangjing:
                goPhotoAlbum(ALBUM_REQUEST_CODE_GZXC);
                break;
            case R.id.iv_pick_yingyezhizhao:
                goPhotoAlbum(ALBUM_REQUEST_CODE_YYZC);
                break;


            case R.id.iv_take_photo_ktp:
                goCamera(CAMERA_REQUEST_CODE_KTP);
                break;
            case R.id.iv_take_photo_hold_ktp:
                goCamera(CAMERA_REQUEST_CODE_KTP_HOLD);
                break;
            case R.id.iv_take_photo_staff_card:
                goCamera(CAMERA_REQUEST_CODE_STAFF_CARD);
                break;
            case R.id.iv_take_photo_gongzidan:
                goCamera(CAMERA_REQUEST_CODE_GZD);
                break;
            case R.id.iv_take_photo_gongzuochangjing:
                goCamera(CAMERA_REQUEST_CODE_GZXC);
                break;
            case R.id.iv_take_photo_yingyezhizhao:
                goCamera(CAMERA_REQUEST_CODE_YYZC);
                break;
        }
    }

    /**
     * 打开相册
     */
    private void goPhotoAlbum(int ALBUM_REQUEST_CODE) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 打开相机
     */
    private void goCamera(int CAMERA_REQUEST_CODE) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(mContext, "com.lemontree.android.provider", cameraSavePath);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else { //否则使用Uri.fromFile(file)方法获取Uri
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 打开相册和相机，操作后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath = "";
        if (resultCode == RESULT_OK) {//相机
            if (requestCode == CAMERA_REQUEST_CODE_KTP) {

            } else if (requestCode == CAMERA_REQUEST_CODE_KTP_HOLD) {

            } else if (requestCode == CAMERA_REQUEST_CODE_STAFF_CARD) {

            } else if (requestCode == CAMERA_REQUEST_CODE_GZD) {

            } else if (requestCode == CAMERA_REQUEST_CODE_GZXC) {

            } else if (requestCode == CAMERA_REQUEST_CODE_YYZC) {

            } else if (requestCode == ALBUM_REQUEST_CODE_KTP) {

            } else if (requestCode == ALBUM_REQUEST_CODE_KTP_HOLD) {

            } else if (requestCode == ALBUM_REQUEST_CODE_STAFF_CARD) {

            } else if (requestCode == ALBUM_REQUEST_CODE_GZD) {

            } else if (requestCode == ALBUM_REQUEST_CODE_GZXC) {

            } else if (requestCode == ALBUM_REQUEST_CODE_YYZC) {

            }
        }

    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
