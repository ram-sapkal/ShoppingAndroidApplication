package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.Model.clothModel;
import com.example.barcodeshope.Model.phoneNumber;
import com.example.barcodeshope.Model.userInformation;
import com.example.barcodeshope.databinding.ActivityOtpVerifyBinding;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class otpVerify extends AppCompatActivity {

    private ActivityOtpVerifyBinding binding;
    private String verificationId;
    private OTP_Receiver otp_receiver;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String ver=null;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        //editTextInput();

        binding.tvMobile.setText(String.format(
                "+91-%s",getIntent().getStringExtra("phone")
        ));

        verificationId = getIntent().getStringExtra("verificationID");

        binding.tvResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(otpVerifyActivity.this, "otp send Sucessfully", Toast.LENGTH_SHORT).show();
                againOtpSend();
                Toast.makeText(otpVerify.this, "Otp resend sucessfully", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBarVerify.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.INVISIBLE);
                if(binding.etC1.getText().toString().trim().isEmpty() ||
                        binding.etC2.getText().toString().trim().isEmpty() ||
                        binding.etC3.getText().toString().trim().isEmpty() ||
                        binding.etC4.getText().toString().trim().isEmpty() ||
                        binding.etC5.getText().toString().trim().isEmpty() ||
                        binding.etC6.getText().toString().trim().isEmpty() ){
                    Toast.makeText(otpVerify.this, "otp is not valid", Toast.LENGTH_SHORT).show();
                }else{
                    if(verificationId !=null){
                        String code = binding.etC1.getText().toString().trim() +
                                binding.etC2.getText().toString().trim() +
                                binding.etC3.getText().toString().trim() +
                                binding.etC4.getText().toString().trim() +
                                binding.etC5.getText().toString().trim() +
                                binding.etC6.getText().toString().trim();

                        if(ver!=null){
                            code=ver;
                        }

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            binding.progressBarVerify.setVisibility(View.VISIBLE);
                                            binding.btnVerify.setVisibility(View.INVISIBLE);

                                            Toast.makeText(otpVerify.this, "Welcome.......", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(otpVerify.this,AccountSettings.class);
                                            intent.putExtra("status","logins");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                            String mnumber=binding.tvMobile.getText().toString().trim();
                                            mnumber=mnumber.substring(4);

                                            databaseReference = FirebaseDatabase.getInstance().getReference("user/"+mAuth.getUid()+"/");
                                            /*userInformation ui=new userInformation("NA","NA",
                                                    mnumber,"NA","yes");*/
                                            HashMap<String,String>map=new HashMap<>();
                                            map.put("phNumber",mnumber);
                                            databaseReference.child("mNumber").setValue(map);

                                            startActivity(intent);
                                        }else {
                                            binding.progressBarVerify.setVisibility(View.GONE);
                                            binding.btnVerify.setVisibility(View.VISIBLE);
                                            Toast.makeText(otpVerify.this, "OTP is not valid", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        autoOtpReceiver();
    }

    private void autoOtpReceiver() {
        otp_receiver = new OTP_Receiver();
        this.registerReceiver(otp_receiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        otp_receiver.initListener(new OTP_Receiver.OtpReceiverListener() {
            @Override
            public void onOtpSuccess(String otp) {
                int o1=Character.getNumericValue(otp.charAt(0));
                int o2=Character.getNumericValue(otp.charAt(1));
                int o3=Character.getNumericValue(otp.charAt(2));
                int o4=Character.getNumericValue(otp.charAt(3));
                int o5=Character.getNumericValue(otp.charAt(4));
                int o6=Character.getNumericValue(otp.charAt(5));

                binding.etC1.setText(String.valueOf(o1));
                binding.etC2.setText(String.valueOf(o2));
                binding.etC3.setText(String.valueOf(o3));
                binding.etC4.setText(String.valueOf(o4));
                binding.etC5.setText(String.valueOf(o5));
                binding.etC6.setText(String.valueOf(o6));
            }

            @Override
            public void onOtpTimeout() {
                Toast.makeText(otpVerify.this, " SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void againOtpSend() {

        binding.progressBarVerify.setVisibility(View.VISIBLE);
        binding.btnVerify.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                binding.progressBarVerify.setVisibility(View.GONE);
                binding.btnVerify.setVisibility(View.VISIBLE);
                Toast.makeText(otpVerify.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                binding.progressBarVerify.setVisibility(View.GONE);
                binding.btnVerify.setVisibility(View.VISIBLE);
                ver=verificationId;
                Toast.makeText(otpVerify.this, "Otp Sucessfully send on ", Toast.LENGTH_SHORT).show();

            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + getIntent().getStringExtra("phone"))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /*private void editTextInput() {
        binding.etC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(otp_receiver !=null){
            unregisterReceiver(otp_receiver);
        }
    }
}