package com.example.gsb;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicamentAdapter medicamentAdapter;
    private GSBApi api;
    private List<Medicament.Posologie> posologies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicaments);

        recyclerView = findViewById(R.id.recyclerViewMedicaments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupération du token pour les appels API
        String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TOKEN", null);
        api = ApiClient.getApiService(token);

        fetchMedicaments();
    }

    private void fetchMedicaments() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Log.e("fetchMedicaments", "Token non trouvé, utilisateur non connecté");
            Toast.makeText(this, "Vous devez être connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        GSBApi api = ApiClient.getApiService(token);
        Log.d("fetchMedicaments", "Appel API lancé");

        api.getAllMedicaments().enqueue(new Callback<List<Medicament>>() {
            @Override
            public void onResponse(@NonNull Call<List<Medicament>> call, @NonNull Response<List<Medicament>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicament> medicaments = response.body();
                    Log.d("fetchMedicaments", "Nombre de médicaments reçus : " + medicaments.size());
                    for (Medicament med : medicaments) {
                        Log.d("Medicament", "→ " + med.getNomCommercial() + " (" + med.getDepotLegal() + ")");
                    }

                    medicamentAdapter = new MedicamentAdapter(medicaments, MedicamentsActivity.this::showBottomSheet);
                    recyclerView.setAdapter(medicamentAdapter);
                } else {
                    Log.e("fetchMedicaments", "Réponse invalide ou vide : " + response.code());
                    Toast.makeText(MedicamentsActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Medicament>> call, @NonNull Throwable t) {
                Log.e("fetchMedicaments", "Erreur réseau : " + t.getMessage(), t);
                Toast.makeText(MedicamentsActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheet(Medicament medicament) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams")
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_medicament_details, null);

        // Référencer les TextViews
        TextView textNomCommercial = sheetView.findViewById(R.id.textNomCommercial);
        TextView textDepotLegal = sheetView.findViewById(R.id.textDepotLegal);
        TextView textFamille = sheetView.findViewById(R.id.textFamille);
        TextView textEffets = sheetView.findViewById(R.id.textEffets);
        TextView textContreIndications = sheetView.findViewById(R.id.textContreIndications);
        TextView textInteractions = sheetView.findViewById(R.id.textInteractions);
        TextView textPosologie = sheetView.findViewById(R.id.textPosologie);

        // Remplir les données dans le BottomSheet
        textNomCommercial.setText(medicament.getNomCommercial());
        textDepotLegal.setText(medicament.getDepotLegal());
        textFamille.setText(medicament.getFamille());
        textEffets.setText(medicament.getEffets());
        textContreIndications.setText(medicament.getContreIndications());
        textInteractions.setText(String.join(", ", medicament.getInteractions()));

        // Correction ici pour afficher correctement la posologie
        if (medicament.getPosologies() != null && !medicament.getPosologies().isEmpty()) {
            StringBuilder posoBuilder = new StringBuilder();
            for (Medicament.Posologie p : medicament.getPosologies()) {
                if (p.getTypeIndividu() != null && p.getDose() != null) {
                    posoBuilder.append("- ")
                            .append(p.getTypeIndividu())
                            .append(" : ")
                            .append(p.getDose())
                            .append("\n");
                }
            }
            textPosologie.setText(posoBuilder.toString().trim());
        } else {
            textPosologie.setText("Aucune posologie disponible");
        }

        // Afficher le BottomSheet
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}