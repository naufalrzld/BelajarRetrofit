package com.naufalrzld.belajarretrofit.utils;

import com.naufalrzld.belajarretrofit.model.APIErrorModel;
import com.naufalrzld.belajarretrofit.services.APIClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Naufal on 09/01/2018.
 */

public class APIErrorUtils {
    public static APIErrorModel parserError(Response<?> response) {
        Converter<ResponseBody, APIErrorModel> converter = APIClient.getApiClient()
                .responseBodyConverter(APIErrorModel.class, new Annotation[0]);

        APIErrorModel error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIErrorModel();
        }

        return error;
    }
}
