package com.example.gsb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifierVisiteActivity extends AppCompatActivity {

    private EditText editTextDateVisite, editTextCommentaire;
    private Button btnSave;
    private String visiteId, praticienId, visiteurId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_visite);

        // Initialisation des vues
        editTextDateVisite = findViewById(R.id.editTextDateVisite);
        editTextCommentaire = findViewById(R.id.editTextCommentaire);
        btnSave = findViewById(R.id.btnSave);

        // Récupérer les données de la visite à modifier
        visiteId = getIntent().getStringExtra("VISITE_ID");
        praticienId = getIntent().getStringExtra("PRA_ID");
        visiteurId = getIntent().getStringExtra("VISITEUR_ID");

        // Charger les informations de la visite existante
        chargerVisite(visiteId);

        // Bouton de sauvegarde
        btnSave.setOnClickListener(v -> {
            String newCommentaire = editTextCommentaire.getText().toString().trim();
            String newDateVisite = editTextDateVisite.getText().toString().trim();

            if (!newCommentaire.isEmpty() && !newDateVisite.isEmpty()) {
                updateVisite(visiteId, newDateVisite, newCommentaire);
            } else {
                Toast.makeText(ModifierVisiteActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chargerVisite(String visiteId) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Vous devez être connecté pour voir cette page", Toast.LENGTH_SHORT).show();
            return;
        }

        GSBApi apiService = ApiClient.getApiService(token); // API avec token

        // Appel à l'API pour récupérer les informations de la visite
        Call<Visite> call = apiService.getVisiteById(visiteId);

        call.enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(Call<Visite> call, Response<Visite> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Visite visite = response.body();
                    // Remplir les champs avec les données de la visite existante
                    editTextDateVisite.setText(visite.getDateVisite());
                    editTextCommentaire.setText(visite.getCommentaire());
                } else {
                    Toast.makeText(ModifierVisiteActivity.this, "Erreur lors du chargement des données de la visite", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Visite> call, Throwable t) {
                Toast.makeText(ModifierVisiteActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisite(String visiteId, String newDateVisite, String newCommentaire) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Vous devez être connecté pour effectuer cette action", Toast.LENGTH_SHORT).show();
            return;
        }

        GSBApi apiService = ApiClient.getApiService(token); // API avec token

        // Création d'un objet VisiteRequest avec les nouvelles données
        VisiteRequest visiteRequest = new VisiteRequest(newDateVisite);

        Call<Visite> call = apiService.updateVisite(visiteId, visiteRequest);
        call.enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(Call<Visite> call, Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModifierVisiteActivity.this, "Visite mise à jour", Toast.LENGTH_SHORT).show();
                    finish();  // Retour à la page précédente après la mise à jour
                } else {
                    Toast.makeText(ModifierVisiteActivity.this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Visite> call, Throwable t) {
                Toast.makeText(ModifierVisiteActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}