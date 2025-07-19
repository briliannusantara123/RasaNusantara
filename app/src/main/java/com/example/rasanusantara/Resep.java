package com.example.rasanusantara;

/**
 * Model class untuk merepresentasikan data Resep dalam aplikasi.
 */
public class Resep {
    // Properti resep
    private int id;
    private String nama;
    private String asal;
    private String deskripsi;
    private String bahan;
    private String langkah;
    private byte[] fotoBlob;  // Foto dalam bentuk blob (byte array)
    private int isFav;        // Penanda apakah resep difavoritkan (1 = ya, 0 = tidak)

    /**
     * Konstruktor untuk membuat objek Resep.
     * @param id ID unik dari resep
     * @param nama Nama resep
     * @param asal Asal daerah resep
     * @param deskripsi Deskripsi singkat resep
     * @param bahan Daftar bahan resep
     * @param langkah Langkah-langkah pembuatan
     * @param fotoBlob Gambar resep dalam bentuk byte[]
     */
    public Resep(int id, String nama, String asal, String deskripsi, String bahan, String langkah, byte[] fotoBlob) {
        this.id = id;
        this.nama = nama;
        this.asal = asal;
        this.deskripsi = deskripsi;
        this.bahan = bahan;
        this.langkah = langkah;
        this.fotoBlob = fotoBlob;
    }

    // Getter untuk mendapatkan nilai properti dari objek Resep

    /**
     * Mengembalikan ID resep.
     */
    public int getId() {
        return id;
    }

    /**
     * Mengembalikan nama resep.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengembalikan asal daerah resep.
     */
    public String getAsal() {
        return asal;
    }

    /**
     * Mengembalikan deskripsi resep.
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * Mengembalikan daftar bahan-bahan resep.
     */
    public String getBahan() {
        return bahan;
    }

    /**
     * Mengembalikan langkah-langkah pembuatan resep.
     */
    public String getLangkah() {
        return langkah;
    }

    /**
     * Mengembalikan gambar resep dalam bentuk byte[].
     */
    public byte[] getFotoBlob() {
        return fotoBlob;
    }

    /**
     * Mengembalikan status favorit (1 = favorit, 0 = tidak).
     */
    public int getIsFav() {
        return isFav;
    }

    /**
     * Menetapkan status favorit pada resep.
     * @param isFav 1 jika favorit, 0 jika tidak
     */
    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }
}
