package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.Adapter.cartItemAdapter;
import com.example.barcodeshope.Adapter.wishListItemAdapter;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.Model.userInformation;
import com.example.barcodeshope.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cart extends AppCompatActivity {

    ActivityCartBinding binding;

    //variables
    int item=0,originalCoast=0,discountCoast=0;
    String discount="%40 off";

    //database
    private DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4;
    private clothModel newCM=new clothModel();
    ArrayList<clothModel> list;
    private RecyclerView recyclerView;
    cartItemAdapter cartItemAdapter;

    //to check user already login or not
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private String userStatus="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
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
        //---------------------------

        //-------------------------------get user person info

        try {
            databaseReference3 = FirebaseDatabase.getInstance().getReference("user/"+ firebaseAuth.getUid() + "/userInfo/");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI

                    if(dataSnapshot.exists()) {
                        binding.personDetails.setVisibility(View.VISIBLE);
                        userInformation ui = dataSnapshot.getValue(userInformation.class);
                        binding.personName.setText(" " + ui.getFirstName() + " " + ui.getLastName());
                        String str = ui.getAddress();
                        if (str.contains("#")) {
                            String a = str.substring(0, str.indexOf("#"));
                            String c = str.substring(str.indexOf("#") + 1);
                            binding.personAddress.setText(a);
                            binding.personACode.setText(c);
                        } else {
                            binding.personAddress.setText(str);
                        }
                        //
                    }else{
                        binding.personDetails.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            };
            databaseReference3.addValueEventListener(postListener);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //-------------------------------get user person info/>

        //-----------------------------fetch recycler data

        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + firebaseAuth.getUid() + "/cart/");
        recyclerView=findViewById(R.id.cartRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        list=new ArrayList<>();
        cartItemAdapter = new cartItemAdapter(this, list);
        recyclerView.setAdapter(cartItemAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.cartAnimation.setVisibility(View.GONE);
                    binding.cartAnimationText.setVisibility(View.GONE);
                    binding.cardPriceDetails.setVisibility(View.VISIBLE);
                    binding.personDetails.setVisibility(View.VISIBLE);
                }
                else{
                    binding.cartAnimation.setVisibility(View.VISIBLE);
                    binding.cartAnimationText.setVisibility(View.VISIBLE);
                    binding.cardPriceDetails.setVisibility(View.GONE);
                    binding.personDetails.setVisibility(View.GONE);
                }
                list.clear();
                item=0;originalCoast=0;discountCoast=0;
                discount="";

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String code = dataSnapshot.getValue(String.class);
                    databaseReference2=FirebaseDatabase.getInstance()
                            .getReference("Store/"+code+"/");
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            clothModel cm=snapshot.getValue(clothModel.class);
                            list.add(cm);
                            int coast=Integer.parseInt(String.valueOf(cm.getPrice()));
                            int discount = (coast*40)/100;
                            item++;
                            originalCoast=originalCoast+coast;
                            discountCoast=discountCoast+(discount);
                            cartItemAdapter.notifyDataSetChanged();
                            if(list.isEmpty()){
                                binding.cartAnimationText.setVisibility(View.VISIBLE);
                                binding.cartAnimation.setVisibility(View.VISIBLE);
                                binding.cardPriceDetails.setVisibility(View.GONE);
                                binding.personDetails.setVisibility(View.GONE);
                            }
                            if(originalCoast!=0 && discountCoast!=0 && item!=0){
                                binding.tvPriceItems.setText("Price ( "+item+" items )");
                                binding.tvPriceItemsPrice.setText("₹"+originalCoast);
                                binding.tvDiscountPrice.setText("-₹"+discountCoast);
                                binding.tvTotalPrice.setText("₹"+(originalCoast-discount));
                                binding.tvGreat.setText("You will save "+discountCoast+" on this order");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                cartItemAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //-----------------------------fetch recycler data/>

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}