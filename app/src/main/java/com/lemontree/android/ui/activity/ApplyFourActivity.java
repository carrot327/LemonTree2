package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    private static final int ALBUM_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 2001;

    public static final int FLAG_KTP1 = 101;
    public static final int FLAG_KTP_HOLD2 = 102;
    public static final int FLAG_STAFF_CARD3 = 103;
    public static final int FLAG_GZD4 = 104;
    public static final int FLAG_GZXC5 = 105;
    public static final int FLAG_YYZC6 = 106;

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
    private Uri mUri;

    public static Intent createIntent(Context context) {
        return new Intent(context, ApplyFourActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_four;
    }

    @Override
    protected void initializeView() {
//        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/colada/authPic/" + System.currentTimeMillis() + ".jpg");

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    int mClickItemName;

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
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_KTP1;
                break;
            case R.id.iv_pick_hold_ktp:
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_KTP_HOLD2;
                break;
            case R.id.iv_pick_staff_card:
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_STAFF_CARD3;
                break;
            case R.id.iv_pick_gongzidan:
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_GZD4;
                break;
            case R.id.iv_pick_gongzuochangjing:
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_GZXC5;
                break;
            case R.id.iv_pick_yingyezhizhao:
                goPhotoAlbum(ALBUM_REQUEST_CODE);
                mClickItemName = FLAG_YYZC6;
                break;


            case R.id.iv_take_photo_ktp:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_KTP1;
                break;
            case R.id.iv_take_photo_hold_ktp:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_KTP_HOLD2;
                break;
            case R.id.iv_take_photo_staff_card:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_STAFF_CARD3;
                break;
            case R.id.iv_take_photo_gongzidan:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_GZD4;
                break;
            case R.id.iv_take_photo_gongzuochangjing:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_GZXC5;
                break;
            case R.id.iv_take_photo_yingyezhizhao:
                goCamera(CAMERA_REQUEST_CODE);
                mClickItemName = FLAG_YYZC6;
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
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //如果在Android7.0以上,使用FileProvider获取Uri
//            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mUri = FileProvider.getUriForFile(mContext, "com.lemontree.android.fileProvider", cameraSavePath);
            Log.d("karl", "mUri:" + mUri);
        } else { //否则使用Uri.fromFile(file)方法获取Uri
            mUri = Uri.fromFile(cameraSavePath);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
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
            if (requestCode == CAMERA_REQUEST_CODE) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android 7.0及以上获取文件 Uri
//                    photoPath = String.valueOf(cameraSavePath);
//                } else {
//                    photoPath = mUri.getEncodedPath();
//                }

//                Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(mUri));
//
//
//            File file = Tools.compressImage(bitmap, fileName);

                Bitmap bm = null;
                try {
                    bm = getBitmapFormUri(mUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setImageSrc(bm);

            } else if (requestCode == ALBUM_REQUEST_CODE) {
//                photoPath = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                mUri = data.getData();//通过getData获取到Uri
                Log.d("karl", "mUri2:" + mUri);
                Bitmap bm = null;
                try {
                    bm = getBitmapFormUri(mUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setImageSrc(bm);
            }
        }

    }

    private void setImageSrc(Bitmap bm) {
        switch (mClickItemName) {
            case FLAG_KTP1:
                ivShowKtp.setImageBitmap(bm);
                break;
            case FLAG_KTP_HOLD2:
                ivShowHoldKtp.setImageBitmap(bm);
                break;
            case FLAG_STAFF_CARD3:
                ivShowStaffCard.setImageBitmap(bm);
                break;
            case FLAG_GZD4:
                ivShowGongzidan.setImageBitmap(bm);
                break;
            case FLAG_GZXC5:
                ivShowGongzuochangjing.setImageBitmap(bm);
                break;
            case FLAG_YYZC6:
                ivShowYingyezhizhao.setImageBitmap(bm);
                break;

        }
    }

    public Bitmap getBitmapFormUri(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        //这一段代码是不加载文件到内存中也得到bitmap的真实宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
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
