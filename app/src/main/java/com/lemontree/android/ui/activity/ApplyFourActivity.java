package com.lemontree.android.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.DialogFactory;
import com.lemontree.android.uploadUtil.Permission;
import com.lemontree.android.uploadUtil.Tools;
import com.lemontree.android.uploadUtil.UploadAuthImg;
import com.lemontree.android.uploadUtil.UploadKTPImg;
import com.minchainx.permission.util.PermissionListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lemontree.android.manager.BaseApplication.mHasShowHoldPicExample;
import static com.lemontree.android.manager.BaseApplication.mHasShowPicExample;

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
    public static final int FLAG_STAFF_CARD3 = 103;// 员工卡
    public static final int FLAG_GZD4 = 104;//工资单
    public static final int FLAG_GZXC5 = 105;//工作现场
    public static final int FLAG_YYZC6 = 106;//营业执照

    private File cameraSavePath;//拍照照片路径
    private Uri mUri;
    private Map<String, File> imgMap = new HashMap<>();
    int mClickItemName;
    File mFile;

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
        checkCameraPermission();
    }

    /**
     * 打开相册和相机，操作后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//相机
            if (requestCode == CAMERA_REQUEST_CODE) {
                handleImage(mUri);
            } else if (requestCode == ALBUM_REQUEST_CODE) {
                handleImage(data.getData());
            }
        }

    }

    private void checkCameraPermission() {
        new Permission(mContext, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, new PermissionListener() {
            @Override
            public void onGranted() {
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

            @Override
            public void onDenied() {
            }
        });
    }

    private void handleImage(Uri uri) {
        Bitmap bitmap;
        try {
            bitmap = getBitmapFormUriWithCompress(uri);
//            File file = Tools.bitmap2File(bitmap, Tools.getFileName());
            saveFileToMap(mFile);
            setImageSrc(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadAllImg() {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Memuat...");
        progressDialog.show();
        new UploadAuthImg().upload(imgMap, new UploadAuthImg.UploadImgListener() {
            @Override
            public void success() {
                progressDialog.dismiss();
                startActivity(BankInfoActivity.createIntent(mContext));
                finish();
            }

            @Override
            public void error() {
                progressDialog.dismiss();

            }
        });
    }

    private void saveFileToMap(File file) {
        if (file == null) return;
        switch (mClickItemName) {
            case FLAG_KTP1:
                imgMap.put("KTP1", file);
                // TODO: 2020-03-13
//                checkOCR();
                break;
            case FLAG_KTP_HOLD2:
                imgMap.put("KTP_HOLD2", file);
                break;
            case FLAG_STAFF_CARD3:
                imgMap.put("STAFF_CARD3", file);
                break;
            case FLAG_GZD4:
                imgMap.put("GZD4", file);
                break;
            case FLAG_GZXC5:
                imgMap.put("GZXC5", file);
                break;
            case FLAG_YYZC6:
                imgMap.put("YYZC6", file);
                break;
        }
    }


    private void checkOCR() {
        new UploadKTPImg().upload(imgMap, new UploadKTPImg.UploadImgListener() {
            @Override
            public void success() {
                handlerKtpCheck.sendEmptyMessage(1);
            }

            @Override
            public void error() {
                handlerKtpCheck.sendEmptyMessage(2);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handlerKtpCheck = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //do nothing
                    break;
                case 2:
                    showToast("ktp can't find");
                    ivShowKtp.setImageBitmap(null);
                    break;
            }
        }
    };

    private void setImageSrc(Bitmap bm) {
        //设置图片圆角角度
//        RoundedCorners roundedCorners = new RoundedCorners(30);
//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
//        Glide.with(mContext).load(bm).apply(options).into(ivShowKtp);

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

    public Bitmap getBitmapFormUriWithCompress(Uri uri) throws FileNotFoundException, IOException {
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
        float hh = 2400f;//这里设置高度为800f
        float ww = 1440f;//这里设置宽度为480f
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

        return compressImage(bitmap);
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0)
                break;
            long length = baos.toByteArray().length;
            Log.d("ApplyFourActivity", "length:" + length);

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据输出流存放到输入流中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把输入流数据生成图片

        mFile = new File(BaseApplication.getContext().getExternalFilesDir(null), Tools.getFileNameByTime() + ".jpg");
        Log.d("ApplyFourActivity", "BaseApplication.getContext().getExternalFilesDir(null):" + BaseApplication.getContext().getExternalFilesDir(null));
        try {
            FileOutputStream fos = new FileOutputStream(mFile);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("ApplyFourActivity", "mFile.length():" + mFile.length());
        return bitmap;
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.iv_pick_ktp, R.id.iv_take_photo_ktp, R.id.iv_pick_hold_ktp,
            R.id.iv_take_photo_hold_ktp, R.id.iv_pick_staff_card, R.id.iv_take_photo_staff_card, R.id.iv_pick_gongzidan,
            R.id.iv_take_photo_gongzidan, R.id.iv_pick_yingyezhizhao, R.id.iv_take_photo_yingyezhizhao,
            R.id.iv_pick_gongzuochangjing, R.id.iv_take_photo_gongzuochangjing, R.id.iv_show_ktp, R.id.iv_show_hold_ktp,
            R.id.iv_show_staff_card, R.id.iv_show_gongzidan, R.id.iv_show_gongzuochangjing, R.id.iv_show_yingyezhizhao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                uploadAllImg();
                break;
            case R.id.iv_pick_ktp:
                if (mHasShowPicExample){
                    goPhotoAlbum(ALBUM_REQUEST_CODE);
                }else {
                    DialogFactory.createExamplePicDialog(mContext,R.drawable.icon_sfz_example).show();
                    mHasShowPicExample = true;
                }
                mClickItemName = FLAG_KTP1;
                break;
            case R.id.iv_pick_hold_ktp:
                if (mHasShowPicExample){
                    goPhotoAlbum(ALBUM_REQUEST_CODE);
                }else {
                    DialogFactory.createExamplePicDialog(mContext,R.drawable.icon_sfz_example).show();
                    mHasShowPicExample = true;
                }
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
                if (mHasShowPicExample){
                    goCamera(CAMERA_REQUEST_CODE);
                }else {
                    DialogFactory.createExamplePicDialog(mContext,R.drawable.icon_sfz_example).show();
                    mHasShowPicExample = true;
                }
                mClickItemName = FLAG_KTP1;
                break;
            case R.id.iv_take_photo_hold_ktp:
                if (mHasShowHoldPicExample){
                    goCamera(CAMERA_REQUEST_CODE);
                }else {
                    DialogFactory.createExamplePicDialog(mContext,R.drawable.icon_sfz_hold_example).show();
                    mHasShowHoldPicExample = true;
                }
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
            case R.id.iv_show_ktp:
                if (ivShowKtp.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowKtp.getDrawable()).show();
//                    Log.d("ApplyFourActivity", "ivShowKtp.getHeight():" + ivShowKtp.getHeight());
//                    Log.d("ApplyFourActivity", "ivShowKtp.getWidth():" + ivShowKtp.getWidth());
                }
                break;
            case R.id.iv_show_hold_ktp:
                if (ivShowHoldKtp.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowHoldKtp.getDrawable()).show();
                }
                break;
            case R.id.iv_show_staff_card:
                if (ivShowStaffCard.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowStaffCard.getDrawable()).show();
                }
                break;
            case R.id.iv_show_gongzidan:
                if (ivShowGongzidan.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowGongzidan.getDrawable()).show();
                }
                break;
            case R.id.iv_show_gongzuochangjing:
                if (ivShowGongzuochangjing.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowGongzuochangjing.getDrawable()).show();
                }
                break;
            case R.id.iv_show_yingyezhizhao:
                if (ivShowYingyezhizhao.getDrawable() != null) {
                    DialogFactory.showImgByDrawable(mContext, ivShowYingyezhizhao.getDrawable()).show();
                }
                break;
        }
    }
}
