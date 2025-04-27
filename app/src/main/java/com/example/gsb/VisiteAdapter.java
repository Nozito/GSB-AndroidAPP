package com.example.gsb;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisiteAdapter extends RecyclerView.Adapter<VisiteAdapter.ViewHolder> {

    private List<Visite> visites;
    private OnActionListener listener;

    public interface OnActionListener {
        void onEditVisite(Visite visite);  // Déclenchement de l'action de modification
    }

    public VisiteAdapter(List<Visite> visites, OnActionListener listener) {
        this.visites = visites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Visite visite = visites.get(position);

        // Formatage de la date sans l'heure
        String formattedDate = formatDate(visite.getDateVisite());

        // Mettre à jour les TextViews
        holder.textMotif.setText(visite.getMotif() != null ? visite.getMotif().getLibelle() : "Non précisé");
        holder.textCommentaire.setText(visite.getCommentaire() != null ? visite.getCommentaire() : "Aucun commentaire");
        holder.textDateVisite.setText(formattedDate);

        // Clic sur le bouton Modifier pour ouvrir le BottomSheet
        holder.buttonModifierVisite.setOnClickListener(v -> {
            listener.onEditVisite(visite);  // Cette méthode appellera la méthode dans PraticienDetailActivity
        });

        // Clic sur le bouton Supprimer pour supprimer la visite
        holder.buttonSupprimerVisite.setOnClickListener(v -> {
            // Animation de fade-out
            Animation fadeOut = AnimationUtils.loadAnimation(v.getContext(), R.anim.item_fade_out);
            holder.itemView.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    visites.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                    // Récupérer le token depuis SharedPreferences
                    SharedPreferences prefs = v.getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String token = prefs.getString("TOKEN", null);

                    if (token == null) {
                        Toast.makeText(v.getContext(), "Token non trouvé, vous devez être connecté", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GSBApi api = ApiClient.getApiService(token);
                    api.deleteVisite(visite.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Visite supprimée", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "Erreur de suppression", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(v.getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return visites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button buttonSupprimerVisite;
        Button buttonModifierVisite;
        TextView textMotif, textCommentaire, textDateVisite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMotif = itemView.findViewById(R.id.textMotif);
            textCommentaire = itemView.findViewById(R.id.textCommentaire);
            textDateVisite = itemView.findViewById(R.id.textDateVisite);
            buttonModifierVisite = itemView.findViewById(R.id.buttonModifierVisite);
            buttonSupprimerVisite = itemView.findViewById(R.id.buttonSupprimerVisite);  // Initialisation du bouton Supprimer
        }
    }

    private String formatDate(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(inputFormat.parse(dateStr));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}