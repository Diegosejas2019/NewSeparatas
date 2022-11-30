package com.example.mislibros.utils;

import com.example.mislibros.interfaces.APIService;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://applibros.errepar.com/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
