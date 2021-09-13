package com.hitrosttech.mobilebanking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* HIDING THE ACTION BAR */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* STARTING THE NEXT ACTIVITY */
        Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(3500);
                } catch (InterruptedException e) {
                    Log.e("Splash Screen", e.getMessage());
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashScreen.this, SignIn.class));
                }
            }
        };
        thread.start();

    }
}