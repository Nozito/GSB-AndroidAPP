package com.example.gsb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500; // durée en ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoImage);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(anim);

        // ✅ Utilisation recommandée
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish(); // Pour éviter de revenir en arrière sur le Splash
        }, SPLASH_DURATION);
    }
}