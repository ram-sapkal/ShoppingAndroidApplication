package com.example.barcodeshope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            getSupportActionBar().hide();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }, 4000);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}