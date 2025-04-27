package com.example.gsb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder> {

    public interface OnMedicamentClickListener {
        void onMedicamentClick(Medicament medicament);
    }

    private final List<Medicament> medicaments;
    private final OnMedicamentClickListener listener;

    public MedicamentAdapter(List<Medicament> medicaments, OnMedicamentClickListener listener) {
        this.medicaments = medicaments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicament, parent, false);
        return new MedicamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.nomCommercial.setText(medicament.getNomCommercial());
        holder.depotLegal.setText(medicament.getDepotLegal());
        holder.famille.setText(medicament.getFamille());

        holder.btnVoirPlus.setOnClickListener(v -> listener.onMedicamentClick(medicament));
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    public static class MedicamentViewHolder extends RecyclerView.ViewHolder {
        TextView nomCommercial, depotLegal, famille;
        Button btnVoirPlus;

        public MedicamentViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCommercial = itemView.findViewById(R.id.textNomCommercial);
            depotLegal = itemView.findViewById(R.id.textDepotLegal);
            famille = itemView.findViewById(R.id.textFamille);
            btnVoirPlus = itemView.findViewById(R.id.btnVoirPlus);
        }
    }
}
