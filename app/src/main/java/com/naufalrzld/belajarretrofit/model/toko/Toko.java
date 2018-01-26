package com.naufalrzld.belajarretrofit.model.toko;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naufal on 26/01/2018.
 */

public class Toko {
    @SerializedName("idToko")
    @Expose
    private Integer idToko;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("namaToko")
    @Expose
    private String namaToko;
    @SerializedName("descToko")
    @Expose
    private String descToko;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Integer getIdToko() {
        return idToko;
    }

    public void setIdToko(Integer idToko) {
        this.idToko = idToko;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getDescToko() {
        return descToko;
    }

    public void setDescToko(String descToko) {
        this.descToko = descToko;
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
