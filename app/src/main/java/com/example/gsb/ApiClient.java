package com.example.gsb;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:3000"; // Utilise cette adresse pour Android Emulator
    private static Retrofit retrofit;

    // Création du client Retrofit avec le token dans les headers
    public static Retrofit getClient(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    // Ajouter le token à l'en-tête Authorization si présent
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token) // Ajoute le token ici
                            .build();
                    return chain.proceed(request);
                })
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)  // Utilisation du client OkHttp avec interceptor
                    .build();
        }

        return retrofit;
    }

    // Méthode pour récupérer le service API
    public static GSBApi getApiService(String token) {
        return getClient(token).create(GSBApi.class);
    }

    // Si tu veux créer une version sans token (pour les requêtes non protégées comme login)
    public static Retrofit getClientWithoutAuth() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static GSBApi getApiServiceWithoutAuth() {
        return getClientWithoutAuth().create(GSBApi.class);
    }


}