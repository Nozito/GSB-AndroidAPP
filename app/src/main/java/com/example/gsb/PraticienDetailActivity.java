package com.example.gsb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PraticienDetailActivity extends AppCompatActivity {

    private TextView textNomPrenom, textEmail;
    private RecyclerView recyclerViewVisites;
    private VisiteAdapter visiteAdapter;
    private List<Visite> listeVisites = new ArrayList<>();

    private String praticienId, nom, prenom, email;
    private String visiteurId = null; // A récupérer depuis la session
    private Map<String, String> motifIdMap = new HashMap<>(); // Libelle -> ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praticien_detail);

        textNomPrenom = findViewById(R.id.textNomPrenom);
        textEmail = findViewById(R.id.textEmail);
        recyclerViewVisites = findViewById(R.id.recyclerViewVisites);

        Button btnCreateReport = findViewById(R.id.btnCreateReport);
        btnCreateReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreerRapportActivity.class);
            intent.putExtra("PRA_ID", praticienId);
            startActivity(intent);
        });

        praticienId = getIntent().getStringExtra("PRA_ID");
        nom = getIntent().getStringExtra("PRA_NOM");
        prenom = getIntent().getStringExtra("PRA_PRENOM");
        email = getIntent().getStringExtra("PRA_EMAIL");

        textNomPrenom.setText(prenom + " " + nom);
        textEmail.setText(email);

        visiteAdapter = new VisiteAdapter(listeVisites, this::showBottomSheet);
        recyclerViewVisites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVisites.setAdapter(visiteAdapter);

        chargerVisitesParPraticien(praticienId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerVisitesParPraticien(praticienId);
    }

    private void chargerVisitesParPraticien(String idPraticien) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            Toast.makeText(this, "Vous devez être connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        GSBApi api = ApiClient.getApiService(token);
        api.getVisites().enqueue(new Callback<List<Visite>>() {
            @Override
            public void onResponse(@NonNull Call<List<Visite>> call, @NonNull Response<List<Visite>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listeVisites.clear();
                    for (Visite visite : response.body()) {
                        if (visite.getPraticien() != null && visite.getPraticien().getId().equals(idPraticien)) {
                            listeVisites.add(visite);
                        }
                    }
                    visiteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PraticienDetailActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Visite>> call, @NonNull Throwable t) {
                Toast.makeText(PraticienDetailActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBottomSheet(Visite visite) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_visite_detail, null);

        Spinner spinnerMotifs = sheetView.findViewById(R.id.spinnerMotifs);
        EditText editDateVisite = sheetView.findViewById(R.id.editDateVisite);
        EditText editCommentaire = sheetView.findViewById(R.id.editCommentaire);
        Button btnUpdate = sheetView.findViewById(R.id.btnUpdateVisite);

        editCommentaire.setText(visite.getCommentaire());
        editDateVisite.setText(formatDate(visite.getDateVisite()));

        fetchMotifs(spinnerMotifs, visite);

        btnUpdate.setOnClickListener(v -> {
            String selectedMotif = spinnerMotifs.getSelectedItem().toString();
            String motifId = motifIdMap.get(selectedMotif);
            String date = editDateVisite.getText().toString().trim();
            String commentaire = editCommentaire.getText().toString().trim();

            if (!motifId.isEmpty() && !date.isEmpty() && !commentaire.isEmpty()) {
                updateVisite(visite.getId(), motifId, date, commentaire, dialog);
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setContentView(sheetView);
        dialog.show();
        sheetView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
    }

    private void fetchMotifs(Spinner spinner, Visite visite) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);
        GSBApi api = ApiClient.getApiService(token);

        api.getAllMotifs().enqueue(new Callback<List<Motif>>() {
            @Override
            public void onResponse(@NonNull Call<List<Motif>> call, @NonNull Response<List<Motif>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> libelles = new ArrayList<>();
                    for (Motif motif : response.body()) {
                        motifIdMap.put(motif.getLibelle(), motif.getMotifId());
                        libelles.add(motif.getLibelle());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PraticienDetailActivity.this, android.R.layout.simple_spinner_item, libelles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    if (visite.getMotif() != null) {
                        int position = adapter.getPosition(visite.getMotif().getLibelle());
                        if (position >= 0) spinner.setSelection(position);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Motif>> call, @NonNull Throwable t) {
                Toast.makeText(PraticienDetailActivity.this, "Erreur de chargement des motifs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisite(String id, String motifId, String date, String commentaire, BottomSheetDialog dialog) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);
        visiteurId = prefs.getString("VISITEUR_ID", null);

        String formattedDate = formatDateForApi(date);
        VisiteRequest request = new VisiteRequest(commentaire, formattedDate, motifId, visiteurId, praticienId);

        GSBApi api = ApiClient.getApiService(token);
        Log.d("DEBUG_UPDATE", "→ Envoi de mise à jour :");
        Log.d("DEBUG_UPDATE", "commentaire = " + commentaire);
        Log.d("DEBUG_UPDATE", "date_visite = " + formattedDate);
        Log.d("DEBUG_UPDATE", "motif = " + motifId);
        Log.d("DEBUG_UPDATE", "visiteurId = " + visiteurId);
        Log.d("DEBUG_UPDATE", "praticienId = " + praticienId);
        api.updateVisite(id, request).enqueue(new Callback<Visite>() {
            @Override
            public void onResponse(Call<Visite> call, Response<Visite> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PraticienDetailActivity.this, "Visite mise à jour", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    chargerVisitesParPraticien(praticienId);
                } else {
                    Toast.makeText(PraticienDetailActivity.this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Visite> call, Throwable t) {
                Toast.makeText(PraticienDetailActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            Date date = input.parse(dateStr);
            return output.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    private String formatDateForApi(String dateStr) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = input.parse(dateStr);
            return output.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}