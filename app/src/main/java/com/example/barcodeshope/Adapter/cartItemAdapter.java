package com.example.barcodeshope.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cartItemAdapter extends RecyclerView.Adapter<cartItemAdapter.MyViewHolder> {

    Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    ArrayList<clothModel> list;

    public cartItemAdapter(Context context, ArrayList<clothModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public cartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_cart_item,parent,false);
        return new cartItemAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cartItemAdapter.MyViewHolder holder, int position) {

        try {
            clothModel ns = list.get(position);
            holder.title.setText(ns.getCompany());
            //holder.tv1.setPaintFlags(holder.tv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            int coast=Integer.parseInt(String.valueOf(ns.getPrice()));
            int discount = (coast*40)/100;
            holder.coast.setText("₹"+coast);
            holder.coast.setPaintFlags(holder.coast.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.costDiscount.setText("₹"+(coast-discount));
            holder.discount.setText("%40 off");
            Glide.with(context.getApplicationContext()).load(ns.getUrl()).into(holder.imageView);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth=FirebaseAuth.getInstance();
                clothModel gnd = list.get(position);
                databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getUid() + "/cart/");
                databaseReference.child(gnd.getBcode()).removeValue();
            }
        });

        holder.buyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title,description,coast,costDiscount,discount,removeItem,buyitem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.cart_image);
            title=itemView.findViewById(R.id.f_name);
            description=itemView.findViewById(R.id.f_description);
            coast=itemView.findViewById(R.id.f_costmrp);
            costDiscount=itemView.findViewById(R.id.f_cost);
            discount=itemView.findViewById(R.id.f_discount);
            removeItem=itemView.findViewById(R.id.remove_item);
            buyitem=itemView.findViewById(R.id.buy_item);
        }
    }
}
