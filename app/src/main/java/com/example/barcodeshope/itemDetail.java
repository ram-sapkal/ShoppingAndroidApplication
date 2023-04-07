package com.example.barcodeshope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.databinding.ActivityItemDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class itemDetail extends AppCompatActivity {

    private ActivityItemDetailBinding binding;
    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();


        binding.itemCompany.setText(getIntent().getStringExtra("company"));
        binding.itemType.setText(getIntent().getStringExtra("type"));
        binding.itemCost.setText(getIntent().getStringExtra("price"));
        binding.itemDescription.setText(getIntent().getStringExtra("description"));
        binding.itemSize.setText("Size :- "+getIntent().getStringExtra("size"));
        String url=getIntent().getStringExtra("url");
        Glide.with(getApplicationContext()).load(url).into(binding.itemImage);

        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PaymentActivity.class);
                intent.putExtra("bCode",getIntent().getStringExtra("bCode"));
                intent.putExtra("type",getIntent().getStringExtra("type"));
                intent.putExtra("company",getIntent().getStringExtra("company"));
                intent.putExtra("description",getIntent().getStringExtra("description"));
                intent.putExtra("size",getIntent().getStringExtra("size"));
                intent.putExtra("price","₹"+getIntent().getStringExtra("price"));
                intent.putExtra("url",getIntent().getStringExtra("url"));
                intent.putExtra("cate",getIntent().getStringExtra("cate"));
                startActivity(intent);

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Category.class));
            }
        });
        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //--------------add item to cart

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth=FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/cart");
                try {
                    databaseReference.child(getIntent().getStringExtra("bCode")).setValue(getIntent().getStringExtra("bCode"));
                    Toast.makeText(itemDetail.this, "Product Added to cart", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //--------------add item to cart/>
    }
}