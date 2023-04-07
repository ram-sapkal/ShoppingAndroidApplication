package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.Adapter.showItemAdapter;
import com.example.barcodeshope.Adapter.wishListItemAdapter;
import com.example.barcodeshope.Model.CateAndBarcode;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.databinding.ActivityWishlistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class wishlist extends AppCompatActivity {

    private ActivityWishlistBinding binding;
    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseAuth mAuth;
    private clothModel newCM=new clothModel();
    ArrayList<clothModel> list;
    private RecyclerView recyclerView;
    wishListItemAdapter wishListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        //--------------------------------load Items

        mAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/wishList/");

        recyclerView=findViewById(R.id.rView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list=new ArrayList<>();
        wishListItemAdapter = new wishListItemAdapter(this, list);
        recyclerView.setAdapter(wishListItemAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.satext.setVisibility(View.GONE);
                    binding.sa.setVisibility(View.GONE);
                }
                else{
                    binding.satext.setVisibility(View.VISIBLE);
                    binding.sa.setVisibility(View.VISIBLE);
                }
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String code = dataSnapshot.getValue(String.class);
                    databaseReference2=FirebaseDatabase.getInstance()
                            .getReference("Store/"+code+"/");
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            clothModel cm=snapshot.getValue(clothModel.class);
                            list.add(cm);
                            wishListItemAdapter.notifyDataSetChanged();
                            if(list.isEmpty()){
                                binding.satext.setVisibility(View.VISIBLE);
                                binding.sa.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                wishListItemAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //--------------------------------load Items/>
    }
}