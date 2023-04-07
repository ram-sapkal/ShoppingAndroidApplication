package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.Adapter.MainPageSIAdapter;
import com.example.barcodeshope.Adapter.SliderAdapter;
import com.example.barcodeshope.Adapter.showItemAdapter;
import com.example.barcodeshope.Model.SliderData;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String categorie;
    private RecyclerView recyclerView,recyclerView2,recyclerView3;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4;
    ArrayList<clothModel> list;
    ArrayList<clothModel> list2;
    ArrayList<clothModel> list3;
    ArrayList<SliderData> sliderData;
    com.example.barcodeshope.Adapter.showItemAdapter showItemAdapter,showItemAdapter2,showItemAdapter3;
    MainPageSIAdapter mainPageSIAdapter,mainPageSIAdapter2,mainPageSIAdapter3;

    //to check user already login or not
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private String userStatus="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();
        try {
            mAuthStateListner = new FirebaseAuth.AuthStateListener() {
                /*FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();*/

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

        binding.categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Category.class);
                startActivity(intent);
            }
        });

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Intent intent = new Intent(getApplicationContext(), cart.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Login First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //--------------------------

        databaseReference = FirebaseDatabase.getInstance().getReference("Store/");

        recyclerView=findViewById(R.id.shirtrecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        list=new ArrayList<>();
        mainPageSIAdapter = new MainPageSIAdapter(this, list);
        recyclerView.setAdapter(mainPageSIAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    clothModel ns = dataSnapshot.getValue(clothModel.class);
                    if(ns.getCategoryy().equals("shirt")) {
                        list.add(ns);
                    }
                    if(list.size()>7){
                        break;
                    }
                }
                mainPageSIAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //--------------------------

        //--------------------------
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Store/");

        recyclerView2=findViewById(R.id.jeansrecyclerView);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        list2=new ArrayList<>();
        mainPageSIAdapter2 = new MainPageSIAdapter(this, list2);
        recyclerView2.setAdapter(mainPageSIAdapter2);

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list2.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    clothModel ns = dataSnapshot.getValue(clothModel.class);
                    assert ns != null;
                    if(ns.getCategoryy().equals("t-shirt")) {
                        list2.add(ns);
                    }
                    if(list.size()>7){
                        break;
                    }
                }
                mainPageSIAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //--------------------------

        //--------------------------
        databaseReference3 = FirebaseDatabase.getInstance().getReference("Store/");

        recyclerView3=findViewById(R.id.jacketrecyclerView);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        list3=new ArrayList<>();
        mainPageSIAdapter3 = new MainPageSIAdapter(this, list3);
        recyclerView3.setAdapter(mainPageSIAdapter3);

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list3.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    clothModel ns = dataSnapshot.getValue(clothModel.class);
                    assert ns != null;
                    if(ns.getCategoryy().equals("jacket")){
                        list3.add(ns);
                    }
                    if(list.size()>7){
                        break;
                    }
                }
                mainPageSIAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //--------------------------

        //----------------slider top & Buttom--------------------

        databaseReference4 = FirebaseDatabase.getInstance().getReference("Discount/Banner/");
        SliderView sliderView1 = findViewById(R.id.slider);//top slider
        SliderView sliderView2= findViewById(R.id.slider2);//bottom slider

        sliderData=new ArrayList<>();
        SliderAdapter adapterS1 = new SliderAdapter(this, sliderData);// top slider

        sliderView1.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView2.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView1.setSliderAdapter(adapterS1);
        sliderView2.setSliderAdapter(adapterS1);

        sliderView1.setScrollTimeInSec(3);
        sliderView2.setScrollTimeInSec(3);

        sliderView1.setAutoCycle(true);
        sliderView2.setAutoCycle(true);

        sliderView1.startAutoCycle();
        sliderView2.startAutoCycle();

        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sliderData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String ns = dataSnapshot.getValue(String.class);
                    SliderData sd=new SliderData(ns);
                    sliderData.add(sd);
                }
                adapterS1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //----------------slider top & Buttom--------------------
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}