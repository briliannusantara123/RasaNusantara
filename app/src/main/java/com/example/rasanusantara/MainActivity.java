package com.example.rasanusantara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ambil tombol dari layout
        Button btnLihatResep = findViewById(R.id.btnLihatResep);

        // Tambahkan aksi ketika tombol ditekan
        btnLihatResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke ResepActivity
                Intent intent = new Intent(MainActivity.this, ResepActivity.class);
                startActivity(intent);
            }
        });
    }
}
