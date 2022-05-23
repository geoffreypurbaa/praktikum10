package com.example.praktikum10.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MahasiswaResponse {
    @SerializedName("data")
    private List<Mahasiswa> data;
    @SerializedName("status")
    private boolean status;
    @SerializedName("nama")
    private String nama;
    @SerializedName("nrp")
    private String nrp;
    @SerializedName("email")
    private String email;
    @SerializedName("jurusan")
    private String jurusan;
    @SerializedName("id")
    private String id;


    public List<Mahasiswa> getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getNama() {
    return nama;
    }

    public String getNrp() {
        return nrp;
    }

    public String getEmail() {
    return email;
    }

    public String getJurusan() {
    return jurusan;
    }

    public String getId() {
    return id;
    }
}
