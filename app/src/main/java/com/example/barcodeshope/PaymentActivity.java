package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barcodeshope.Model.OrderId;
import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.Model.orderInfo;
import com.example.barcodeshope.Model.userInformation;
import com.example.barcodeshope.databinding.ActivityPaymentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityPaymentBinding binding;
    private DatabaseReference databaseReference,databaseReference2,
            databaseReference3,databaseReference4,databaseReference5,
             databaseReference6,databaseReference7;
    private FirebaseAuth firebaseAuth;
    private clothModel newCM=new clothModel();
    ArrayList<clothModel> list;
    int coast=0,discount=0,total=0;
    String orderId="";
    userInformation ui;
    clothModel cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Checkout.preload(getApplicationContext());

        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();


        //----------------------------------------------load all deta


        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("user/"+ firebaseAuth.getUid() + "/userInfo/");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    if(dataSnapshot.exists()) {
                        binding.personDetails.setVisibility(View.VISIBLE);
                        ui = dataSnapshot.getValue(userInformation.class);
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
                    }else{
                        binding.personDetails.setVisibility(View.GONE);
                        Toast.makeText(PaymentActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                    // ..
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            };
            databaseReference.addValueEventListener(postListener);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        try {
            databaseReference2 = FirebaseDatabase.getInstance().getReference("Store/"+ getIntent().getStringExtra("bCode") +"/");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    cm = dataSnapshot.getValue(clothModel.class);
                    Glide.with(getApplicationContext()).load(cm.getUrl()).into(binding.itemImage);
                    binding.itemCompany.setText(cm.getCompany());
                    binding.itemDescription.setText(cm.getStyle());
                    binding.itemSize.setText(cm.getSize());

                    coast=Integer.parseInt(String.valueOf(cm.getPrice()));
                    discount = (coast*40)/100;
                    binding.fCostmrp.setText("₹"+coast);
                    binding.fCostmrp.setPaintFlags(binding.fCostmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.fCost.setText("₹"+(coast-discount));
                    binding.fDiscount.setText("%40 off");

                    binding.tvPriceItemsPrice.setText("₹"+coast);
                    binding.tvDiscountPrice.setText("- ₹"+discount);
                    binding.tvTotalPrice.setText("₹"+(coast-discount));
                    binding.tvGreat.setText("You will save ₹"+(discount)+" on this order");
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

        //----------------------------------------------load all deta/>


        binding.onlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser == null) {
                    Toast.makeText(PaymentActivity.this, "Login First", Toast.LENGTH_SHORT).show();
                }else{
                    if(binding.personName.getText().toString().isEmpty()){
                        binding.personName.setError("Fill Information");
                        binding.personName.requestFocus();
                    }else if(binding.personACode.getText().toString().isEmpty()){
                        binding.personACode.setError("Fill Information");
                        binding.personACode.requestFocus();
                    }else if(binding.personAddress.getText().toString().isEmpty()){
                        binding.personAddress.setError("Fill Information");
                        binding.personAddress.requestFocus();
                    }else if(orderId.trim().isEmpty()){
                        Toast.makeText(PaymentActivity.this, "Wait for a sec", Toast.LENGTH_SHORT).show();
                    }else{
                        OrderId oid=new OrderId(orderId);
                        databaseReference4.setValue(oid);
                        startPayment(coast - discount,oid.getOid());
                    }
                }
            }
        });

        databaseReference3=FirebaseDatabase.getInstance().getReference("orderid/lastorderid/");
        databaseReference4=FirebaseDatabase.getInstance().getReference("orderid/lastorderid");
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    OrderId od=snapshot.getValue(OrderId.class);
                    int num = Integer.parseInt(od.getOid());
                    num=num+1;
                    String code=String.valueOf(num);
                    orderId=code;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startPayment(int price, String orderi) {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_live_wQpGAIQXtIHkz9");


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.barcode);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Barcode Shope");
            options.put("description", orderi+"");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", orderi+"");//from response of step 3.
            options.put("theme.color", "#0327ED");
            options.put("currency", "INR");
            options.put("amount", (price*100)+"");//pass amount in currency subunits
            //options.put("prefill.email", "sapkaljagannathram21@gmail.com");
            options.put("prefill.contact",ui.getMobileNumber()+"");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Toast.makeText(activity, "Error in starting Razorpay Checkout, "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(this, "Success payment Id"+s, Toast.LENGTH_SHORT).show();
        databaseReference5 = FirebaseDatabase.getInstance().getReference("Store/"+cm.getBcode());
        int quantity=Integer.parseInt(cm.getQuantity());
        if(quantity>0){
            quantity=quantity-1;
        }
        clothModel cm1=new clothModel(cm.getType(),cm.getCompany(),
                cm.getStyle(),cm.getSize(),
                cm.getPrice(),cm.getBcode(),
                cm.getUrl(), cm.getCategoryy(),
                quantity+"");
        databaseReference5.setValue(cm1);

        orderInfo oi = new orderInfo(ui.getFirstName(), ui.getLastName(),
                                     ui.getMobileNumber(),ui.getAddress(),
                (ui.getAddress()).substring(ui.getAddress().indexOf("#")+1),
                cm.getBcode(),s,orderId,"Deliver within 24hrs");
        databaseReference6 = FirebaseDatabase.getInstance().getReference("user/"+ firebaseAuth.getUid() + "/MyOrders");
        databaseReference6.child(oi.getBcode()).setValue(oi);
        databaseReference7=FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference7.child(oi.getOrderId()).setValue(oi);



    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Failed "+s, Toast.LENGTH_SHORT).show();
    }
}