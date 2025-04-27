package com.example.gsb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final int LOADING_DURATION = 3500; // ms

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView textBienvenue = findViewById(R.id.textBienvenue);

        // Récupérer nom et prénom de l'utilisateur connecté
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String nom = prefs.getString("USER_NOM", "Nom");
        String prenom = prefs.getString("USER_PRENOM", "Prénom");

        textBienvenue.setText("Bienvenue " + prenom + " " + nom);

        // Délai de chargement
        new Handler().postDelayed(() -> {
            // Aller à la page d'accueil (MainActivity ou autre)
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }, LOADING_DURATION);
    }
}