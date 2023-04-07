package com.example.barcodeshope.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.barcodeshope.Model.CateAndBarcode;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.R;
import com.example.barcodeshope.itemDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class wishListItemAdapter extends RecyclerView.Adapter<wishListItemAdapter.MyViewHolder> {

    Context context;
    private DatabaseReference databaseReference2;
    private FirebaseAuth mAuth;
    ArrayList<clothModel> list;

    public wishListItemAdapter(Context context, ArrayList<clothModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_cloth,parent,false);
        return new wishListItemAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            clothModel ns = list.get(position);
            holder.tv1.setText(ns.getCompany());
            holder.tv1.setPaintFlags(holder.tv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv2.setText(ns.getStyle());
            holder.tv3.setText("₹"+ns.getPrice());
            Glide.with(context.getApplicationContext()).load(ns.getUrl()).into(holder.imageView);
            holder.blank.setVisibility(View.INVISIBLE);
            holder.favtBtn.setVisibility(View.VISIBLE);
            holder.favtBtn.playAnimation();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clothModel gnd = list.get(position);
                Intent intent = new Intent(context, itemDetail.class);
                intent.putExtra("bCode",gnd.getBcode());
                intent.putExtra("type",gnd.getType());
                intent.putExtra("company",gnd.getCompany());
                intent.putExtra("description",gnd.getStyle());
                intent.putExtra("size",gnd.getSize());
                intent.putExtra("price","₹"+gnd.getPrice());
                intent.putExtra("barcode",gnd.getBcode());
                intent.putExtra("url",gnd.getUrl());
                context.startActivity(intent);
            }
        });

        holder.favtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth=FirebaseAuth.getInstance();
                clothModel gnd = list.get(position);
                databaseReference2 = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/wishList/");
                databaseReference2.child(gnd.getBcode()).removeValue();

                holder.favtBtn.pauseAnimation();
                holder.blank.setVisibility(View.VISIBLE);
                holder.favtBtn.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv1,tv2,tv3;
        LottieAnimationView favtBtn;
        ImageView blank;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.f_img);
            tv1=itemView.findViewById(R.id.f_name);
            tv2=itemView.findViewById(R.id.f_description);
            tv3=itemView.findViewById(R.id.f_cost);
            favtBtn=itemView.findViewById(R.id.favBtnAni);
            blank=itemView.findViewById(R.id.favBtnblank);

        }
    }
}
