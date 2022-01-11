package com.example.erreparseparatas.utils;

import com.example.erreparseparatas.interfaces.APIService;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://applibros.errepar.com/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
