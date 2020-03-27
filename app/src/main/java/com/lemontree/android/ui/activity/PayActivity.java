package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.base.BaseResponseBean;
import com.lemontree.android.bean.request.GetPayCodeReqBean;
import com.lemontree.android.bean.response.GetPayCodeResBean;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;
import com.lemontree.android.network.OKHttpClientEngine;
import com.lemontree.android.ui.adapter.GuideExpandableLVAdapter;
import com.lemontree.android.utils.CurrencyFormatUtils;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class PayActivity extends BaseActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_pay_amount)
    TextView tvPayAmount;
    @BindView(R.id.tv_pay_code)
    TextView tvPayCode;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;

    private String mIsExtentPay;
    private String mPayTypeName;
    private String mRepayAmount;
    private String mIsUseCoupon;
    private Map<String, String> guide_content;
    private Map<String, String> MAP_CIMBVA = new LinkedHashMap<>();
    private Map<String, String> MAP_BNIVA = new LinkedHashMap<>();
    private Map<String, String> MAP_MANDIRI_VA = new LinkedHashMap<>();
    private Map<String, String> MAP_PERMATA_VA = new LinkedHashMap<>();
    private Map<String, String> MAP_TOKO_SERBA_ADA = new LinkedHashMap<>();

    public static Intent createIntent(Context context) {
        return new Intent(context, PayActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_code;
    }

    @Override

    protected void initializeView() {
        Intent intent = getIntent();
        mIsExtentPay = intent.getStringExtra("is_extent_pay");
        mPayTypeName = intent.getStringExtra("pay_type_name");//还款方式（eg. MandiriVA）
        mRepayAmount = intent.getStringExtra("repayment_amount");
        mIsUseCoupon = intent.getStringExtra("is_use_coupon");
//        initGuideContent();

        title.setText(mPayTypeName);
//        expandableListView.setAdapter(new GuideExpandableLVAdapter(mContext, guide_content));
//        expandableListView.setGroupIndicator(null);
        if ("ConvenienceStore".equals(mPayTypeName)) {
            expandableListView.expandGroup(0);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

    }

    private void initGuideContent() {
        switch (mPayTypeName) {
            case "ConvenienceStore":
                MAP_TOKO_SERBA_ADA.put(TOKO_TITLE_1, TOKO_CONTENT_1);
                guide_content = MAP_TOKO_SERBA_ADA;
                break;
            case "MandiriVA":
                MAP_MANDIRI_VA.put(MandiriVA_TITLE_1, MandiriVA_CONTENT_1);
                MAP_MANDIRI_VA.put(MandiriVA_TITLE_2, MandiriVA_CONTENT_2);
                guide_content = MAP_MANDIRI_VA;
                break;
            case "PermataVA":
                MAP_PERMATA_VA.put(PermataVA_TITLE_1, PermataVA_CONTENT_1);
                MAP_PERMATA_VA.put(PermataVA_TITLE_2, PermataVA_CONTENT_2);
                MAP_PERMATA_VA.put(PermataVA_TITLE_3, PermataVA_CONTENT_3);
                guide_content = MAP_PERMATA_VA;
                break;
            case "BNIVA":
                MAP_BNIVA.put(BNIVA_TITLE_1, BNIVA_CONTENT_1);
                MAP_BNIVA.put(BNIVA_TITLE_2, BNIVA_CONTENT_2);
                MAP_BNIVA.put(BNIVA_TITLE_3, BNIVA_CONTENT_3);
                MAP_BNIVA.put(BNIVA_TITLE_4, BNIVA_CONTENT_4);
                MAP_BNIVA.put(BNIVA_TITLE_5, BNIVA_CONTENT_5);
                guide_content = MAP_BNIVA;
                break;
            case "CIMBVA":
                MAP_CIMBVA.put(CIMBVA_TITLE_1, CIMBVA_CONTENT_1);
                MAP_CIMBVA.put(CIMBVA_TITLE_2, CIMBVA_CONTENT_2);
                MAP_CIMBVA.put(CIMBVA_TITLE_3, CIMBVA_CONTENT_3);
                MAP_CIMBVA.put(CIMBVA_TITLE_4, CIMBVA_CONTENT_4);
                MAP_CIMBVA.put(CIMBVA_TITLE_5, CIMBVA_CONTENT_5);
                guide_content = MAP_CIMBVA;
                break;
        }
    }

    @Override
    protected void loadData() {
        if ("2".equals(mIsExtentPay)) {//展期
            getDelayPayCode();
        } else {
            getPayCode();
        }
    }


    /**
     * 获取还款码（正常/部分还款）
     */
    private void getPayCode() {
        GetPayCodeReqBean getPayCodeReqBean = new GetPayCodeReqBean();
        getPayCodeReqBean.pay_type = mPayTypeName;
        getPayCodeReqBean.cash_coupon_type = mIsUseCoupon;
        getPayCodeReqBean.repayment_amount = mRepayAmount;//部分还款才有此值
        getPayCodeReqBean.order_type = TextUtils.isEmpty(mRepayAmount) ? "1" : "3"; //部分还款3，否则为1

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.GET_PAY_CODE)
                .content(new Gson().toJson(getPayCodeReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<GetPayCodeResBean>() {
                    @Override
                    public void onSuccess(Call call, GetPayCodeResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            tvPayAmount.setText(CurrencyFormatUtils.formatDecimal(response.amount));
                            tvPayCode.setText(response.pay_code.replaceAll("\\d{4}(?!$)", "$0 "));
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    /**
     * 获取展期还款码
     */
    private void getDelayPayCode() {
        GetPayCodeReqBean getPayCodeReqBean = new GetPayCodeReqBean();
        getPayCodeReqBean.pay_type = mPayTypeName;

        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + ConstantValue.GET_DELAY_PAY_CODE)
                .content(new Gson().toJson(getPayCodeReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<GetPayCodeResBean>() {
                    @Override
                    public void onSuccess(Call call, GetPayCodeResBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            tvPayAmount.setText(CurrencyFormatUtils.formatDecimal(response.amount));
                            tvPayCode.setText(response.pay_code.replaceAll("\\d{4}(?!$)", "$0 "));
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }


    /*CIMBVA*/
    public String CIMBVA_TITLE_1 = "ATM CIMB";
    public String CIMBVA_TITLE_2 = "CIMB CLICKS";
    public String CIMBVA_TITLE_3 = "GO MOBILE";
    public String CIMBVA_TITLE_4 = "INTERNET BANKING LAINNYA";
    public String CIMBVA_TITLE_5 = "ATM BANK LAINNYA";
    public String CIMBVA_CONTENT_1 = "1. Menu Transfer <br><br> \n" +
            "2. Pilih Antar Rekening CIMB NIAGA <br><br> \n" +
            "3. Input nominal transfer<br><br> \n" +
            "4. Input nomor virtual account di rekening tujuan<br><br> \n" +
            "5. Konfirmasi dan proses pembayaran";
    public String CIMBVA_CONTENT_2 = "1. Menu Transfer <br><br> \n" +
            "2. Input nominal transfer <br><br> \n" +
            "3. Pilih Other Account – Bank CIMB Niaga / Rekening Ponsel<br><br> \n" +
            "4. Rekening penerima diisi dengan nomor virtual account<br><br> \n" +
            "5. Input mPin<br><br> \n" +
            "6. Konfirmasi dan proses pembayaran";
    public String CIMBVA_CONTENT_3 = "1. Menu Transfer <br><br> \n" +
            "2. Pilih Transfer to Other CIMB Niaga Account <br><br> \n" +
            "3. Input nomor virtual account dan nominal pembayaran<br><br> \n" +
            "4. Input PIN<br><br> \n" +
            "5. Konfirmasi dan proses pembayaran";
    public String CIMBVA_CONTENT_4 = "1. Menu Transfer<br><br>\n" +
            "2. Pilih Rekening Bank Lain<br><br>\n" +
            "3. Input nomor virtual account pada rekening tujuan<br><br>\n" +
            "4. Input nominal pembayaran<br><br>\n" +
            "5.Input bank penerima dengan CIMB Niaga<br><br>\n" +
            "6. Jenis transfer yang digunakan adalah online/realtime<br><br>\n" +
            "7.Konfirmasi dan proses pembayaran";
    public String CIMBVA_CONTENT_5 = "1. Menu Transfer<br><br>\n" +
            "2. Pilih Rekening Bank Lain<br><br>\n" +
            "3. Input kode bank 022<br><br>\n" +
            "4. Input nomor virtual account pada rekening tujuan<br><br>\n" +
            "5. Input nominal pembayaran<br><br>\n" +
            "6. Konfirmasi dan proses pembayaran";

    /*BNIVA*/
    public String BNIVA_TITLE_1 = "ATM BNI";
    public String BNIVA_TITLE_2 = "INTERNET BANKING BNI";
    public String BNIVA_TITLE_3 = "MOBILE BANKING BNI";
    public String BNIVA_TITLE_4 = "INTERNET BANKING LAINNYA";
    public String BNIVA_TITLE_5 = "ATM BANK LAINNYA";
    public String BNIVA_CONTENT_1 = "1. Menu Lainnya <br><br> \n" +
            "2. Pilih Transfer <br><br> \n" +
            "3. Pilih Rekening Tabungan<br><br> \n" +
            "4. Pilih Ke Rekening BNI<br><br> \n" +
            "5. Input nomor virtual account<br><br> \n" +
            "6. Input nominal transfer<br><br> \n" +
            "7. Konfirmasi pemindahbukuan";
    public String BNIVA_CONTENT_2 = "1. Menu Transfer <br><br> \n" +
            "2. Pilih Tambah Rekening Favorit <br><br> \n" +
            "3. Pilih Antar Rekening BNI<br><br> \n" +
            "4. Bila menggunakan desktop untuk menambah rekening: menu Transaksi, lalu Atur Rekening Tujuan,\n" +
            "Tambah Rekening Tujuan<br><br> \n" +
            "5. Masukkan nama dan nomor virtual account<br><br> \n" +
            "6. Input kode otentikasi token<br><br> \n" +
            "7. Rekening tujuan berhasil ditambahkan<br><br> \n" +
            "8. Kembali ke menu Transfer<br><br> \n" +
            "9. Pilih Transfer Antar Rekening BNI<br><br> \n" +
            "10. Pilih Rekening Tujuan<br><br> \n" +
            "11. Pilih rekening debet dan input nominal transfer<br><br> \n" +
            "12. Input kode otentikasi token";
    public String BNIVA_CONTENT_3 = "1. Menu Transfer<br><br> \n" +
            "2. Pilih Antar Rekening BNI dan Input Rekening Baru<br><br> \n" +
            "3. Input rekening debet, nomor virtual account di rekening tujuan, dan nominal transfer<br><br> \n" +
            "4. Input password dan konfirmasi transaksi";
    public String BNIVA_CONTENT_4 = "1. Menu Transfer<br><br> \n" +
            "2. Pilih Rekening Bank Lain<br><br> \n" +
            "3. Input nomor virtual account pada rekening tujuan<br><br> \n" +
            "4. Input nominal pembayaran<br><br> \n" +
            "5. Input bank penerima dengan BNI<br><br> \n" +
            "6. Jenis transfer yang digunakan adalah online/realtime<br><br> \n" +
            "7. Konfirmasi dan proses pembayaran";
    public String BNIVA_CONTENT_5 = "1. Menu Transfer<br><br>\n" +
            "2. Pilih Rekening Bank Lain<br><br>\n" +
            "3. Input kode bank 009<br><br>\n" +
            "4. Input nomor virtual account pada rekening tujuan<br><br>\n" +
            "5.Input nominal pembayaran<br><br>\n" +
            "6. Konfirmasi dan proses pembayaran";

    /*MandiriVA*/
    public String MandiriVA_TITLE_1 = "ATM MANDIRI";
    public String MandiriVA_TITLE_2 = "ONLINE BANKING";
    public String MandiriVA_CONTENT_1 = "1. Pilih menu Bayar/Beli <br><br> \n" +
            "2. Pilih menu Multipayment <br><br> \n" +
            "3. Input kode perusahaan Bank Mandiri (5 digit terdepan kode pembayaran) tekan Benar Input nomor virtual account kemudian tekan Benar<br><br> \n" +
            "4. Pilih Nomor 1 atau sesuai tagihan yang akan dibayar lalu tekan YA";
    public String MandiriVA_CONTENT_2 = "1. Input User ID & Password <br><br> \n" +
            "2. Pilih menu Pembayaran <br><br> \n" +
            "3. Pilih menu Multipayment<br><br> \n" +
            "4. Pada Kolom Penyedia Jasa, pilih penyedia layanan yang sesuai<br><br> \n" +
            "5. Input nomor virtual account<br><br> \n" +
            "6. Pilih Lanjut<br><br> \n" +
            "• Web : <br><br> \n" +
            "a. OTP dikirimkan ke nomor HP  <br><br> \n" +
            "b. OTP yang didapatkan diinput di Token untuk mendapatkan challenge code c. Chanllenge code input ke Mandiri online, lalu klik Lanjut <br><br> \n" +
            "• Apps : <br><br>  \n" +
            "a. Setelah muncul konfirmasi transaksi pilih Kirim <br><br> \n" +
            "b. Input MPIN (6 digit) <br><br> \n" +
            "<b style=\"color: #DD524D;\"> PS: Kalau kode pembayaran diawalkan dengan 70014, berarti cuma bisa bayar di produk MANDIRI </b>";

    /*PermataVA*/
    public String PermataVA_TITLE_1 = "ATM PERMATA";
    public String PermataVA_TITLE_2 = "MOBILE INTERNET";
    public String PermataVA_TITLE_3 = "ATM BERSAMA, ALTO & PRIMA";
    public String PermataVA_CONTENT_1 = "1. Pilih menu Transaksi Lainnya <br><br> \n" +
            "2. Pilih Pembayaran <br><br> \n" +
            "3. Pilih Pembayaran Lainnya<br><br> \n" +
            "4. Pilih Virtual Account<br><br> \n" +
            "5. Input nomor virtual account<br><br> \n" +
            "6. Pada layar akan tampil konfirmasi pembayaran";
    public String PermataVA_CONTENT_2 = "1. Masukan User ID & Password<br><br> \n" +
            "2. Pilih Pembayaran Tagihan<br><br> \n" +
            "3. Pilih virtual account<br><br> \n" +
            "4. Input nomor virtual account<br><br> \n" +
            "5. Input nominal pembayaran<br><br> \n" +
            "6. Apabila data sudah sesuai masukan otentikasi transaksi/Token";
    public String PermataVA_CONTENT_3 = "1. Pilih menu Transfer<br><br> \n" +
            "1. Pilih Transfer ke Bank Lain<br><br> \n" +
            "• Jika melalui ATM Bersama & Alto :<br><br> \n" +
            "a.Input kode PermataBank (013) +16 digit kode bayar<br><br> \n" +
            "• Jika melalui ATM Prima :<br><br> \n" +
            "a. Masukan kode PermataBank(013)tekan BENAR, dilanjutkan dengan 16 digit kode bayar<br><br> \n" +
            "b. Input nominal pembayaran<br><br> \n" +
            "c. Pada layar akan tampil konfirmasi Nama & Nominal Pembayaran";

    /*Langkah pembayaran*/
    public String TOKO_TITLE_1 = "Toko serba ada";
    public String TOKO_CONTENT_1 = "1. Sebutkan mau bayar ke Finpay021, lalu kasih kasir pay code\n" +
            "<br><br>\n" +
            "2. Jika tidak dapat Finpay021, sebutkan mau bayar ke telkom/telefon, kemudian pilih speedy/indihome, kemudian kasih kasir pay code\n" +
            "<br><br>\n" +
            "3. Kasir akan menanyakan kode pembayaran. Berikan kode pembayaran anda. Kasir akanmelakukan konfirmasi data konsumen berupa Nama Merchant, Nama Konsumen,\n" +
            "danNominal. Lakukan pembayaran ke kasir sejumlah nominal yang disebutkan\n" +
            "<br><br>\n" +
            "4. Terima struk sebagai bukti pembayaran sudah sukses dilakukan. Notifikasi pembayaranakan langsung diterima oleh Merchant\n" +
            "<br><br>\n" +
            "5. Jika tidak bisa dapatkan saluran tersebut di saluran membayar, silakan coba cari \"wayangpay\" atau \"wayangpay via nicepay\" dan ulangi operasi tersebut\n" +
            "<br><br>\n" +
            "6. Selesai\n";
}
