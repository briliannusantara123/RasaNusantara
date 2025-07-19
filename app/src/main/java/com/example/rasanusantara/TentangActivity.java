package com.example.rasanusantara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class TentangActivity extends AppCompatActivity {

    // Deklarasi tombol logout
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        // Inisialisasi tombol logout dari layout
        btnLogout = findViewById(R.id.btnLogout);

        // Event klik tombol logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(); // Panggil fungsi logout
            }
        });

        // Inisialisasi BottomNavigationView dan atur navigasi berdasarkan item yang dipilih
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.footer_tentang); // Tandai item yang aktif saat ini

        // Listener untuk item navigasi bawah
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.footer_home) {
                // Navigasi ke halaman Resep (Home)
                startActivity(new Intent(this, ResepActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.footer_fav) {
                // Navigasi ke halaman Favorit
                startActivity(new Intent(this, ResepFavoritActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return itemId == R.id.footer_tentang;
        });
    }

    /**
     * Fungsi logout untuk menghapus session user
     * dan kembali ke halaman login.
     */
    private void logout() {
        // Hapus semua data dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Arahkan kembali ke LoginActivity dan hapus task stack sebelumnya
        Intent intent = new Intent(TentangActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Tutup activity saat ini
    }
}
