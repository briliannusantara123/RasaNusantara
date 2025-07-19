package com.example.rasanusantara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Komponen input dan tombol
    EditText etUsername, etPassword;
    Button btnLogin;

    // Objek helper database dan session
    SQLiteHelper dbHelper;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi view dari layout
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Inisialisasi database helper dan session manager
        dbHelper = new SQLiteHelper(this);
        sessionManager = new SessionManager(this); // Untuk menyimpan data login di SharedPreferences

        // Tombol teks untuk pindah ke halaman registrasi
        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(view -> {
            // Intent untuk membuka halaman registrasi
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Tombol login diklik
        btnLogin.setOnClickListener(v -> {
            // Ambil input username dan password
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input kosong
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek ke database apakah username & password valid
            int userId = dbHelper.loginUser(username, password);

            if (userId != -1) {
                // Jika berhasil, simpan session login
                sessionManager.createLoginSession(userId, username);

                // Pindah ke halaman utama (ResepActivity)
                startActivity(new Intent(LoginActivity.this, ResepActivity.class));
                finish(); // Tutup halaman login agar tidak bisa dikembalikan dengan tombol back
            } else {
                // Gagal login, tampilkan pesan kesalahan
                Toast.makeText(this, "Login gagal. Username atau password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
