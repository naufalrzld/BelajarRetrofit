package com.naufalrzld.belajarretrofit.model.barang;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naufal on 30/01/2018.
 */

public class Barang {
    @SerializedName("kodeBarang")
    @Expose
    private Integer kodeBarang;
    @SerializedName("idToko")
    @Expose
    private Integer idToko;
    @SerializedName("namaBarang")
    @Expose
    private String namaBarang;
    @SerializedName("stok")
    @Expose
    private Integer stok;
    @SerializedName("harga")
    @Expose
    private Integer harga;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Integer getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(Integer kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public Integer getIdToko() {
        return idToko;
    }

    public void setIdToko(Integer idToko) {
        this.idToko = idToko;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
