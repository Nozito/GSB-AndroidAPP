package com.example.gsb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textNomPrenom;
    private RecyclerView recyclerViewPraticiens;
    private PraticienAdapter praticienAdapter;
    private List<Praticien> praticiensList = new ArrayList<>();
    private Button btnVoirPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        textNomPrenom = findViewById(R.id.textNomPrenom);
        recyclerViewPraticiens = findViewById(R.id.recyclerViewPraticiens);
        btnVoirPlus = findViewById(R.id.btnVoirPlus);
        Button btnMedicament = findViewById(R.id.btnMedicaments);

        // Affichage des infos utilisateur
        displayUserInfo();

        recyclerViewPraticiens.setLayoutManager(new LinearLayoutManager(this));
        praticienAdapter = new PraticienAdapter(praticiensList, new PraticienAdapter.OnActionListener() {
            @Override
            public void onViewMore(Praticien praticien) {
                Intent intent = new Intent(MainActivity.this, PraticienDetailActivity.class);
                intent.putExtra("PRA_ID", praticien.getId());
                intent.putExtra("PRA_NOM", praticien.getNom());
                intent.putExtra("PRA_PRENOM", praticien.getPrenom());
                intent.putExtra("PRA_EMAIL", praticien.getEmail());
                startActivity(intent);
            }

            @Override
            public void onCreateReport(Praticien praticien) {
                Intent intent = new Intent(MainActivity.this, CreerRapportActivity.class);
                intent.putExtra("PRA_ID", praticien.getId());
                startActivity(intent);
            }
        });
        recyclerViewPraticiens.setAdapter(praticienAdapter);

        loadRecentPraticiens();

        btnVoirPlus.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PraticienActivity.class);
            startActivity(intent);
        });
        btnMedicament.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedicamentsActivity.class);
            startActivity(intent);
        });
    }
    //bouton pour retrouvez nos echantillons

    private void displayUserInfo() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String nom = prefs.getString("USER_NOM", "Nom");
        String prenom = prefs.getString("USER_PRENOM", "Prénom");
        textNomPrenom.setText(prenom + " " + nom);
    }

    private void loadRecentPraticiens() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Vous devez être connecté pour voir les praticiens.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TOKEN_MAIN", "Token récupéré depuis SharedPreferences : " + token);  // <-- log ajouté

        GSBApi apiService = ApiClient.getApiService(token);
        Call<List<Praticien>> call = apiService.getPraticiens();

        call.enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Praticien> all = response.body();
                    Log.d("API_DEBUG", "Nb praticiens reçus : " + all.size());  // <-- log ajouté

                    praticiensList.clear();
                    praticiensList.addAll(all.subList(0, Math.min(3, all.size())));
                    praticienAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API_DEBUG", "Erreur API : " + response.code());  // <-- log ajouté
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                Log.e("API_DEBUG", "Erreur réseau : " + t.getMessage());  // <-- log ajouté
            }
        });
    }
}