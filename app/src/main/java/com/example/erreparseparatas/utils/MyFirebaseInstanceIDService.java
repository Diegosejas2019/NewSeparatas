package com.example.erreparseparatas.utils;

import com.google.firebase.messaging.FirebaseMessagingService;



public class MyFirebaseInstanceIDService  {

    private static final String TAG = "MyFirebaseIIDService";

    /*@Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
    }
*/
    private void storeToken(String token) {
        //saving the token on shared preferences
        //SharedPreference.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}