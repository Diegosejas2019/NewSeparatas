package com.example.mislibros.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.mislibros.MainActivity;
import com.example.mislibros.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager extends Application {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 234;
    public static final String CHANNEL_ID = "my_channel_01";
    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showSmallNotification(String title, String id, String titulo, String descripcion, String link) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mCtx);
        //if (!prefs.getString("suscriptor", "").equals("") && !prefs.getString("clave", "").equals("")) {
        //if (!prefs.getString("suscriptor", "").equals("") && !prefs.getString("clave", "").equals("")) {
        //if (!prefs.getString("suscriptor", "").equals("") && !prefs.getString("clave", "").equals("")) {
        //if (!prefs.getString("suscriptor", "").equals("") && !prefs.getString("clave", "").equals("")) {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent resultIntent = new Intent(mCtx, MainActivity.class);
            resultIntent.putExtra("publicacionid", id);
            resultIntent.putExtra("linkImg", link);

            PendingIntent resultPendingIntent = null;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mCtx);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            resultPendingIntent = PendingIntent.getActivity
                    (mCtx, ID_SMALL_NOTIFICATION, resultIntent, PendingIntent.FLAG_MUTABLE);
            resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            resultPendingIntent = PendingIntent.getActivity
                    (mCtx, ID_SMALL_NOTIFICATION, resultIntent, PendingIntent.FLAG_ONE_SHOT);
            resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        }




            NotificationManager mNotificationManager =
                    (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= 32) {
                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                mNotificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mCtx,CHANNEL_ID);

            PowerManager pm = (PowerManager) mCtx.getSystemService(POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "errepar");
            wl.acquire();

            Notification notification;
            notification = mBuilder.setSmallIcon(R.drawable.ic_publicaciones).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setFullScreenIntent(resultPendingIntent,true)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_publicaciones)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.ic_publicaciones))
                    .setContentText(descripcion)
                    .setSound(soundUri)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setLights(Color.RED, 3000, 3000)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            int mId = Integer.parseInt(id);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("notificacion", id);
            editor.commit();
            wl.release();
            mNotificationManager.notify(mId, notification);


    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

