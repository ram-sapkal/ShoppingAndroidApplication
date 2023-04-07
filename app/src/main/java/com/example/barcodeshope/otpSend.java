package com.example.barcodeshope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeshope.databinding.ActivityOtpSendBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpSend extends AppCompatActivity {

    private ActivityOtpSendBinding binding;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        try {
            //FirebaseApp.initializeApp(getApplicationContext());
            mAuth = FirebaseAuth.getInstance();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(binding.etPhone.getText().toString().trim().isEmpty()){
                    Toast.makeText(otpSend.this, "Invalid number", Toast.LENGTH_SHORT).show();
                }else if(binding.etPhone.getText().toString().trim().length()<10){
                    Toast.makeText(otpSend.this, "phone must be 10 numbers", Toast.LENGTH_SHORT).show();
                }else{
                    otpSend();
                }
            }
        });
    }

    private void otpSend() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSend.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                binding.progressBar.setVisibility(View.GONE);
                binding.btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(otpSend.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                binding.progressBar.setVisibility(View.GONE);
                binding.btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(otpSend.this, "Otp Sucessfully send on " + binding.etPhone.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(otpSend.this, otpVerify.class);
                intent.putExtra("phone", binding.etPhone.getText().toString().trim());
                intent.putExtra("verificationID", verificationId);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + binding.etPhone.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}