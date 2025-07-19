package com.example.rasanusantara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ResepFavoritActivity extends AppCompatActivity {

    LinearLayout containerResepFavorit;
    SQLiteHelper db;
    SessionManager sessionManager;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep_favorit);

        // Inisialisasi komponen UI dan helper
        containerResepFavorit = findViewById(R.id.containerResepFavorit);
        db = new SQLiteHelper(this); // akses database
        sessionManager = new SessionManager(this); // ambil session user
        inflater = LayoutInflater.from(this); // inflater untuk layout item

        // Ambil userId dari session
        int userId = sessionManager.getUserId();

        // Tampilkan daftar resep favorit berdasarkan userId
        tampilkanResepFavorit(userId);

        // Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.footer_fav); // Set item aktif

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.footer_home) {
                startActivity(new Intent(this, ResepActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.footer_tentang) {
                startActivity(new Intent(this, TentangActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.footer_fav) {
                return true;
            }
            return false;
        });
    }

    /**
     * Menampilkan resep favorit yang dimiliki oleh user tertentu
     * @param userId ID pengguna yang sedang login
     */
    private void tampilkanResepFavorit(int userId) {
        containerResepFavorit.removeAllViews(); // Bersihkan tampilan lama

        // Ambil semua resep favorit user dari database
        List<Resep> resepFavoritList = db.getResepFavoritByUser(userId);

        // Loop setiap resep favorit dan tampilkan
        for (Resep resep : resepFavoritList) {
            // Inflate layout item resep favorit
            View cardView = inflater.inflate(R.layout.item_resep, containerResepFavorit, false);

            // Ambil referensi view di layout
            ImageView img = cardView.findViewById(R.id.imgResep);
            TextView title = cardView.findViewById(R.id.txtJudul);
            TextView asal = cardView.findViewById(R.id.txtAsal);
            ImageView btnDelete = cardView.findViewById(R.id.btnDelete);
            ImageView btnFav = cardView.findViewById(R.id.btnFavorite);

            // Set data resep ke tampilan
            title.setText(resep.getNama());
            asal.setText(resep.getAsal());

            // Tampilkan gambar jika ada, jika tidak tampilkan default
            byte[] fotoBytes = resep.getFotoBlob();
            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                img.setImageBitmap(bitmap);
            } else {
                img.setImageResource(R.drawable.logo); // default image
            }

            // Klik tombol favorit: mencoba menambahkan ulang (meskipun biasanya sudah difavoritkan)
            btnFav.setOnClickListener(v -> {
                if (userId != -1) {
                    long result = db.tambahFavorit(userId, resep.getId());
                    if (result != -1) {
                        Toast.makeText(ResepFavoritActivity.this, "Berhasil Menambahkan " + resep.getNama() + " ke Favorit", Toast.LENGTH_SHORT).show();
                        tampilkanResepFavorit(userId); // Refresh daftar
                    } else {
                        Toast.makeText(ResepFavoritActivity.this, "Resep sudah ada di favorit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResepFavoritActivity.this, "Login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            });

            // Klik item: buka detail resep
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(ResepFavoritActivity.this, DetailResepActivity.class);
                intent.putExtra("nama", resep.getNama());
                intent.putExtra("deskripsi", resep.getDeskripsi());
                intent.putExtra("bahan", resep.getBahan());
                intent.putExtra("langkah", resep.getLangkah());
                intent.putExtra("foto", resep.getFotoBlob());
                startActivity(intent);
            });

            // Klik tombol delete: konfirmasi penghapusan dari favorit
            btnDelete.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(ResepFavoritActivity.this)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Apakah Anda yakin ingin menghapus resep ini dari favorit?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            if (db.hapusFavoritByResepId(userId, resep.getId())) {
                                Toast.makeText(ResepFavoritActivity.this, "Resep dihapus dari favorit", Toast.LENGTH_SHORT).show();
                                tampilkanResepFavorit(userId); // Refresh tampilan
                            } else {
                                Toast.makeText(ResepFavoritActivity.this, "Gagal menghapus resep dari favorit", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                        .show();
            });

            // Tambahkan cardView ke container
            containerResepFavorit.addView(cardView);
        }
    }
}
