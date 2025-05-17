package com.example.rasanusantara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // Tandai item "home" sebagai yang dipilih
        bottomNav.setSelectedItemId(R.id.footer_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.footer_home) {
                // Sudah di halaman ini, jadi hanya tandai checked
                item.setChecked(true);
                return true;
            } else if (id == R.id.footer_tentang) {
                item.setChecked(true); // Tandai checked sebelum pindah
                startActivity(new Intent(this, TentangActivity.class));
                overridePendingTransition(0, 0); // Opsional: hilangkan animasi transisi
                return true;
            }

            return false;
        });

        // Scroll ke bagian tertentu saat gambar diklik
        ImageView imgSunda = findViewById(R.id.imgSunda);
        ImageView imgPadang = findViewById(R.id.imgPadang);
        ImageView imgJawa = findViewById(R.id.imgJawa);
        ImageView imgBetawi = findViewById(R.id.imgBetawi);
        ImageView imgBali = findViewById(R.id.imgBali);

        NestedScrollView mainScroll = findViewById(R.id.mainScroll);
        View sectionSunda = findViewById(R.id.sectionSunda);
        View sectionPadang = findViewById(R.id.sectionPadang);
        View sectionJawa = findViewById(R.id.sectionJawa);
        View sectionBetawi = findViewById(R.id.sectionBetawi);
        View sectionBali = findViewById(R.id.sectionBali);

        imgSunda.setOnClickListener(v -> mainScroll.smoothScrollTo(0, sectionSunda.getTop()));
        imgPadang.setOnClickListener(v -> mainScroll.smoothScrollTo(0, sectionPadang.getTop()));
        imgJawa.setOnClickListener(v -> mainScroll.smoothScrollTo(0, sectionJawa.getTop()));
        imgBetawi.setOnClickListener(v -> mainScroll.smoothScrollTo(0, sectionBetawi.getTop()));
        imgBali.setOnClickListener(v -> mainScroll.smoothScrollTo(0, sectionBali.getTop()));

        LinearLayout nasitimbeltop = findViewById(R.id.nasitimbeltop);
        LinearLayout nasitimbel = findViewById(R.id.nasitimbel);
        LinearLayout rendangtop = findViewById(R.id.rendangtop);
        LinearLayout gudegtoptop = findViewById(R.id.gudegtop);
        LinearLayout sotobetawitop = findViewById(R.id.sotobetawitop);
        LinearLayout sayurasem = findViewById(R.id.sayurasem);

// Listener untuk Nasi Timbel (pakai untuk nasitimbeltop dan nasitimbel)
        View.OnClickListener klikNasiTimbel = v -> {
            Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
            intent.putExtra("imageResId", R.drawable.nasi_timbel);
            intent.putExtra("title", "Nasi Timbel");
            intent.putExtra("bahan", "1. Ayam Goreng / Ikan Asin / Tahu Tempe:\n" +
                    "  - 1 ekor ayam potong ikan asin,tahu,atau tempe\n" +
                    "  - Bumbu ungkep(bawang putih,ketumbar,kunyit,garam)\n" +
                    "2. Sambal Terasi:\n" +
                    "  - 10 buah cabai merah keriting\n" +
                    "  - 5 buah cabai rawit merah (sesuai selera)\n" +
                    "  - 1 sdt terasi (dibakar)\n" +
                    "  - 1 buah tomat merah\n" +
                    "  - 1 siung bawang putih\n" +
                    "  - 2 siung bawang merah\n" +
                    "  - Garam, gula secukupnya\n" +
                    "  - Minyak goreng secukupnya\n" +
                    "3. Lalapan:\n" +
                    "  - Daun kemangi\n" +
                    "  - Mentimun\n" +
                    "  - Kol\n" +
                    "  - Tomat\n" +
                    "  - Daun selada");
            intent.putExtra("langkah", "1. Siapkan Nasi :\n" +
                    "  - Cuci beras hingga bersih (2 gelas beras).\n" +
                    "  - Masak nasi seperti biasa menggunakan rice cooker atau kukusan. Jika ingin lebih wangi, bisa tambahkan sedikit daun pandan saat menanak.\n" +
                    "  - Setelah matang, diamkan nasi selama 10–15 menit agar uapnya berkurang dan teksturnya lebih padat.\n" +
                    "2. Siapkan Daun Pisang :\n" +
                    "  - Layukan daun pisang di atas api kompor sebentar agar lentur dan tidak mudah sobek saat dibungkus.\n" +
                    "  - Lap daun dengan kain bersih untuk memastikan tidak ada kotoran.\n" +
                    "3. Bungkus Nasi :\n" +
                    "  - Ambil nasi hangat secukupnya, taruh di atas daun pisang, bentuk memanjang seperti lontong.\n" +
                    "  - Gulung daun pisang, rapatkan kedua ujungnya, dan bisa disemat dengan tusuk gigi.\n" +
                    "  - Kukus kembali nasi yang sudah dibungkus selama ±10 menit agar aromanya meresap ke dalam nasi.\n");
            intent.putExtra("description","Nasi Timbel adalah hidangan khas Sunda yang disajikan dengan nasi putih panas yang dibungkus daun pisang. Proses pembungkusan ini memberikan aroma khas yang harum dan alami. Biasanya, nasi timbel disajikan dengan aneka lauk seperti ayam goreng, tahu, tempe, ikan asin, sambal, lalapan segar (mentimun, kemangi, tomat), dan kadang-kadang ditambah sayur asem.");
            startActivity(intent);
        };

// Listener untuk Rendang
        View.OnClickListener klikRendang = v -> {
            Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
            intent.putExtra("imageResId", R.drawable.rendang);
            intent.putExtra("title", "Rendang Sapi");
            intent.putExtra("bahan", "1. Daging sapi\n" +
                    "2. Santan\n" +
                    "3. Bumbu rempah seperti serai, lengkuas, daun jeruk\n" +
                    "4. Cabe merah\n" +
                    "5. Bawang merah dan bawang putih\n" +
                    "6. Garam dan gula secukupnya");
            intent.putExtra("langkah", "1. Potong-potong daging sapi.\n" +
                    "2. Haluskan bumbu rempah.\n" +
                    "3. Tumis bumbu hingga harum.\n" +
                    "4. Masukkan daging dan santan.\n" +
                    "5. Masak dengan api kecil hingga kuah mengering dan daging empuk.");
            intent.putExtra("description","Rendang adalah masakan khas Padang yang terbuat dari daging sapi yang dimasak dengan santan dan berbagai bumbu rempah hingga kering dan empuk.");
            startActivity(intent);
        };

// Listener untuk Gudeg
        View.OnClickListener klikGudeg = v -> {
            Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
            intent.putExtra("imageResId", R.drawable.gudeg);
            intent.putExtra("title", "Gudeg");
            intent.putExtra("bahan", "1. Nangka muda\n" +
                    "2. Santan\n" +
                    "3. Daun jati\n" +
                    "4. Gula merah\n" +
                    "5. Bawang merah dan bawang putih\n" +
                    "6. Lengkuas dan serai");
            intent.putExtra("langkah", "1. Rebus nangka muda hingga empuk.\n" +
                    "2. Tumis bumbu halus.\n" +
                    "3. Masukkan nangka dan santan.\n" +
                    "4. Masak dengan api kecil hingga santan menyusut dan warna coklat tua.\n" +
                    "5. Tambahkan daun jati untuk warna.");
            intent.putExtra("description","Gudeg adalah makanan khas Yogyakarta yang terbuat dari nangka muda dimasak dengan santan dan gula merah.");
            startActivity(intent);
        };

// Listener untuk Soto Betawi
        View.OnClickListener klikSotoBetawi = v -> {
            Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
            intent.putExtra("imageResId", R.drawable.sotobetawi);
            intent.putExtra("title", "Soto Betawi");
            intent.putExtra("bahan", "1. Daging sapi\n" +
                    "2. Santan\n" +
                    "3. Bumbu soto (bawang putih, bawang merah, kemiri, jahe)\n" +
                    "4. Tomat\n" +
                    "5. Kentang goreng\n" +
                    "6. Daun bawang dan seledri");
            intent.putExtra("langkah", "1. Rebus daging hingga empuk.\n" +
                    "2. Tumis bumbu soto hingga harum.\n" +
                    "3. Masukkan santan dan daging.\n" +
                    "4. Tambahkan tomat dan kentang goreng.\n" +
                    "5. Sajikan dengan daun bawang dan seledri.");
            intent.putExtra("description","Soto Betawi adalah soto khas Jakarta dengan kuah santan dan isian daging sapi serta kentang goreng.");
            startActivity(intent);
        };

// Listener untuk Sayur Asem
        View.OnClickListener klikSayurAsem = v -> {
            Intent intent = new Intent(ResepActivity.this, DetailResepActivity.class);
            intent.putExtra("imageResId", R.drawable.sayur_asem);
            intent.putExtra("title", "Sayur Asem");
            intent.putExtra("bahan", "1. Jagung manis\n" +
                    "2. Labu siam\n" +
                    "3. Kacang panjang\n" +
                    "4. Melinjo dan daun melinjo\n" +
                    "5. Asam jawa\n" +
                    "6. Bumbu dasar (bawang merah, bawang putih, cabai)");
            intent.putExtra("langkah", "1. Rebus bumbu dasar hingga harum.\n" +
                    "2. Masukkan sayuran satu per satu sesuai waktu matang.\n" +
                    "3. Tambahkan asam jawa dan garam.\n" +
                    "4. Masak hingga sayuran matang.");
            intent.putExtra("description","Sayur Asem adalah sup sayur segar dengan rasa asam yang menyegarkan, khas masakan Indonesia.");
            startActivity(intent);
        };

// Pasang listener ke masing-masing LinearLayout
        nasitimbeltop.setOnClickListener(klikNasiTimbel);
        nasitimbel.setOnClickListener(klikNasiTimbel);
        rendangtop.setOnClickListener(klikRendang);
        gudegtoptop.setOnClickListener(klikGudeg);
        sotobetawitop.setOnClickListener(klikSotoBetawi);
        sayurasem.setOnClickListener(klikSayurAsem);




    }
}
