package com.example.angelo.doorbelliot;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

/**
 * Created by angelo on 21/05/17.
 */

public class Message extends MqttMessage{
    private MqttMessage mqttMessage;
    private  static ArrayList<String> mqttMessageArrayList = new ArrayList<>();
    private String link;
    private static final String TAG="Array Message";


    public Message(MqttMessage message){
        this.mqttMessage= message;
        link= new String(message.getPayload());
        mqttMessageArrayList.add(link);


    }


    public static void showSize(){
        Log.d(TAG, String.valueOf(mqttMessageArrayList.size()));
    }

    public static String [] getStringsFromArray(){
        String [] titles= new String[0];
        for (int i=0; i<mqttMessageArrayList.size();i++){
            titles[i]=mqttMessageArrayList.get(i);
            mqttMessageArrayList.remove(i);
        }
        return titles;
    }



}
