package com.example.angelo.doorbelliot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by angelo on 29/05/17.
 */

public class NotificationBuild {
    private final static String TAG ="NotificationBuild";

    public static void buildNotification(String message, Context context){
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notifica IotApp");
        builder.setContentText("Immagine arrivata"+ message);
        builder.setSound(Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.door_bell_sound));
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(SharedPreferencesSingleton.MESSAGGIO,message);

        Log.d(TAG,resultIntent.getStringExtra(SharedPreferencesSingleton.MESSAGGIO));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager NM= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(0,builder.build());
    }
}
