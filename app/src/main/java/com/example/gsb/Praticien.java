package com.example.gsb;

public class Praticien {
    private String _id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String rue;
    private String code_postal;
    private String ville;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getRue() {
        return rue;
    }

    public String getCodePostal() {
        return code_postal;
    }

    public String getVille() {
        return ville;
    }

    public String getAdresseComplete() {
        return rue + ", " + code_postal + " " + ville;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}