package com.example.rasanusantara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailResepActivity extends AppCompatActivity {

    // Deklarasi komponen UI
    private ImageView detailImage;
    private TextView detailText, description, bahan, langkah;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep);

        // Inisialisasi komponen UI dari layout
        detailImage = findViewById(R.id.detailImage);
        detailText = findViewById(R.id.detailText);
        description = findViewById(R.id.description);
        bahan = findViewById(R.id.bahan);
        langkah = findViewById(R.id.langkah);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data resep dari intent yang dikirim dari activity sebelumnya
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        String bahanText = intent.getStringExtra("bahan");
        String langkahText = intent.getStringExtra("langkah");
        byte[] fotoBlob = intent.getByteArrayExtra("foto");

        // Tampilkan data nama dan deskripsi ke tampilan
        detailText.setText(nama);
        description.setText(deskripsi);

        // Tampilkan bahan resep, jika null maka tampilkan default
        bahan.setText(bahanText != null ? bahanText : "Tidak ada bahan.");

        // Tampilkan langkah resep, jika null maka tampilkan default
        langkah.setText(langkahText != null ? langkahText : "Tidak ada langkah.");

        // Jika ada gambar (dalam bentuk byte array), decode dan tampilkan
        if (fotoBlob != null && fotoBlob.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBlob, 0, fotoBlob.length);
            detailImage.setImageBitmap(bitmap);
        } else {
            // Jika gambar kosong, tampilkan gambar default dan log error
            Log.e("DETAIL_RESEP", "Foto kosong atau null.");
            detailImage.setImageResource(R.drawable.logo);
        }

        // Aksi tombol kembali ke halaman sebelumnya
        btnBack.setOnClickListener(v -> finish());
    }
}
