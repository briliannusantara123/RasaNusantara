package com.example.rasanusantara;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class TentangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // Set item "Tentang" sebagai yang dipilih saat ini
        bottomNav.setSelectedItemId(R.id.footer_tentang);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.footer_home) {
                item.setChecked(true); // Tandai sebagai terpilih
                startActivity(new Intent(this, ResepActivity.class));
                overridePendingTransition(0, 0); // Opsional: tanpa animasi
                return true;
            } else if (id == R.id.footer_tentang) {
                item.setChecked(true); // Sudah di sini, tandai saja
                return true;
            }

            return false;
        });
    }
}
