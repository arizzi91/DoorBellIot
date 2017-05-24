package com.example.angelo.doorbelliot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by angelo on 18/05/17.
 */

public class Connection {
    private MqttAndroidClient androidClient=null;
    private static final String TAG="Connection";
    private Context context;
    private String clientName,serverName,topicName;


    public Connection(Context context,String client,String server,String topic){
        this.context=context;
        this.clientName=client;
        this.serverName=server;
        this.topicName=topic;
    }



    public MqttAndroidClient createClient(){
        androidClient= SingletonClient.getInstance(context).createClient(context,serverName,clientName);
        Log.d(TAG,"client creato");
        return androidClient;

    }

    public void connect(final MqttAndroidClient androidClient){

        Log.d(TAG,androidClient.getClientId()+" "+androidClient.getServerURI());

        final MqttConnectOptions mqttConnectOptions= new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setKeepAliveInterval(0);

        try {
            androidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG,"ok connesso");



                    SharedPreferencesSingleton.setStringPreferences(SharedPreferencesSingleton.CLIENT,androidClient.getClientId()).
                            setStringPreferences(SharedPreferencesSingleton.SERVER,androidClient.getServerURI());
                    sottoscriviTopic();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "la connessione non è riuscita "+exception.getMessage());
                    Toast.makeText(context, "la connessione non è riuscita "+exception.getMessage(),Toast.LENGTH_LONG).show();
                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                    Intent conn_intent= new Intent(context,NotificationService.class);
                    context.stopService(conn_intent);
                }
            });
        } catch (MqttException e) {
            Log.d(TAG,e.getMessage());


        }

    }

    public void sottoscriviTopic(){

        try {

            androidClient.subscribe(topicName, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG,"ok sottoscritto");
                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,true).
                            setBooleanPreferences(SharedPreferencesSingleton.BOOT_SERVICE,true);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG,exception.getMessage());
                    Toast.makeText(context,"Non sei riuscito a sottoscriverti "+exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {

        androidClient= SingletonClient.getAndroidClient();
        try {
            androidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false).
                            setBooleanPreferences(SharedPreferencesSingleton.BOOT_SERVICE,false);
                    Intent disc_intent= new Intent(context,NotificationService.class);
                    context.stopService(disc_intent);
                    Log.d(TAG,"ok disconnesso");
                    Toast.makeText(context,"Sei disconnesso",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG,exception.getMessage());
                    Toast.makeText(context,"Non sei riuscito a disconnetterti "+exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }catch (NullPointerException nullpointer){
            Log.d(TAG,"Non hai creato nessun client");
            Toast.makeText(context,"Non hai creato nessun client",Toast.LENGTH_LONG).show();
        }
    }

    public void publish(String publishMessage, String topic){
        androidClient= SingletonClient.getAndroidClient();
        MqttMessage mqttMessage= new MqttMessage();
        mqttMessage.setPayload(publishMessage.getBytes());
        try {
            androidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG,"Messaggio non pubblicato "+e.getMessage());
            Toast.makeText(context,"Messaggio non pubblicato "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }




}
