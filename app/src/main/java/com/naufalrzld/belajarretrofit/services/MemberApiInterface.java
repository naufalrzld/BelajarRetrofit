package com.naufalrzld.belajarretrofit.services;

import com.naufalrzld.belajarretrofit.model.member.MemberResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Naufal on 08/01/2018.
 */

public interface MemberApiInterface {
    @POST("/member/register")
    Call<MemberResponse> APIRegister(@Body JSONObject param);

    @POST("/member/login")
    Call<MemberResponse> APILogin(@Body JSONObject param);
}
