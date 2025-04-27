package com.example.gsb;

public class VisiteRequest {
    private String commentaire;
    private String date_visite;
    private String motifId;
    private String visiteurId;
    private String praticienId;

    // Constructeur avec tous les champs
    public VisiteRequest(String commentaire, String date_visite, String motifId, String visiteurId, String praticienId) {
        this.commentaire = commentaire;
        this.date_visite = date_visite;
        this.motifId = motifId;
        this.visiteurId = visiteurId;
        this.praticienId = praticienId;
    }

    public VisiteRequest(String updatedCommentaire, String updatedDate, String updatedMotif) {
        this.commentaire = updatedCommentaire;
        this.date_visite = updatedDate;
        this.motifId = updatedMotif;
        this.visiteurId = visiteurId;
        this.praticienId = praticienId;
    }

    public VisiteRequest(String newDateVisite) {
        this.date_visite = newDateVisite;
    }

    // Getter et setter pour chaque champ
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getDate_visite() {
        return date_visite;
    }

    public void setDate_visite(String date_visite) {
        this.date_visite = date_visite;  // Assure-toi d'utiliser date_visite ici
    }

    public String getMotifId() {
        return motifId;
    }

    public void setMotifId(String motifId) {
        this.motifId = motifId;
    }

    public String getVisiteurId() {
        return visiteurId;
    }

    public void setVisiteurId(String visiteurId) {
        this.visiteurId = visiteurId;
    }

    public String getPraticienId() {
        return praticienId;
    }

    public void setPraticienId(String praticienId) {
        this.praticienId = praticienId;
    }

    @Override
    public String toString() {
        return "VisiteRequest{" +
                "commentaire='" + commentaire + '\'' +
                ", date_visite='" + date_visite + '\'' +  // Assurez-vous d'afficher date_visite ici
                ", motifId='" + motifId + '\'' +
                ", visiteurId='" + visiteurId + '\'' +
                ", praticienId='" + praticienId + '\'' +
                '}';
    }


}