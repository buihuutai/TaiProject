package com.example.tai1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListBarn extends AppCompatActivity {

    private CardView barn1CardView, barn2CardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barn);

        barn1CardView = findViewById(R.id.barn1_cardview);
        barn2CardView = findViewById(R.id.barn2_cardview);

        barn1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListBarn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        barn2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListBarn.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}