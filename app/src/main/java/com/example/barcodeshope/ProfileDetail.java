package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.barcodeshope.Model.userInformation;
import com.example.barcodeshope.databinding.ActivityProfileDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileDetail extends AppCompatActivity {

    ActivityProfileDetailBinding binding;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    private FirebaseAuth mAuth;
    HashMap hmap;
    private String isV="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/userInfo");

        //-----------------------------Fill phnumber if exist
        Object obj=new Object();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("user/"+mAuth.getUid()+"/mNumber/");
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    hmap=(HashMap)snapshot.getValue(obj.getClass());
                    binding.phnumber.setText(hmap.get("phNumber")+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //-----------------------------Fill phnumber if exist

        //-----------------------------Fill details if exist

        try {
            databaseReference2 = FirebaseDatabase.getInstance().getReference("user/"+ mAuth.getUid() + "/userInfo/");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        // Get Post object and use the values to update the UI
                        userInformation ui = dataSnapshot.getValue(userInformation.class);
                        if (!ui.getFirstName().equals("NA")) {
                            binding.fname.setText(ui.getFirstName());
                        }
                        if (!ui.getLastName().equals("NA")) {
                            binding.lname.setText(ui.getLastName());
                        }
                        if (!ui.getAddress().equals("NA")) {
                            binding.address.setText(ui.getAddress());
                            String str = ui.getAddress();
                            if (str.contains("#")) {
                                String a = str.substring(0, str.indexOf("#"));
                                binding.address.setText(a);
                            } else {
                                binding.address.setText(str);
                            }
                        }/*
                        if (!ui.getIsVerified().equals("NA")) {
                            isV = ui.getIsVerified();
                        }*/
                        // ..
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            };
            databaseReference2.addValueEventListener(postListener);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //-----------------------------Fill details if exist/>


        //-----------------------------submit Profile Detail
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName=binding.fname.getText().toString().trim();
                String lName=binding.lname.getText().toString().trim();
                String phNumber=binding.phnumber.getText().toString().trim();
                String address=binding.address.getText().toString().trim();

                if(fName.isEmpty()){
                    binding.fname.setError("Fill The Field");
                    binding.fname.requestFocus();
                }else if(lName.isEmpty()){
                    binding.lname.setError("Fill The Field");
                    binding.lname.requestFocus();
                }else if(phNumber.isEmpty()){
                    binding.phnumber.setError("Fill The Field");
                    binding.phnumber.requestFocus();
                }else if(address.isEmpty()){
                    binding.address.setError("Fill The Field");
                    binding.address.requestFocus();
                }else{
                    binding.submitBtn.playAnimation();
                    userInformation ui=new userInformation(fName,lName,phNumber,address);
                    databaseReference.setValue(ui);
                }
            }
        });

        binding.backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountSettings.class));
            }
        });
        //-----------------------------submit Profile Detail/>
    }
}