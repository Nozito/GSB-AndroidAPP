package com.example.gsb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PraticienAdapter extends RecyclerView.Adapter<PraticienAdapter.ViewHolder> {

    private List<Praticien> praticiens;
    private OnActionListener listener;

    public interface OnActionListener {
        void onViewMore(Praticien praticien);
        void onCreateReport(Praticien praticien);
    }

    public PraticienAdapter(List<Praticien> praticiens, OnActionListener listener) {
        this.praticiens = praticiens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_praticien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Praticien praticien = praticiens.get(position);
        holder.nomPrenom.setText(praticien.getPrenom() + " " + praticien.getNom());
        holder.adresseMail.setText(praticien.getEmail());

        holder.btnViewMore.setOnClickListener(v -> listener.onViewMore(praticien));
        holder.btnCreateReport.setOnClickListener(v -> listener.onCreateReport(praticien));
    }

    @Override
    public int getItemCount() {
        return praticiens.size();
    }

    public void updateData(List<Praticien> newList) {
        praticiens.clear();
        praticiens.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, adresseMail;
        Button btnViewMore, btnCreateReport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenom = itemView.findViewById(R.id.textNomPrenom);
            adresseMail = itemView.findViewById(R.id.textAdresseMail);
            btnViewMore = itemView.findViewById(R.id.btn_view_more);
            btnCreateReport = itemView.findViewById(R.id.btn_create_report);
        }
    }
}