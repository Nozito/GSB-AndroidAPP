package com.example.gsb;

public class Visite {

    private String _id;
    private String date_visite;
    private String commentaire;
    private Visiteur visiteur;
    private Praticien praticien;
    private Motif motif;

    // Getters
    public String getId() {
        return _id;
    }

    public String getDateVisite() {
        return date_visite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Visiteur getVisiteur() {
        return visiteur;
    }

    public Praticien getPraticien() {
        return praticien;
    }

    public Motif getMotif() {
        return motif;
    }
}
