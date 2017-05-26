package com.example.angelo.doorbelliot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by angelo on 18/05/17.
 */

public class NotificationService extends Service{
    private static final String TAG="Service";
    private MqttAndroidClient androidClient;
    private Uri sound = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"service stop");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.hasExtra("CLIENT") && intent.hasExtra("SERVER") && intent.hasExtra("TOPIC")){
            Connection connection= new Connection(getApplicationContext(),
                    intent.getStringExtra("CLIENT"),
                    intent.getStringExtra("SERVER"),
                    intent.getStringExtra("TOPIC"));

            androidClient=connection.createClient();
            connection.connect(androidClient);
            this.messageArrived(androidClient);
        }else{
            SharedPreferencesSingleton.init(getBaseContext());
            Connection connection=new Connection(getApplicationContext(),
                    SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF),
                    SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF),
                    SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.TOPIC_DEF));
            androidClient=connection.createClient();
            connection.connect(androidClient);
            this.messageArrived(androidClient);
        }


        return START_STICKY_COMPATIBILITY;
    }

    private void messageArrived(MqttAndroidClient androidClient) {
        androidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connessione persa");
                Intent lost_intent= new Intent(getBaseContext(),NotificationService.class);
                getBaseContext().stopService(lost_intent);
                SharedPreferencesSingleton.init(getBaseContext());
                SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                Toast.makeText(getBaseContext(),"connessione persa",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "messaggio");

                NotificationCompat.Builder builder= new NotificationCompat.Builder(getBaseContext());
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("Notifica IotApp");
                builder.setContentText("Immagine arrivata");
                builder.setSound(sound);
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_VIBRATE);

                /*Intent resultIntent = new Intent(getBaseContext(), MainActivity.class);
                resultIntent.putExtra("messaggio",new String(message.getPayload()));
                Log.d(TAG,resultIntent.getStringExtra("messaggio"));
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
// Adds the back stack
                stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent to the top of the stack
                stackBuilder.addNextIntent(resultIntent);

// Gets a PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


                builder.setContentIntent(resultPendingIntent);*/

                NotificationManager NM= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NM.notify(0,builder.build());



                CronologiaFragment.addMessage(new String(message.getPayload()));
                Toast.makeText(getApplicationContext(),"messaggio arrivato"+new String(message.getPayload()),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }



}
