package com.example.angelo.servicemqtt;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * This class create a single instance of android client
 */

public class SingletonClient {

    private static SingletonClient instance = null;
    private static MqttAndroidClient client = null;



    private SingletonClient(Context context){

    }

    /**
     * Create an android client
     * @param context
     * @param serverName
     * @param clientName
     * @return android client
     */
    public MqttAndroidClient createClient(Context context, String serverName, String clientName){
        client = new MqttAndroidClient(context,serverName,clientName+System.currentTimeMillis());
        return client;
    }

    /**
     *
     * @param context
     * @return single instance of android client
     */
    public synchronized static SingletonClient getInstance(Context context)
    {
        if (instance == null) {
            instance = new SingletonClient(context);
        }

        return instance;
    }

    /**
     * Get the instance of android client
     * @return android client
     */
    public static MqttAndroidClient getAndroidClient(){
        return client;
    }
}
