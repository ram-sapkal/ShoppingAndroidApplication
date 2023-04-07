package com.example.barcodeshope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.barcodeshope.databinding.ActivityDeactivateaccBinding;


public class deactivateacc extends AppCompatActivity {

    private ActivityDeactivateaccBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeactivateaccBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.phnumber.getText().toString().trim().isEmpty()){
                    binding.phnumber.setError("fill the field");
                    binding.phnumber.requestFocus();
                }else {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            binding.deAnimation.setVisibility(View.VISIBLE);
                            binding.aniText.setVisibility(View.VISIBLE);
                            binding.deAnimation.playAnimation();
                        }
                    }, 2500);
                    binding.deactivate.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}