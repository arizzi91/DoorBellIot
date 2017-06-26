package com.example.angelo.servicemqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.example.angelo.data.MyDataModel;
import com.example.angelo.data.SharedPreferencesSingleton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 *This class handle the application service for handle the events of the connection
 */
public class MyMqttService extends Service{
    /**
     * Debug Tag for use logging debug output to LogCat
     */
    private static final String TAG="Service";

    private MqttAndroidClient androidClient;

    private MyDataModel dataModel= MyDataModel.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *Create the service
     */
    @Override
    public void onCreate() {

        super.onCreate();

    }

    /**
     *Destroy the service
     */
    @Override
    public void onDestroy() {
        Log.d(TAG,"service stop");
        super.onDestroy();
    }

    /**
     *Start service oof the application to handle Mqtt push notification
     * @param intent
     * @param flags
     * @param startId
     * @return START_STICKY_COMPATIBILITY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            if(intent != null && intent.hasExtra("CLIENT") && intent.hasExtra("SERVER") && intent.hasExtra("TOPIC")){
                Connection connection= new Connection(getApplicationContext(),
                        intent.getStringExtra("CLIENT"),
                        intent.getStringExtra("SERVER"),
                        intent.getStringExtra("TOPIC"));
                /**
                 * Create an android client
                 * @see Connection#createClient()
                 */
                androidClient=connection.createClient();
                /**
                 * Create a connection to broker
                 * @see Connection#connect(MqttAndroidClient)
                 */
                connection.connect(androidClient);
                /**
                 * @see MyMqttService#messageArrived(MqttAndroidClient)
                 */
                this.messageArrived(androidClient);
                /**
                 * If intent is null start service with SharedPreferences
                 */
            }else{
                /**
                 * Init SharedPreferences
                 * @see SharedPreferencesSingleton#init(Context)
                 */
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

    /**
     *Handle new message arrived
     * @param androidClient
     */
    private void messageArrived(MqttAndroidClient androidClient) {
        androidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connessione persa");
                Intent lost_intent= new Intent(getBaseContext(),MyMqttService.class);
                getBaseContext().stopService(lost_intent);
                SharedPreferencesSingleton.init(getBaseContext());
                SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                dataModel.setMyData(SharedPreferencesSingleton.MESS_STATUS_DEF);
                Toast.makeText(getBaseContext(),"connessione persa",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "messaggio dal topic "+topic+" contenente "+new String(message.getPayload()));



                /**
                 * Get payload of mqtt message and put it in a string
                 */
                String messageArrived= new String(message.getPayload());


                /**
                 * Control if message is result of a query
                 */
                if(messageArrived.contains("::")){
                    String [] messagesQuery=  messageArrived.split("::");
                    /**
                     * Create notification
                     * @see NotificationBuild#buildNotificationQuery(String[], Context)
                     */
                    NotificationBuild.buildNotificationQuery(messagesQuery,getBaseContext());
                }else{


                    /**
                     * Create notification
                     * @see NotificationBuild#buildNotification(String, Context)
                     */




                    NotificationBuild.buildNotification(messageArrived,getBaseContext());
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }




}
