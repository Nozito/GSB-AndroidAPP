package com.example.gsb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GSBApi {

    // Connexion
    @POST("api/visiteurs/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Récupération de tous les praticiens
    @GET("api/praticiens/")
    Call<List<Praticien>> getPraticiens();

    // Récupération de tous les motifs
    @GET("api/motifs")
    Call<List<Motif>> getAllMotifs();

    // Récupération de toutes les visites
    @GET("api/visites")
    Call<List<Visite>> getVisites();

    //Suppression d'une visite
    @DELETE("api/visites/{id}")
    Call<Void> deleteVisite(@Path("id") String visiteId);

    // Récupération d'une visite par ID
    @GET("api/visites/{id}")
    Call<Visite> getVisiteById(@Path("id") String visiteId);

    // Création d'une visite
    @POST("api/visites")
    Call<Visite> createVisite(@Body VisiteRequest request);

    // Mise à jour d'une visite existante
    @PUT("api/visites/{id}")
    Call<Visite> updateVisite(@Path("id") String visiteId, @Body VisiteRequest request);

    @GET("api/medicaments")
    Call<List<Medicament>> getAllMedicaments();
}