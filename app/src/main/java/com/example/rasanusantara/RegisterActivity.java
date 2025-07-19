package com.example.rasanusantara;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // Deklarasi komponen input dan tombol
    EditText etUsername, etEmail, etPassword;
    Button btnDaftar;
    private ImageButton btnBack;
    // Objek database helper
    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi komponen dari layout
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        btnBack = findViewById(R.id.btnBack);

        // Inisialisasi database helper
        db = new SQLiteHelper(this);

        // Listener tombol "Daftar" saat diklik
        btnDaftar.setOnClickListener(view -> {
            // Ambil input dari user
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi apakah ada field yang kosong
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // Panggil fungsi register di SQLiteHelper
                boolean success = db.registerUser(username, email, password);

                if (success) {
                    // Jika berhasil, tampilkan toast dan kembali ke halaman login
                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup halaman register
                } else {
                    // Jika gagal (misalnya username sudah digunakan), tampilkan pesan
                    Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack.setOnClickListener(v -> finish());
    }
}
