package com.hitrosttech.mobilebanking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hitrosttech.mobilebanking.databinding.ActivitySignInBinding;
import com.hitrosttech.mobilebanking.databinding.ActivitySignUpBinding;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private ProgressDialog mdialog;
    public static final String userEmail = "";

    public static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        /*Starting the Sign Up Activity*/
        binding.signUp.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
        });

        /*Getting the Users Instance from Firebase*/
        mdialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener = firebaseAuth -> {
            if (mUser != null){
                Intent intent = new Intent(SignIn.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Log.e(TAG, "AuthStateChanged : Sign In");
            }else {
                Log.e(TAG, "AuthStateChanged : Logout");
            }
        };

        binding.login.setOnClickListener(v -> {
            userSignIn();
        });
    }

    /*Checking User Credentials*/
    private void userSignIn() {
       String emailValue = binding.email.getText().toString().trim();
       String passwordValue = binding.password.getText().toString().trim();

        if (TextUtils.isEmpty(emailValue) || !emailValue.contains("@")){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Enter a Valid Email!");
            return;
        }else if (TextUtils.isEmpty(passwordValue)){
            Toast.makeText(this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Enter a Valid password");
            return;
        }

        /*Logging In the User*/
        mdialog.setMessage("Logging In Please wait...");
        mdialog.show();
        mAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(task -> {
           if (!task.isSuccessful()){
               mdialog.dismiss();
               Toast.makeText(SignIn.this, "Login not Successful!", Toast.LENGTH_SHORT).show();
               Log.e(TAG, "Login not successful!");
           }else {
               mdialog.dismiss();
               checkEmailVerified();
           }
        }).addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }
    
    /*Activity Lifecycle*/
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /*Checking Email Verification form Firebase*/
    private void checkEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        
        if (!emailVerified){
            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }else {
            binding.email.getText().clear();
            binding.password.getText().clear();
            
            Intent intent = new Intent(SignIn.this, Home.class);
            String emailValue = binding.email.getText().toString();
            intent.putExtra(userEmail, emailValue);
            startActivity(intent);
        }
    }

}