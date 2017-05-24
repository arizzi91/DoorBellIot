package com.example.angelo.doorbelliot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by angelo on 18/05/17.
 */

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent= new Intent(context,NotificationService.class);
        SharedPreferencesSingleton.init(context);
        myIntent.putExtra("CLIENT",SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF));
        myIntent.putExtra("SERVER",SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF));
        myIntent.putExtra("TOPIC",SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.TOPIC_DEF));
        context.startService(myIntent);
    }

}
