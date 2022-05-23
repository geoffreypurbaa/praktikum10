package com.example.praktikum10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.praktikum10.api.ApiConfig;
import com.example.praktikum10.model.AddMahasiswaResponse;
import com.example.praktikum10.model.Mahasiswa;
import com.example.praktikum10.model.MahasiswaResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edtNrp;
    private EditText edtNama;
    private EditText edtEmail;
    private EditText edtJurusan;
    private ProgressBar progressBar;
    private Button btnAdd;
    private Button btnListData;
    private MahasiswaAdapter adapter;
    private List<Mahasiswa> mahasiswaModelList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNrp = findViewById(R.id.edtNrp);
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtJurusan = findViewById(R.id.edtJurusan);
        progressBar = findViewById(R.id.progressBar);
        btnAdd = findViewById(R.id.btnAdd);
        btnListData = findViewById(R.id.btnList);
        btnAdd.setOnClickListener(view -> {
            addDataMahasiswa();
        });
        btnListData.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,
                    SearchMahasiswaActivity.class);
            startActivity(intent);
        });
        myRecyclerView();
    }
    @Override
    protected void onStart(){
        super.onStart();
        getDataMahasiswa();
    }
    private void getDataMahasiswa(){
        showLoading(true);
        mahasiswaModelList = new ArrayList<>();
        Call<MahasiswaResponse> client = ApiConfig.getApiService().getMahasiswa();
        client.enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        showLoading(false);
                        mahasiswaModelList = response.body().getData();
                        adapter = new MahasiswaAdapter(mahasiswaModelList,MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    if (response.body() != null){
                        Log.e("","onFailure: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
showLoading(false);
Log.e("Error Retrofit", "onFailure: " + t.getMessage());
            }
        });
    }
    private void myRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void addDataMahasiswa() {
        showLoading(true);
        String nrp = edtNrp.getText().toString();
        String nama = edtNama.getText().toString();
        String email = edtEmail.getText().toString();
        String jurusan = edtJurusan.getText().toString();
        if (nrp.isEmpty() || nama.isEmpty() ||
                email.isEmpty() || jurusan.isEmpty()){
            Toast.makeText(MainActivity.this, "Silahkan lengkapi form terlebih dahulu",
                    Toast.LENGTH_SHORT).show();
            showLoading(false);
        }else {
            Call<AddMahasiswaResponse> client =
                    ApiConfig.getApiService().addMahasiswa(nrp, nama, email,
                            jurusan);
            client.enqueue(new Callback<AddMahasiswaResponse>() {
                                       @Override
                                       public void
                                       onResponse(Call<AddMahasiswaResponse> call,
                                                  Response<AddMahasiswaResponse> response) {
                                           showLoading(false);
                                           if (response.isSuccessful()){
                                               if (response.body() != null){

                                                   Toast.makeText(MainActivity.this, "Berhasil menambahakan silahakan cek data pada halaman list!",
                                                   Toast.LENGTH_SHORT).show();
                                               }
                                           } else {
                                               if (response.body() != null) {
                                                   Log.e("", "onFailure: " +
                                                           response.body().getMessage());
                                               }
                                           }
                                       }
                                       @Override
                                       public void
                                       onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                                           showLoading(false);
                                           Log.e("Error Retrofit", "onFailure: "
                                                   + t.getMessage());
                                       }
                                   });
        }
    }
    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}