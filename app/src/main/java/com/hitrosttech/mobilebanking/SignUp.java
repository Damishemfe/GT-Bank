package com.hitrosttech.mobilebanking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hitrosttech.mobilebanking.databinding.ActivitySignUpBinding;
import com.hitrosttech.mobilebanking.model.User;

import java.util.Date;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);


        /*Mapping the Sign In Text to Sign in Page*/
        binding.tvSignIn.setOnClickListener(
                v -> startActivity(new Intent(SignUp.this, SignIn.class))
        );

        /*Authentication Using Firebase*/
        mAuth = FirebaseAuth.getInstance();
        binding.signUp.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onClick(View v) {
        if(v == binding.signUp){
            userRegistration();
        }else if(v == binding.tvSignIn){
            startActivity(new Intent(SignUp.this, SignIn.class));
        }
    }

    /*Get the value of the text from users and validate the fields*/
    /*Register Users*/
    private void userRegistration() {
        String firstNameValue = binding.firstName.getText().toString().trim();
        String lastNameValue = binding.lastName.getText().toString().trim();
        String emailValue = binding.email.getText().toString().trim();
        String passwordValue = binding.password.getText().toString().trim();

        /*Conditional Statements to check the fields*/
        if (TextUtils.isEmpty(firstNameValue)){
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(lastNameValue)){
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(emailValue)){
            Toast.makeText(this, "Please enter Email Address", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(passwordValue) || passwordValue.length() < 6){
            Toast.makeText(this, "Enter password up to 6 Characters", Toast.LENGTH_SHORT).show();
            return;
        }

        /*Display dialog Message*/
        mDialog.setMessage("Creating Your Account...");
        mDialog.setCancelable(false);
        mDialog.show();

        /*Creating Account*/
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                sendEmailVerification();
                mDialog.dismiss();
                onAuth(task.getResult().getUser());
                startActivity(new Intent(
                        SignUp.this, SignIn.class
                ));
            }
        }).addOnFailureListener(e -> Log.e("Sign Up", e.getMessage()));
    }

    private void onAuth(FirebaseUser user) { createANewUser(user.getUid());
    }

    private void createANewUser(String uid) {
        User user = buildNewUser();
        mDatabase.child(uid).setValue(user);
    }

    /*Building A New User*/
    private User buildNewUser() {
        return new User(
                binding.firstName,
                binding.lastName,
                binding.email,
                new Date().getTime()
        );
    }

    /*Sending Email Verification*/
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(this, "Check Your Email for Verification", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
            });
        }
    }
}