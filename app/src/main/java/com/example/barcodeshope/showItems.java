package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.barcodeshope.Adapter.showItemAdapter;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.databinding.ActivityShowItemsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class showItems extends AppCompatActivity {

    private ActivityShowItemsBinding binding;
    private String categorie="shirt";
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<clothModel> list;
    showItemAdapter showItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShowItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        categorie = getIntent().getStringExtra("cat").toString();

        binding.customTitle.setText(capitalize(categorie+"s"));
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Category.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Store/");

        recyclerView=findViewById(R.id.items_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list=new ArrayList<>();
        showItemAdapter = new showItemAdapter(this, list);
        recyclerView.setAdapter(showItemAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    clothModel ns = dataSnapshot.getValue(clothModel.class);
                    if(ns.getCategoryy().equals(categorie)) {
                        list.add(ns);
                    }
                }
                showItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static final String capitalize(String str)
    {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}