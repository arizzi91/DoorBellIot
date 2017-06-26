package com.example.angelo.servicemqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.angelo.data.SharedPreferencesSingleton;


/**
 *Re-start service after boot of smartphone
 */
public class BootBroadcast extends BroadcastReceiver {
    /**
     * Receive intent to start service at boot of smartphone
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent= new Intent(context,MyMqttService.class);
        /**
         * @see SharedPreferencesSingleton#init(Context)
         */
        SharedPreferencesSingleton.init(context);
        myIntent.putExtra(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF));
        myIntent.putExtra(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF));
        myIntent.putExtra(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.TOPIC_DEF));
        /**
         * @see MyMqttService#onCreate()
         * @see MyMqttService#onStartCommand(Intent, int, int)
         */
        context.startService(myIntent);
    }

}
