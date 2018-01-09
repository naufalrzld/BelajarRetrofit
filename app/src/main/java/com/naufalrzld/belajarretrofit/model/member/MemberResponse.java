package com.naufalrzld.belajarretrofit.model.member;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naufal on 09/01/2018.
 */

public class MemberResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Member data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }

}
