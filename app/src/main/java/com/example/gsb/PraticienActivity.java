package com.example.gsb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PraticienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PraticienAdapter praticienAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praticien);

        // Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPraticiens);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation avec une liste vide pour éviter l'erreur de RecyclerView
        praticienAdapter = new PraticienAdapter(new ArrayList<>(), new PraticienAdapter.OnActionListener() {
            @Override
            public void onViewMore(Praticien praticien) {
                // Lancer l'activité de détails pour le praticien sélectionné
                Intent intent = new Intent(PraticienActivity.this, PraticienDetailActivity.class);
                intent.putExtra("PRA_ID", praticien.getId());
                startActivity(intent);
            }

            @Override
            public void onCreateReport(Praticien praticien) {
                Intent intent = new Intent(PraticienActivity.this, CreerRapportActivity.class);
                intent.putExtra("PRA_ID", praticien.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(praticienAdapter);

        // Chargement des praticiens
        fetchPraticiens();
    }

    private void fetchPraticiens() {
        // Récupérer le token stocké dans SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Vous devez être connecté pour voir cette page", Toast.LENGTH_SHORT).show();
            return;
        }

        // Utiliser ApiClient avec le token pour obtenir les praticiens
        GSBApi api = ApiClient.getApiService(token); // Utiliser le client avec le token

        Call<List<Praticien>> call = api.getPraticiens();

        call.enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mettre à jour l'adapter avec la liste de praticiens récupérée
                    praticienAdapter.updateData(response.body());
                } else {
                    Toast.makeText(PraticienActivity.this, "Erreur lors du chargement des praticiens", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                Log.e("API_ERROR", "Échec du chargement des praticiens", t);
                Toast.makeText(PraticienActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}