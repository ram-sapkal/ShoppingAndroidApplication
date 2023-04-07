package com.example.barcodeshope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().hide();
    }
}