package com.example.rasanusantara;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;
import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Resep.db";
    private static final int DATABASE_VERSION = 7; // Ubah versi agar onUpgrade() terpanggil

    public static final String TABLE_RESEP = "resep";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_ASAL = "asal";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_BAHAN = "bahan";
    public static final String COLUMN_LANGKAH = "langkah";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_IS_FAV = "is_fav";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        buatSemuaTabel(db);
    }

    // Membuat semua tabel yang diperlukan
    private void buatSemuaTabel(SQLiteDatabase db) {
        // Tabel resep
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RESEP + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAMA + " TEXT," +
                COLUMN_ASAL + " TEXT," +
                COLUMN_DESKRIPSI + " TEXT," +
                COLUMN_BAHAN + " TEXT," +
                COLUMN_LANGKAH + " TEXT," +
                COLUMN_FOTO + " BLOB," +
                COLUMN_IS_FAV + " INTEGER DEFAULT 0)");

        // Tabel user
        db.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT," +
                "email TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // Tabel favorit
        db.execSQL("CREATE TABLE IF NOT EXISTS favorit (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "resep_id INTEGER NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(user_id, resep_id)," +
                "FOREIGN KEY(user_id) REFERENCES user(id)," +
                "FOREIGN KEY(resep_id) REFERENCES resep(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Untuk upgrade versi database, hapus tabel lama dan buat ulang
        db.execSQL("DROP TABLE IF EXISTS favorit");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESEP);
        onCreate(db);
    }

    // Menambahkan resep baru ke database
    public void tambahResep(String nama, String asal, String deskripsi, String bahan, String langkah, byte[] fotoBlob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_ASAL, asal);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_BAHAN, bahan);
        values.put(COLUMN_LANGKAH, langkah);
        values.put(COLUMN_FOTO, fotoBlob);
        db.insert(TABLE_RESEP, null, values);
        db.close();
    }

    // Mengambil semua data resep dari database
    public List<Resep> getSemuaResep() {
        List<Resep> resepList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESEP, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                String asal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ASAL));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
                String bahan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BAHAN));
                String langkah = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGKAH));
                byte[] fotoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOTO));

                resepList.add(new Resep(id, nama, asal, deskripsi, bahan, langkah, fotoBytes));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return resepList;
    }

    // Menghapus resep berdasarkan ID
    public boolean hapusResepById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_RESEP, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Mengupdate status favorit dari sebuah resep
    public void updateIsFav(int resepId, int isFav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAV, isFav);
        db.update(TABLE_RESEP, values, COLUMN_ID + " = ?", new String[]{String.valueOf(resepId)});
        db.close();
    }

    // ==================== USER ====================

    // Mendaftarkan user baru
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM user WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // Username sudah digunakan
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("user", null, values);
        return result != -1;
    }

    // Melakukan login user, mengembalikan userId jika berhasil, -1 jika gagal
    public int loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM user WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    // ==================== FAVORIT ====================

    // Menambahkan resep ke daftar favorit user
    public long tambahFavorit(int userId, int resepId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("resep_id", resepId);
        long result = db.insertWithOnConflict("favorit", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return result;
    }

    // Menghapus resep dari daftar favorit user
    public int hapusFavorit(int userId, int resepId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("favorit", "user_id=? AND resep_id=?", new String[]{String.valueOf(userId), String.valueOf(resepId)});
        db.close();
        return result;
    }

    // Mengecek apakah suatu resep sudah difavoritkan oleh user
    public boolean isFavorit(int userId, int resepId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM favorit WHERE user_id=? AND resep_id=?", new String[]{String.valueOf(userId), String.valueOf(resepId)});
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }

    // Mengambil daftar resep favorit berdasarkan user ID
    public List<Resep> getResepFavoritByUser(int userId) {
        List<Resep> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.* FROM " + TABLE_RESEP + " r " +
                "JOIN favorit f ON r.id = f.resep_id WHERE f.user_id=?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                String asal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ASAL));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
                String bahan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BAHAN));
                String langkah = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGKAH));
                byte[] foto = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FOTO));

                list.add(new Resep(id, nama, asal, deskripsi, bahan, langkah, foto));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Menghapus favorit berdasarkan resep dan user ID
    public boolean hapusFavoritByResepId(int userId, int resepId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("favorit", "user_id=? AND resep_id=?", new String[]{String.valueOf(userId), String.valueOf(resepId)});
        return result > 0;
    }
}
