package com.example.gsb;

import java.util.List;

public class Medicament {
    private String id;
    private String nomCommercial;
    private String depotLegal;
    private String effets;
    private String contreIndications;
    private List<Composant> composition;
    private List<String> interactions;
    private List<Posologie> posologies;
    private String famille;

    // Getters
    public String getId() {
        return id;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public String getDepotLegal() {
        return depotLegal;
    }

    public String getEffets() {
        return effets;
    }

    public String getContreIndications() {
        return contreIndications;
    }

    public List<Composant> getComposition() {
        return composition;
    }

    public List<String> getInteractions() {
        return interactions;
    }

    public List<Posologie> getPosologies() {
        return posologies;
    }

    public String getFamille() {
        return famille;
    }

    // Inner classes
    public static class Composant {
        private String composant;
        private String quantite;

        public String getComposant() {
            return composant;
        }

        public String getQuantite() {
            return quantite;
        }
    }

    public static class Posologie {
        private String typeIndividu;
        private String dose;

        public String getTypeIndividu() {
            return typeIndividu;
        }

        public String getDose() {
            return dose;
        }
    }
}