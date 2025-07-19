package com.example.rasanusantara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TambahResepActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private Uri imageUri;

    private EditText edtNama, edtDeskripsi, edtBahan, edtLangkah;
    private Spinner spinnerAsal;
    private ImageView previewImage;
    private Button btnPilihFoto, btnSimpan;

    // Launcher untuk memilih gambar dari galeri
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        previewImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_resep);

        dbHelper = new SQLiteHelper(this);

        // Inisialisasi komponen view
        edtNama = findViewById(R.id.etNamaMasakan);
        edtBahan = findViewById(R.id.edtBahan);
        edtLangkah = findViewById(R.id.edtLangkah);
        spinnerAsal = findViewById(R.id.spinnerAsalMasakan);
        edtDeskripsi = findViewById(R.id.etDeskripsi);
        previewImage = findViewById(R.id.previewImage);
        btnPilihFoto = findViewById(R.id.btnPilihFoto);
        btnSimpan = findViewById(R.id.btnSimpanResep);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Tombol kembali ke activity sebelumnya
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Mengisi spinner dengan pilihan asal masakan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.asal_masakan_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAsal.setAdapter(adapter);

        // Aksi tombol pilih foto
        btnPilihFoto.setOnClickListener(v -> openImagePicker());

        // Aksi tombol simpan resep
        btnSimpan.setOnClickListener(v -> {
            String nama = edtNama.getText().toString().trim();
            String asal = spinnerAsal.getSelectedItem().toString().trim();
            String deskripsi = edtDeskripsi.getText().toString().trim();
            String bahan = edtBahan.getText().toString().trim();
            String langkah = edtLangkah.getText().toString().trim();

            // Validasi input
            if (nama.isEmpty() || deskripsi.isEmpty() || bahan.isEmpty() || langkah.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri == null) {
                Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke database
            try {
                byte[] fotoBytes = getBytesFromUri(imageUri);
                dbHelper.tambahResep(nama, asal, deskripsi, bahan, langkah, fotoBytes);
                Toast.makeText(this, "Resep berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Membuka galeri untuk memilih gambar
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Pilih Gambar"));
    }

    // Mengubah URI gambar menjadi array byte agar bisa disimpan ke database
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream iStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}
