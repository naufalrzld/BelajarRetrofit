package com.naufalrzld.belajarretrofit.services;

import com.naufalrzld.belajarretrofit.model.barang.BarangResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Naufal on 30/01/2018.
 */

public interface BarangApiInterface {
    @GET("barang/getAllBarang/{idToko}")
    Call<BarangResponse> getAllBarang(@Path("idToko") String idToko);
}
