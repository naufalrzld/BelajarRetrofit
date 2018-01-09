package com.naufalrzld.belajarretrofit.model;

/**
 * Created by Naufal on 09/01/2018.
 */

public class APIErrorModel {
    private int status;
    private String message;

    public APIErrorModel() {

    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
