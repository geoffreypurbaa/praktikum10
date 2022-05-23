package com.example.praktikum10;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum10.api.ApiConfig;
import com.example.praktikum10.model.Mahasiswa;
import com.example.praktikum10.model.MahasiswaDeleteResponse;


import java.util.List;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {
    private Context context;
    private List<Mahasiswa> mahasiswaModelList;

    public MahasiswaAdapter(List<Mahasiswa> mahasiswaModelList, Context context) {
        this.mahasiswaModelList = mahasiswaModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa,parent,false);
        return new MahasiswaViewHolder(view);
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvNRP, tvEmail, tvJurusan;
        private CardView cvItem;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_item_nama);
            tvNRP = itemView.findViewById(R.id.tv_item_nrp);
            tvEmail = itemView.findViewById(R.id.tv_item_email);
            tvJurusan = itemView.findViewById(R.id.tv_item_jurusan);
            cvItem = itemView.findViewById(R.id.cv_item_note);
        }
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, @SuppressLint("Recyclerview") int position) {
        Mahasiswa mahasiswaModel = mahasiswaModelList.get(position);
        holder.tvNRP.setText(mahasiswaModel.getNrp());
        holder.tvNama.setText(mahasiswaModel.getNama());
        holder.tvEmail.setText(mahasiswaModel.getEmail());
        holder.tvJurusan.setText(mahasiswaModel.getJurusan());
        holder.cvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNRP", mahasiswaModelList.get(position).getNrp());
                                bundle.putString("dataNama", mahasiswaModelList.get(position).getNama());
                                bundle.putString("dataEmail", mahasiswaModelList.get(position).getEmail());
                                bundle.putString("dataJurusan", mahasiswaModelList.get(position).getJurusan());
                                bundle.putString("dataID", mahasiswaModelList.get(position).getId());
                                Intent intent = new Intent(view.getContext(), MahasiswaUpdateActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                deleteDataMahasiswa(mahasiswaModelList.get(position).getId());
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() { return mahasiswaModelList.size(); }

    private void deleteDataMahasiswa(String id) {
        Call<MahasiswaDeleteResponse> client = ApiConfig.getApiService().deleteMahasiswa(id);
        client.enqueue(new Callback<MahasiswaDeleteResponse> (){
            @Override
            public void onResponse(Call<MahasiswaDeleteResponse> call, Response<MahasiswaDeleteResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(context, "Behasil menghapus", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.body() != null) {
                        Log.e("", "onFailure: " +
                                response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MahasiswaDeleteResponse> call, Throwable t) {
                Log.e("Error Retrofit", "onFailure: " + t.getMessage());
            }
        });
    }
}


