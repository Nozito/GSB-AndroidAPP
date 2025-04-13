import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PraticienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PraticienAdapter praticienAdapter;
    private Button btnCreatePractitioner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praticien);

        recyclerView = findViewById(R.id.recyclerView);
        btnCreatePractitioner = findViewById(R.id.btn_create_practitioner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnCreatePractitioner.setOnClickListener(v -> {
            Intent intent = new Intent(PraticienActivity.this, AddPraticienActivity.class);
            startActivity(intent);
        });

        fetchPraticiens();
    }

    private void fetchPraticiens() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.100:8080/api/") // Remplace par l’IP correcte de ton API locale
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getPraticiens().enqueue(new Callback<List<Praticien>>() {
            @Override
            public void onResponse(Call<List<Praticien>> call, Response<List<Praticien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    praticienAdapter = new PraticienAdapter(response.body());
                    recyclerView.setAdapter(praticienAdapter);
                } else {
                    Toast.makeText(PraticienActivity.this, "Erreur de récupération", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Praticien>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(PraticienActivity.this, "Connexion échouée", Toast.LENGTH_SHORT).show();
            }
        });
    }
}