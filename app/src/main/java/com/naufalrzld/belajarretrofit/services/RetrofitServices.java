package com.naufalrzld.belajarretrofit.services;

/**
 * Created by Naufal on 08/01/2018.
 */

public class RetrofitServices {
    public static MemberApiInterface sendMemberRequset() {
        return APIClient.getApiClient().create(MemberApiInterface.class);
    }

    public static TokoApiInterface sendTokoRequest() {
        return APIClient.getApiClient().create(TokoApiInterface.class);
    }

    public static BarangApiInterface sendBarangRequest() {
        return APIClient.getApiClient().create(BarangApiInterface.class);
    }
}
