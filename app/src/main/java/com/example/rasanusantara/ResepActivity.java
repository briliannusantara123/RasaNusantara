package com.example.rasanusantara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.*;

public class ResepActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private LinearLayout containerKategori;
    private LinearLayout containerSubKategori;
    private NestedScrollView scrollView;

    // Menyimpan header view dari setiap kategori untuk keperluan scroll
    private Map<String, View> kategoriHeaderMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        // Ambil username dari SharedPreferences untuk ditampilkan di greeting
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        tvGreeting.setText("Hi " + username);

        // Inisialisasi database dan elemen-elemen layout
        dbHelper = new SQLiteHelper(this);
        containerKategori = findViewById(R.id.containerKategori);
        containerSubKategori = findViewById(R.id.containerSubKategori);
        scrollView = findViewById(R.id.mainScroll);

        // Tombol tambah resep
        Button tambahResep = findViewById(R.id.btnTambahResep);
        tambahResep.setOnClickListener(v ->
                startActivity(new Intent(this, TambahResepActivity.class))
        );

        // Bottom Navigation setup
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.footer_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.footer_home) {
                return true; // Sudah di halaman ini
            } else if (itemId == R.id.footer_tentang) {
                startActivity(new Intent(this, TentangActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.footer_fav) {
                startActivity(new Intent(this, ResepFavoritActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Menampilkan ulang daftar resep saat kembali dari halaman lain
        tampilkanResepDariDatabase();
    }

    /**
     * Fungsi untuk mengambil semua resep dari database dan menampilkannya
     * secara dinamis berdasarkan kategori (asal daerah).
     */
    private void tampilkanResepDariDatabase() {
        // Bersihkan kontainer sebelumnya
        containerKategori.removeAllViews();
        containerSubKategori.removeAllViews();
        kategoriHeaderMap.clear();

        List<Resep> resepList = dbHelper.getSemuaResep();
        LayoutInflater inflater = LayoutInflater.from(this);

        // Kelompokkan resep berdasarkan asal/kategori
        Map<String, List<Resep>> mapKategori = new LinkedHashMap<>();
        for (Resep resep : resepList) {
            String asal = resep.getAsal().trim();
            mapKategori.computeIfAbsent(asal, k -> new ArrayList<>()).add(resep);
        }

        // Buat tombol kategori horizontal
        for (String kategori : mapKategori.keySet()) {
            Button btnKategori = new Button(this);
            btnKategori.setText(kategori);
            btnKategori.setTextSize(14f);
            btnKategori.setAllCaps(false);
            btnKategori.setBackgroundResource(R.drawable.button_kategori_background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(280, 200);
            params.setMargins(8, 15, 8, 15);
            btnKategori.setLayoutParams(params);

            // Scroll ke bagian kategori saat tombol diklik
            btnKategori.setOnClickListener(v -> {
                View headerView = kategoriHeaderMap.get(kategori);
                if (headerView != null) {
                    scrollView.post(() -> scrollView.scrollTo(0, headerView.getTop()));
                } else {
                    Toast.makeText(this, "Kategori tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            });

            containerKategori.addView(btnKategori);
        }

        // Tampilkan resep-resep berdasarkan kategori
        for (Map.Entry<String, List<Resep>> entry : mapKategori.entrySet()) {
            String kategori = entry.getKey();
            List<Resep> daftarResep = entry.getValue();

            // Header nama kategori
            TextView header = new TextView(this);
            header.setText(kategori);
            header.setTextSize(18);
            header.setTypeface(null, Typeface.BOLD);
            header.setPadding(0, 24, 0, 8);
            containerSubKategori.addView(header);
            kategoriHeaderMap.put(kategori, header);

            // Kontainer resep-resep dalam kategori ini
            LinearLayout containerResep = new LinearLayout(this);
            containerResep.setOrientation(LinearLayout.VERTICAL);
            containerSubKategori.addView(containerResep);

            // Tampilkan setiap resep
            for (Resep resep : daftarResep) {
                View cardView = inflater.inflate(R.layout.item_resep_card, containerResep, false);

                ImageView img = cardView.findViewById(R.id.imgResep);
                TextView title = cardView.findViewById(R.id.txtJudul);
                TextView asal = cardView.findViewById(R.id.txtAsal);
                ImageView btnDelete = cardView.findViewById(R.id.btnDelete);
                ImageView btnFav = cardView.findViewById(R.id.btnFavorite);

                // Set data resep
                title.setText(resep.getNama());
                asal.setText(resep.getAsal());

                // Tampilkan gambar dari blob
                byte[] fotoBytes = resep.getFotoBlob();
                if (fotoBytes != null && fotoBytes.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                    img.setImageBitmap(bitmap);
                } else {
                    img.setImageResource(R.drawable.logo);
                }

                // Tampilkan status favorit
                if (resep.getIsFav() == 1) {
                    btnFav.setImageResource(R.drawable.ic_star);
                } else {
                    btnFav.setImageResource(R.drawable.ic_star_border);
                }

                // Tangani klik favorit
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);

                btnFav.setOnClickListener(v -> {
                    if (userId != -1) {
                        long result = dbHelper.tambahFavorit(userId, resep.getId());
                        if (result != -1) {
                            Toast.makeText(ResepActivity.this, "Berhasil Menambahkan " + resep.getNama() + " ke Favorit", Toast.LENGTH_SHORT).show();
                            tampilkanResepDariDatabase(); // Refresh tampilan
                        } else {
                            Toast.makeText(ResepActivity.this, "Resep sudah ada di favorit", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ResepActivity.this, "Login terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                });

                // Buka detail resep saat card diklik
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
                    intent.putExtra("nama", resep.getNama());
                    intent.putExtra("deskripsi", resep.getDeskripsi());
                    intent.putExtra("bahan", resep.getBahan());
                    intent.putExtra("langkah", resep.getLangkah());
                    intent.putExtra("foto", resep.getFotoBlob());
                    startActivity(intent);
                });

                // Tangani tombol hapus resep
                btnDelete.setOnClickListener(v -> {
                    new androidx.appcompat.app.AlertDialog.Builder(ResepActivity.this)
                            .setTitle("Konfirmasi Hapus")
                            .setMessage("Apakah Anda yakin ingin menghapus resep ini?")
                            .setPositiveButton("Hapus", (dialog, which) -> {
                                if (dbHelper.hapusResepById(resep.getId())) {
                                    Toast.makeText(ResepActivity.this, "Resep berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    tampilkanResepDariDatabase(); // Refresh tampilan
                                }
                            })
                            .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                            .show();
                });

                containerResep.addView(cardView);
            }
        }
    }
}
