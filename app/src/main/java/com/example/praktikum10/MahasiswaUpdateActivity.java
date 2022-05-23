package com.example.praktikum10;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum10.api.ApiConfig;
import com.example.praktikum10.model.AddMahasiswaResponse;
import com.example.praktikum10.model.MahasiswaUpdateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MahasiswaUpdateActivity extends AppCompatActivity {
private EditText nrpBaru , namaBaru, emailBaru, jurusanBaru;
private Button btnUpdate;
private String cekNRP, cekNama, cekEmail, cekJurusan;
private ProgressBar progressBar;

@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mahasiswa_update);

    nrpBaru = findViewById(R.id.et_nrp);
    namaBaru = findViewById(R.id.et_nama);
    emailBaru = findViewById(R.id.et_email);
    jurusanBaru = findViewById(R.id.et_jurusan);
    btnUpdate = findViewById(R.id.btn_update);
    progressBar = findViewById(R.id.pb_update);

    getData();
    btnUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cekNRP = nrpBaru.getText().toString();
            cekNama = namaBaru.getText().toString();
            cekEmail = emailBaru.getText().toString();
            cekJurusan = jurusanBaru.getText().toString();
            if (isEmpty(cekNRP) || isEmpty(cekNama) || isEmpty(cekEmail) || isEmpty(cekJurusan)) {
                Toast.makeText(MahasiswaUpdateActivity.this, "Data kosong", Toast.LENGTH_SHORT).show();
            } else {
                updateDataMahasiswa();
            }
        }
    });
}
private boolean isEmpty(String s) {return TextUtils.isEmpty(s);}
    private void getData() {
        final String getNRP = getIntent().getExtras().getString("dataNRP");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getEmail = getIntent().getExtras().getString("dataEmail");
        final String getJurusan = getIntent().getExtras().getString("dataJurusan");
        nrpBaru.setText(getNRP);
        namaBaru.setText(getNama);
        emailBaru.setText(getEmail);
        jurusanBaru.setText(getJurusan);
    }
    private void updateDataMahasiswa(){
    showLoading(true);
    String id = getIntent().getExtras().getString("dataID");
    String nrp = nrpBaru.getText().toString();
    String nama = namaBaru.getText().toString();
    String email = emailBaru.getText().toString();
    String jurusan = jurusanBaru.getText().toString();
    if (nrp.isEmpty() || nama.isEmpty() ||email.isEmpty() || jurusan.isEmpty()) {
        Toast.makeText(MahasiswaUpdateActivity.this, "Lengkapi isian", Toast.LENGTH_SHORT).show();
        showLoading(false);
    }else{
        Call<MahasiswaUpdateResponse> client = ApiConfig.getApiService().updateMahasiswa(id, nrp, nama, email,jurusan);
        client.enqueue(new Callback<MahasiswaUpdateResponse>(){
            @Override
            public void
            onResponse(Call<MahasiswaUpdateResponse> call,
                       Response<MahasiswaUpdateResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Toast.makeText(MahasiswaUpdateActivity.this, "Berhasil meengubah data pada halaman list!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    if (response.body() != null) {
                        Log.e("", "onFailure: " +
                                response.body().getMessage());
                        finish();
                    }

            }
        }
        @Override
        public void
        onFailure(Call<MahasiswaUpdateResponse> call, Throwable t) {
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
