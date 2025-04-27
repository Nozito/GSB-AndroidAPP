package com.example.gsb;

public class Visiteur {
    private String _id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String emailHash;
    private String date_embauche;
    private String password;

    public String getId() {
        return _id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public String getDateEmbauche() {
        return date_embauche;
    }

    public String getPassword() {
        return password;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}