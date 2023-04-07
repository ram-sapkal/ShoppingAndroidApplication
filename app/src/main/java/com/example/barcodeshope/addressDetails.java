package com.example.barcodeshope;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.Model.userInformation;
import com.example.barcodeshope.databinding.ActivityAddressDetailsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class addressDetails extends AppCompatActivity {

    private ActivityAddressDetailsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE=100;

    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddressDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/userInfo");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("user/"+ mAuth.getUid() + "/userInfo/");
        //-----------------------------Fill details if exist

        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    userInformation ui = dataSnapshot.getValue(userInformation.class);
                    if (!ui.getAddress().equals("NA")) {
                        String str=ui.getAddress();
                        if(str.contains("#")) {
                            String a = str.substring(0, str.indexOf("#"));
                            String c = str.substring(str.indexOf("#") + 1);
                            binding.address.setText(a);
                            binding.city.setText(c);
                        }else{
                            binding.address.setText(str);
                        }
                    }
                    // ..
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

        //-----------------------------Submit Address

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=binding.address.getText().toString().trim();
                String c=binding.city.getText().toString().trim();

                if(a.isEmpty()){
                    binding.address.setError("Fill The Field");
                    binding.address.requestFocus();
                }else if(c.isEmpty()){
                    binding.city.setError("Fill The Field");
                    binding.city.requestFocus();
                }else{
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userInformation ui = snapshot.getValue(userInformation.class);
                            ui.setAddress(a+"#"+c);
                            databaseReference.setValue(ui);
                            Toast.makeText(addressDetails.this, "Address Saved", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        //-----------------------------Submit Address/>

        binding.backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountSettings.class));
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

    }

    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location !=null){
                                Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address>address= null;
                                try {
                                    address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    binding.city.setText(address.get(0).getPostalCode()+"");
                                    //binding.address.setText(address.get(0).getAddressLine(0)+"");
                                    String a=address.get(0).getAddressLine(0);
                                    a=a.substring(a.indexOf(",")+1);
                                    binding.address.setText(a);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });

        }else {

            askPermission();

        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(addressDetails.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this, "Request Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}