package com.example.erreparseparatas.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                Log.e("JSON_OBJECT", object.toString());
                sendPushNotification(object);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {

            String title = "Errepar";
            String id = json.get("id").toString();
            String titulo = json.get("titulo").toString();
            String descripcion = json.get("descripcion").toString();
            String link = json.get("link").toString();
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            mNotificationManager.showSmallNotification(titulo, id,descripcion,descripcion,link);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}
