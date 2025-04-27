package com.example.gsb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de la requête de login
        LoginRequest request = new LoginRequest(email, password);

        // Crée une instance de GSBApi
        GSBApi apiService = ApiClient.getApiServiceWithoutAuth();  // Utilise sans token pour le login
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    String nom = loginResponse.getNom();
                    String prenom = loginResponse.getPrenom();

                    Log.d("LOGIN", "Token: " + token);
                    Log.d("LOGIN", "Nom: " + nom + ", Prénom: " + prenom);

                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("TOKEN", token);
                    editor.putString("VISITEUR_ID", loginResponse.getVisiteurId());
                    editor.putString("USER_NOM", nom);
                    editor.putString("USER_PRENOM", prenom);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("LOGIN", "Échec de la connexion");
                    Toast.makeText(LoginActivity.this, "Échec de la connexion, vérifiez vos identifiants", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN", "Erreur lors du login", t);
                Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}