package com.example.gsb;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreerRapportActivity extends AppCompatActivity {

    private EditText editDate, editCommentaire;
    private Spinner spinnerMotifs, spinnerPraticiens;

    private List<Motif> motifList = new ArrayList<>();
    private List<Praticien> praticienList = new ArrayList<>();

    private String selectedMotifId;
    private String selectedPraticienId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_rapport);

        // Liaison des vues
        editDate = findViewById(R.id.editDate);
        editCommentaire = findViewById(R.id.editCommentaire);
        spinnerMotifs = findViewById(R.id.spinnerMotifs);
        spinnerPraticiens = findViewById(R.id.spinnerPraticiens);

        editDate.setOnClickListener(v -> showDatePicker());

        fetchMotifs();
        fetchPraticiens();

        spinnerMotifs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMotifId = motifList.get(position).getMotifId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                selectedMotifId = null;
            }
        });

        spinnerPraticiens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPraticienId = praticienList.get(position).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                selectedPraticienId = null;
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            String formatted = new SimpleDateFormat("yyyy-MM-dd").format(selected.getTime()); // Format ISO sans heure
            editDate.setText(formatted);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void fetchMotifs() {
        String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TOKEN", null);
        GSBApi api = ApiClient.getApiService(token);

        api.getAllMotifs().enqueue(new Callback<List<Motif>>() {
            @Override
            public void onResponse(Call<List<Motif>> call, Response<List<Motif>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    motifList = response.body();
                    List<String> libelles = new ArrayList<>();
                    for (Motif m : motifList) libelles.add(m.getLibelle());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreerRapportActivity.this,
                            android.R.layout.simple_spinner_item, libelles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMotifs.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Motif>> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur chargement motifs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPraticiens() {
        String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TOKEN", null);
        GSBApi api = ApiClient.getApiService(token);

        api.getPraticiens().enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    praticienList = response.body();
                    List<String> noms = new ArrayList<>();
                    for (Praticien p : praticienList)
                        noms.add(p.getNom() + " " + p.getPrenom());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreerRapportActivity.this,
                            android.R.layout.simple_spinner_item, noms);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPraticiens.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur chargement praticiens", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void validerRapport(View view) {
        String date = editDate.getText().toString().trim();
        String commentaire = editCommentaire.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String visiteurId = prefs.getString("VISITEUR_ID", null);
        String token = prefs.getString("TOKEN", null);

        if (date.isEmpty() || commentaire.isEmpty() || selectedMotifId == null || selectedPraticienId == null) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        VisiteRequest request = new VisiteRequest(commentaire, date, selectedMotifId, visiteurId, selectedPraticienId);
        Log.d("DEBUG_RAPPORT", "Création rapport : " + request);

        GSBApi api = ApiClient.getApiService(token);
        api.createVisite(request).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(@NonNull Call<Visite> call, @NonNull Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreerRapportActivity.this, "Rapport créé avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("CreerRapportActivity", "Erreur création: " + response.code() + " - " + response.message());
                    Toast.makeText(CreerRapportActivity.this, "Erreur lors de la création", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Visite> call, Throwable t) {
                Toast.makeText(CreerRapportActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}