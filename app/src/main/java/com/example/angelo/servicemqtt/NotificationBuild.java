package com.example.angelo.servicemqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.angelo.doorbelliot.MainActivity;
import com.example.angelo.doorbelliot.R;
import com.example.angelo.data.SharedPreferencesSingleton;

import java.util.Date;

/**
 * This class build notification when new message arrive at the client
 */

public class NotificationBuild {
    /**
     * Debug Tag for use logging debug output to LogCat
     */
    private final static String TAG ="NotificationBuild";

    /**
     *Build notification with the mqtt message and show it at the user
     * @param message
     * @param context
     */
    public static void buildNotification(String message, Context context){
        Log.d(TAG,"notifica normale");
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_door_notification);
        builder.setContentTitle("WhoRing");
        builder.setContentText("Immagine arrivata: "+ message);
        builder.setSound(Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.door_bell_sound));
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);


        /**
         * Create intent and pass it at MainActivity
         * @see MainActivity#onNewIntent(Intent)
         */
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(SharedPreferencesSingleton.MESSAGGIO,message);

        Log.d(TAG,resultIntent.getStringExtra(SharedPreferencesSingleton.MESSAGGIO));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(generateIDNotification()+1, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager NM= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(generateIDNotification(),builder.build());


    }

    /**
     * Method called when arrived result of a query
     * @param query
     * @param context
     */
    public static void buildNotificationQuery(String [] query,Context context){
        Log.d(TAG,"notifica con query");
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_door_notification);
        builder.setContentTitle("WhoRing-Query");
        /**
         * @see NotificationBuild#printMessage(String[])
         */
        builder.setContentText(printMessage(query));
        builder.setSound(Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.door_bell_sound));
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);



        /**
         * Create intent and pass it at MainActivity
         * @see MainActivity#onNewIntent(Intent)
         */

        Intent query_result= new Intent(context, MainActivity.class);
        query_result.putExtra(SharedPreferencesSingleton.QUERY_RESULT,query);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(query_result);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(generateIDNotification()+1, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager NM= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        /**
         * @see NotificationBuild#generateIDNotification()
         */
        NM.notify(generateIDNotification(),builder.build());
    }

    /**
     *
     * @param query
     * @return text of notification
     */
    private static String printMessage(String [] query) {
        String title="";
        for(int i=0; i<query.length; i++){
            title=title+query[i]+"\n.";
        }
        Log.d(TAG,title);
        return title;
    }

    /**
     * Generate unique ID for notification
     * @return ID notification
     */

    private static int generateIDNotification(){

        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }




}
