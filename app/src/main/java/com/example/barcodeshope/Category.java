package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.databinding.ActivityCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Category extends AppCompatActivity {

    ActivityCategoryBinding binding;

    //to check user already login or not
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private String userStatus="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();



        //--------------------------- user login or not check
        firebaseAuth=FirebaseAuth.getInstance();
        try {
            mAuthStateListner = new FirebaseAuth.AuthStateListener() {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                    if (mFirebaseUser != null) {
                        userStatus="ucl";
                    }
                }
            };
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Failed :="+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //---------------------------user login or not check/>


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
        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userStatus.equals("NA")) {
                    Intent intent = new Intent(getApplicationContext(), Account.class);
                    startActivity(intent);
                }else if(userStatus.equals("ucl")){
                    Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
                    startActivity(intent);
                }
            }
        });
        //--------------------------clicked on tabs/>

        //-----------------------clicked on item then
        try {

            binding.shirt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "shirt");
                    startActivity(intent);
                }
            });
            binding.tshirt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "t-shirt");
                    startActivity(intent);
                }
            });
            binding.jacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "jacket");
                    startActivity(intent);
                }
            });
            binding.jeans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "jeans");
                    startActivity(intent);
                }
            });
            binding.shorts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "shorts");
                    startActivity(intent);
                }
            });
            binding.cap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "cap");
                    startActivity(intent);
                }
            });

            binding.belt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "belt");
                    startActivity(intent);
                }
            });
            binding.sweater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "sweater");
                    startActivity(intent);
                }
            });
            binding.sunglass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), showItems.class);
                    intent.putExtra("cat", "sunglass");
                    startActivity(intent);
                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //-----------------------clicked on item then
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}