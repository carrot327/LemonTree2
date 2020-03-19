package com.lemontree.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lemontree.android.R;
import com.lemontree.android.base.BaseActivity;
import com.lemontree.android.ui.adapter.GuideExpandableLVAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class PayCodeActivity extends BaseActivity {


    @BindView(R.id.tv_pay_amount)
    TextView tvPayAmount;
    @BindView(R.id.tv_pay_code)
    TextView tvPayCode;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;


    public static Intent createIntent(Context context) {
        return new Intent(context, PayCodeActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_code;
    }

    private Map<String, String> MAP_CIMBVA = new LinkedHashMap<>();
    private Map<String, String> MAP_BNIVA = new LinkedHashMap<>();
    private Map<String, String> MAP_MANDIRI_VA = new LinkedHashMap<>();
    private Map<String, String> MAP_PERMATA_VA = new LinkedHashMap<>();
    private Map<String, String> MAP_TOKO_SERBA_ADA = new LinkedHashMap<>();
    List<Map<String, String>> list = new ArrayList<>();

    @Override

    protected void initializeView() {
        MAP_CIMBVA.put(CIMBVA_TITLE_1, CIMBVA_CONTENT_1);
        MAP_CIMBVA.put(CIMBVA_TITLE_2, CIMBVA_CONTENT_2);
        MAP_CIMBVA.put(CIMBVA_TITLE_3, CIMBVA_CONTENT_3);
        MAP_CIMBVA.put(CIMBVA_TITLE_4, CIMBVA_CONTENT_4);
        MAP_CIMBVA.put(CIMBVA_TITLE_5, CIMBVA_CONTENT_5);

        MAP_BNIVA.put(BNIVA_TITLE_1, BNIVA_CONTENT_1);
        MAP_BNIVA.put(BNIVA_TITLE_2, BNIVA_CONTENT_2);
        MAP_BNIVA.put(BNIVA_TITLE_3, BNIVA_CONTENT_3);
        MAP_BNIVA.put(BNIVA_TITLE_4, BNIVA_CONTENT_4);
        MAP_BNIVA.put(BNIVA_TITLE_5, BNIVA_CONTENT_5);

        MAP_MANDIRI_VA.put(MandiriVA_TITLE_1, MandiriVA_CONTENT_1);
        MAP_MANDIRI_VA.put(MandiriVA_TITLE_2, MandiriVA_CONTENT_2);

        MAP_PERMATA_VA.put(PermataVA_TITLE_1, PermataVA_CONTENT_1);
        MAP_PERMATA_VA.put(PermataVA_TITLE_2, PermataVA_CONTENT_2);
        MAP_PERMATA_VA.put(PermataVA_TITLE_3, PermataVA_CONTENT_3);

        MAP_TOKO_SERBA_ADA.put(TOKO_TITLE_1, TOKO_CONTENT_1);

        expandableListView.setAdapter(new GuideExpandableLVAdapter(mContext, MAP_CIMBVA));
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                showToast(groupPosition + "|" + childPosition);
                return true;
            }
        });
    }

    @Override
    protected void loadData() {

    }

    /*CIMBVA*/
    public String CIMBVA_TITLE_1 = "ATM CIMB";
    public String CIMBVA_TITLE_2 = "CIMB CLICKS";
    public String CIMBVA_TITLE_3 = "GO MOBILE";
    public String CIMBVA_TITLE_4 = "INTERNET BANKING LAINNYA";
    public String CIMBVA_TITLE_5 = "ATM BANK LAINNYA";
    public String CIMBVA_CONTENT_1 = "<small>1. Menu Transfer <br></small> \n" +
            "<small>2. Pilih Antar Rekening CIMB NIAGA <br></small> \n" +
            "<small>3. Input nominal transfer<br></small> \n" +
            "<small>4. Input nomor virtual account di rekening tujuan<br></small> \n" +
            "<small>5. Konfirmasi dan proses pembayaran<br></small>";
    public String CIMBVA_CONTENT_2 = "<small>1. Menu Transfer <br></small> \n" +
            "<small>2. Input nominal transfer <br></small> \n" +
            "<small>3. Pilih Other Account – Bank CIMB Niaga / Rekening Ponsel<br></small> \n" +
            "<small>4. Rekening penerima diisi dengan nomor virtual account<br></small> \n" +
            "<small>5. Input mPin<br></small> \n" +
            "<small>6. Konfirmasi dan proses pembayaran<br></small>";
    public String CIMBVA_CONTENT_3 = "<small>1. Menu Transfer <br></small> \n" +
            "<small>2. Pilih Transfer to Other CIMB Niaga Account <br></small> \n" +
            "<small>3. Input nomor virtual account dan nominal pembayaran<br></small> \n" +
            "<small>4. Input PIN<br></small> \n" +
            "<small>5. Konfirmasi dan proses pembayaran<br></small>";
    public String CIMBVA_CONTENT_4 = "<small>1. Menu Transfer<br></small>\n" +
            "<small>2. Pilih Rekening Bank Lain<br></small>\n" +
            "<small>3. Input nomor virtual account pada rekening tujuan<br></small>\n" +
            "<small>4. Input nominal pembayaran<br></small>\n" +
            "<small>5.Input bank penerima dengan CIMB Niaga<br></small>\n" +
            "<small>6. Jenis transfer yang digunakan adalah online/realtime<br></small>\n" +
            "<small>7.Konfirmasi dan proses pembayaran<br></small>";
    public String CIMBVA_CONTENT_5 = "<small>1. Menu Transfer<br></small>\n" +
            "<small>2. Pilih Rekening Bank Lain<br></small>\n" +
            "<small>3. Input kode bank 022<br></small>\n" +
            "<small>4. Input nomor virtual account pada rekening tujuan<br></small>\n" +
            "<small>5. Input nominal pembayaran<br></small>\n" +
            "<small>6. Konfirmasi dan proses pembayaran<br></small>";

    /*BNIVA*/
    public String BNIVA_TITLE_1 = "ATM BNI";
    public String BNIVA_TITLE_2 = "INTERNET BANKING BNI";
    public String BNIVA_TITLE_3 = "MOBILE BANKING BNI";
    public String BNIVA_TITLE_4 = "INTERNET BANKING LAINNYA";
    public String BNIVA_TITLE_5 = "ATM BANK LAINNYA";
    public String BNIVA_CONTENT_1 = "<small>1. Menu Lainnya <br></small> \n" +
            "<small>2. Pilih Transfer <br></small> \n" +
            "<small>3. Pilih Rekening Tabungan<br></small> \n" +
            "<small>4. Pilih Ke Rekening BNI<br></small> \n" +
            "<small>5. Input nomor virtual account<br></small> \n" +
            "<small>6. Input nominal transfer<br></small> \n" +
            "<small>7. Konfirmasi pemindahbukuan</small>";
    public String BNIVA_CONTENT_2 = "<small>1. Menu Transfer <br></small> \n" +
            "<small>2. Pilih Tambah Rekening Favorit <br></small> \n" +
            "<small>3. Pilih Antar Rekening BNI<br></small> \n" +
            "<small>4. Bila menggunakan desktop untuk menambah rekening: menu Transaksi, lalu Atur Rekening Tujuan,\n" +
            "Tambah Rekening Tujuan<br></small> \n" +
            "<small>5. Masukkan nama dan nomor virtual account<br></small> \n" +
            "<small>6. Input kode otentikasi token<br></small> \n" +
            "<small>7. Rekening tujuan berhasil ditambahkan<br></small> \n" +
            "<small>8. Kembali ke menu Transfer<br></small> \n" +
            "<small>9. Pilih Transfer Antar Rekening BNI<br></small> \n" +
            "<small>10. Pilih Rekening Tujuan<br></small> \n" +
            "<small>11. Pilih rekening debet dan input nominal transfer<br></small> \n" +
            "<small>12. Input kode otentikasi token<br></small>";
    public String BNIVA_CONTENT_3 = "<small>1. Menu Transfer<br></small> \n" +
            "<small>2. Pilih Antar Rekening BNI dan Input Rekening Baru<br></small> \n" +
            "<small>3. Input rekening debet, nomor virtual account di rekening tujuan, dan nominal transfer<br></small> \n" +
            "<small>4. Input password dan konfirmasi transaksi<br></small>";
    public String BNIVA_CONTENT_4 = "<small>1. Menu Transfer<br></small> \n" +
            "<small>2. Pilih Rekening Bank Lain<br></small> \n" +
            "<small>3. Input nomor virtual account pada rekening tujuan<br></small> \n" +
            "<small>4. Input nominal pembayaran<br></small> \n" +
            "<small>5. Input bank penerima dengan BNI<br></small> \n" +
            "<small>6. Jenis transfer yang digunakan adalah online/realtime<br></small> \n" +
            "<small>7. Konfirmasi dan proses pembayaran<br></small>";
    public String BNIVA_CONTENT_5 = "<small>1. Menu Transfer<br></small>\n" +
            "<small>2. Pilih Rekening Bank Lain<br></small>\n" +
            "<small>3. Input kode bank 009<br></small>\n" +
            "<small>4. Input nomor virtual account pada rekening tujuan<br></small>\n" +
            "<small>5.Input nominal pembayaran<br></small>\n" +
            "<small>6. Konfirmasi dan proses pembayaran<br></small>";

    /*MandiriVA*/
    public String MandiriVA_TITLE_1 = "ATM MANDIRI";
    public String MandiriVA_TITLE_2 = "ONLINE BANKING";
    public String MandiriVA_CONTENT_1 = "<small>1. Pilih menu Bayar/Beli <br></small> \n" +
            "<small>2. Pilih menu Multipayment <br></small> \n" +
            "<small>3. Input kode perusahaan Bank Mandiri (5 digit terdepan kode pembayaran) tekan Benar Input nomor virtual account kemudian tekan Benar<br></small> \n" +
            "<small>4. Pilih Nomor 1 atau sesuai tagihan yang akan dibayar lalu tekan YA<br></small>";
    public String MandiriVA_CONTENT_2 = "<small>1. Input User ID & Password <br></small> \n" +
            "<small>2. Pilih menu Pembayaran <br></small> \n" +
            "<small>3. Pilih menu Multipayment<br></small> \n" +
            "<small>4. Pada Kolom Penyedia Jasa, pilih penyedia layanan yang sesuai<br></small> \n" +
            "<small>5. Input nomor virtual account<br></small> \n" +
            "<small>6. Pilih Lanjut<br></small> \n" +
            "<small>• Web : <br></small> \n" +
            "<small>a. OTP dikirimkan ke nomor HP  <br></small> \n" +
            "<small>b. OTP yang didapatkan diinput di Token untuk mendapatkan challenge code c. Chanllenge code input ke Mandiri online, lalu klik Lanjut <br></small> \n" +
            "<small>• Apps : <br> </small> \n" +
            "<small>a. Setelah muncul konfirmasi transaksi pilih Kirim <br></small> \n" +
            "<small>b. Input MPIN (6 digit) <br></small> <br>\n" +
            "<b style=\"color: #DD524D;\"> PS: Kalau kode pembayaran diawalkan dengan 70014, berarti cuma bisa bayar di produk MANDIRI </b>";

    /*PermataVA*/
    public String PermataVA_TITLE_1 = "ATM PERMATA";
    public String PermataVA_TITLE_2 = "MOBILE INTERNET";
    public String PermataVA_TITLE_3 = "ATM BERSAMA, ALTO & PRIMA";
    public String PermataVA_CONTENT_1 = "<small>1. Pilih menu Transaksi Lainnya <br></small> \n" +
            "<small>2. Pilih Pembayaran <br></small> \n" +
            "<small>3. Pilih Pembayaran Lainnya<br></small> \n" +
            "<small>4. Pilih Virtual Account<br></small> \n" +
            "<small>5. Input nomor virtual account<br></small> \n" +
            "<small>6. Pada layar akan tampil konfirmasi pembayaran</small>";
    public String PermataVA_CONTENT_2 = "<small>1. Masukan User ID & Password<br></small> \n" +
            "<small>2. Pilih Pembayaran Tagihan<br></small> \n" +
            "<small>3. Pilih virtual account<br></small> \n" +
            "<small>4. Input nomor virtual account<br></small> \n" +
            "<small>5. Input nominal pembayaran<br></small> \n" +
            "<small>6. Apabila data sudah sesuai masukan otentikasi transaksi/Token<br></small>";
    public String PermataVA_CONTENT_3 = "<small>1. Pilih menu Transfer<br></small> \n" +
            "<small>1. Pilih Transfer ke Bank Lain<br></small> \n" +
            "<small>• Jika melalui ATM Bersama & Alto :<br></small> \n" +
            "<small>a.Input kode PermataBank (013) +16 digit kode bayar<br></small> \n" +
            "<small>• Jika melalui ATM Prima :<br></small> \n" +
            "<small>a. Masukan kode PermataBank(013)tekan BENAR, dilanjutkan dengan 16 digit kode bayar<br></small> \n" +
            "<small>b. Input nominal pembayaran<br></small> \n" +
            "<small>c. Pada layar akan tampil konfirmasi Nama & Nominal Pembayaran<br></small>";

    /*Langkah pembayaran*/
    public String TOKO_TITLE_1 = "Toko serba ada";
    public String TOKO_CONTENT_1 = "1. Sebutkan mau bayar ke Finpay021, lalu kasih kasir pay code\n" +
            "<br/>\n" +
            "2. Jika tidak dapat Finpay021, sebutkan mau bayar ke telkom/telefon, kemudian pilih speedy/indihome, kemudian kasih kasir pay code\n" +
            "<br/>\n" +
            "3. Kasir akan menanyakan kode pembayaran. Berikan kode pembayaran anda. Kasir akanmelakukan konfirmasi data konsumen berupa Nama Merchant, Nama Konsumen,\n" +
            "danNominal. Lakukan pembayaran ke kasir sejumlah nominal yang disebutkan\n" +
            "<br/>\n" +
            "4. Terima struk sebagai bukti pembayaran sudah sukses dilakukan. Notifikasi pembayaranakan langsung diterima oleh Merchant\n" +
            "<br/>\n" +
            "5. Jika tidak bisa dapatkan saluran tersebut di saluran membayar, silakan coba cari \"wayangpay\" atau \"wayangpay via nicepay\" dan ulangi operasi tersebut\n" +
            "<br/>\n" +
            "6. Selesai\n" +
            "<br/>";
}
