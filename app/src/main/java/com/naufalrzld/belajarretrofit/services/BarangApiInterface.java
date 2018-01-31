package com.naufalrzld.belajarretrofit.services;

import com.naufalrzld.belajarretrofit.model.barang.BarangResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Naufal on 30/01/2018.
 */

public interface BarangApiInterface {
    @GET("barang/getAllBarang/{idToko}")
    Call<BarangResponse> getAllBarang(@Path("idToko") String idToko);

    @POST("barang/addBarang")
    Call<String> APIAddBarang(@Body JSONObject params);
}
