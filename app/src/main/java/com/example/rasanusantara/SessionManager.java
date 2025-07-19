package com.example.rasanusantara;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class ini digunakan untuk mengelola sesi login user menggunakan SharedPreferences.
 */
public class SessionManager {
    // Nama SharedPreferences
    private static final String PREF_NAME = "user_session";

    // Key untuk menyimpan data user
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    // Deklarasi SharedPreferences dan Editor-nya
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /**
     * Konstruktor untuk inisialisasi SharedPreferences.
     * @param context Context aplikasi
     */
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Menyimpan sesi login ke SharedPreferences.
     * @param userId ID user dari database
     * @param username Username yang digunakan untuk login
     */
    public void createLoginSession(int userId, String username) {
        editor.putInt(KEY_USER_ID, userId);        // Simpan user ID
        editor.putString(KEY_USERNAME, username);  // Simpan username
        editor.apply();                            // Terapkan perubahan
    }

    /**
     * Mengambil ID user dari sesi login.
     * @return ID user, atau -1 jika belum login
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1); // -1 berarti tidak ditemukan / belum login
    }

    /**
     * Mengambil username dari sesi login.
     * @return Username user, atau "Guest" jika belum login
     */
    public String getUsername() {
        return pref.getString(KEY_USERNAME, "Guest");
    }

    /**
     * Menghapus semua data sesi login (logout).
     */
    public void logoutUser() {
        editor.clear();   // Hapus semua data di SharedPreferences
        editor.apply();   // Terapkan perubahan
    }

    /**
     * Mengecek apakah user sudah login.
     * @return true jika sudah login, false jika belum
     */
    public boolean isLoggedIn() {
        return pref.contains(KEY_USER_ID); // Jika user ID tersimpan, berarti user sudah login
    }
}
