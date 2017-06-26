package com.example.angelo.servicemqtt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.angelo.data.MyDataModel;
import com.example.angelo.data.SharedPreferencesSingleton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 *Class connection provides all methods to create and manage connection to broker.
 */

public class Connection {
    /**
     * Debug Tag for use logging debug output to LogCat
     */
    private static final String TAG="Connection";
    private MqttAndroidClient androidClient=null;
    private Context context;
    private String clientName,serverName,topicName;
    private MyDataModel dataModel= MyDataModel.getInstance();

    /**
     *
     *
     * @param context context of application
     * @param client  clientID
     * @param server  server
     * @param topic   topic name
     */
    public Connection(Context context,String client,String server,String topic){
        this.context=context;
        this.clientName=client;
        this.serverName=server;
        this.topicName=topic;
    }


    /**
     *This method create a single instance of an android client.
     * @see SingletonClient#getInstance(Context)
     * @see SingletonClient#createClient(Context, String, String)
     * @return  single instance of android client.
     */
    public MqttAndroidClient createClient(){
        androidClient= SingletonClient.getInstance(context).createClient(context,serverName,clientName);
        Log.d(TAG,"client creato");
        return androidClient;

    }

    /**
     * This method create a connection with the broker.
     * @param androidClient
     */
    public void connect(final MqttAndroidClient androidClient){


        Log.d(TAG,androidClient.getClientId()+" "+androidClient.getServerURI());
        /**
         * Set connect options
         */
        final MqttConnectOptions mqttConnectOptions= new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setKeepAliveInterval(0);

        try {
            androidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG,"ok connesso");


                    /**
                     * Save server and client variables in the SharedPreferences
                     * @see SharedPreferencesSingleton#setStringPreferences(String, String)
                     */
                    SharedPreferencesSingleton.setStringPreferences(SharedPreferencesSingleton.CLIENT,androidClient.getClientId()).
                            setStringPreferences(SharedPreferencesSingleton.SERVER,androidClient.getServerURI());
                    /**
                     *  @see Connection#sottoscriviTopic()
                     */
                    sottoscriviTopic();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "la connessione non è riuscita "+exception.getMessage());
                    Toast.makeText(context, "la connessione non è riuscita "+exception.getMessage(),Toast.LENGTH_LONG).show();
                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                    /**
                     * Initializes the intent to stop the service
                     */
                    Intent conn_intent= new Intent(context,MyMqttService.class);
                    context.stopService(conn_intent);
                    //data model
                    dataModel.setMyData(SharedPreferencesSingleton.MESS_STATUS_DEF);
                }
            });
        } catch (MqttException e) {
            Log.d(TAG,e.getMessage());


        }

    }

    /**
     *Subscribe the topic
     */
    public void sottoscriviTopic(){

        try {

            androidClient.subscribe(topicName, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG,"ok sottoscritto");
                    Toast.makeText(context,"Connesso al broker: "+androidClient.getServerURI()+
                                    ".\nCon clientID: "+androidClient.getClientId()+
                                    ".\nSottoscritto al topic: "+topicName,
                            Toast.LENGTH_LONG).show();
                    //set data model
                    dataModel.setMyData("Connesso al broker: "+androidClient.getServerURI()+
                            ".\nCon clientID: "+androidClient.getClientId()+
                            ".\nSottoscritto al topic: "+topicName);

                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,true);

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

    /**
     *Disconnect from broker
     */
    public void disconnect() {

        androidClient= SingletonClient.getAndroidClient();
        try {
            androidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    SharedPreferencesSingleton.setBooleanPreferences(SharedPreferencesSingleton.STATUS,false);
                    Intent disc_intent= new Intent(context,MyMqttService.class);
                    context.stopService(disc_intent);
                    Log.d(TAG,"ok disconnesso");
                    Toast.makeText(context,"Sei disconnesso",Toast.LENGTH_LONG).show();

                    dataModel.setMyData(SharedPreferencesSingleton.MESS_STATUS_DEF);

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

    /**
     *Publish message on topic
     * @param publishMessage value of the message
     * @param topic name of topic
     */
    public void publish(String publishMessage, String topic){
        androidClient= SingletonClient.getAndroidClient();
        MqttMessage mqttMessage= new MqttMessage();
        mqttMessage.setPayload(publishMessage.getBytes());
        try {
            androidClient.publish(topic, mqttMessage);
            Log.d(TAG,"Messaggio pubblicato: "+publishMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG,"Messaggio non pubblicato "+e.getMessage());
            Toast.makeText(context,"Messaggio non pubblicato "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }




}
