package com.example.angelo.doorbelliot;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * Created by angelo on 08/05/17.
 */

public class SingletonClient {

    private static SingletonClient instance = null;
    private static MqttAndroidClient client = null;



    private SingletonClient(Context context){

    }


    public MqttAndroidClient createClient(Context context, String serverName, String clientName){
        client = new MqttAndroidClient(context,serverName,clientName);
        return client;
    }

    public synchronized static SingletonClient getInstance(Context context)
    {
        if (instance == null) {
            instance = new SingletonClient(context);
        }

        return instance;
    }


    public static MqttAndroidClient getAndroidClient(){
        return client;
    }
}
