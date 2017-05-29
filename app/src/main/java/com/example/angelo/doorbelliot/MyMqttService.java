package com.example.angelo.doorbelliot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by angelo on 18/05/17.
 */

public class MyMqttService extends Service{
    private static final String TAG="Service";
    private MqttAndroidClient androidClient;

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
                Intent lost_intent= new Intent(getBaseContext(),MyMqttService.class);
                getBaseContext().stopService(lost_intent);
                SharedPreferencesSingleton.init(getBaseContext());
                SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                Toast.makeText(getBaseContext(),"connessione persa",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "messaggio dal topic "+topic+" contenente "+new String(message.getPayload()));
                String messageArrived= new String(message.getPayload());
                NotificationBuild.buildNotification(messageArrived,getBaseContext());
                CronologiaFragment.addMessageStatic(messageArrived);
                Toast.makeText(getApplicationContext(),"messaggio dal topic "+topic+" contenente "+messageArrived ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

}
