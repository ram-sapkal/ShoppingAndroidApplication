package com.example.barcodeshope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.databinding.ActivityAccountSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettings extends AppCompatActivity {

    ActivityAccountSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccountSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        //--------------------------clicked on tabs
        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),cart.class);
                startActivity(intent);
            }
        });
        binding.categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Category.class);
                startActivity(intent);
            }
        });
        //--------------------------clicked on tabs/>

        //--------------------------clicked on account Setting items
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfileDetail.class);
                startActivity(intent);
            }
        });

        binding.deactivateAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),deactivateacc.class);
                startActivity(intent);
            }
        });

        binding.feedbackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),feedback.class);
                startActivity(intent);
            }
        });

        binding.saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), addressDetails.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(AccountSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.termsAndPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),activity_terms.class));
            }
        });

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }catch (Exception e){
                    Toast.makeText(AccountSettings.this, "Log out exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //--------------------------clicked on account Setting items/>

        //--------------------------clicked on cards
        binding.orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),orders.class);
                startActivity(intent);
            }
        });
        binding.wishListcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),wishlist.class);
                startActivity(intent);
            }
        });
        binding.notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),notification.class);
                startActivity(intent);
            }
        });
        //--------------------------clicked on cards/>
    }
}