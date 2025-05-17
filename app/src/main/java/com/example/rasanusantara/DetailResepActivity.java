package com.example.rasanusantara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailResepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep); // pastikan ini sesuai dengan nama XML kamu

        ImageView imageView = findViewById(R.id.detailImage);
        TextView textView = findViewById(R.id.detailText);
        TextView decsriptiontext = findViewById(R.id.description);
        TextView bahantext = findViewById(R.id.bahan);
        TextView langkahtext = findViewById(R.id.langkah);
        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());


        Intent intent = getIntent();
        int imageResId = intent.getIntExtra("imageResId", 0);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String bahan = intent.getStringExtra("bahan");
        String langkah = intent.getStringExtra("langkah");

        imageView.setImageResource(imageResId);
        textView.setText(title);
        decsriptiontext.setText(description);
        bahantext.setText(bahan);
        langkahtext.setText(langkah);
    }
}
