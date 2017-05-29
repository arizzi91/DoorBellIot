package com.example.angelo.doorbelliot;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by angelo on 16/05/17.
 */

public class SharedPreferencesSingleton {
    private SharedPreferences mySHaredPref;
    private static SharedPreferencesSingleton currentInstance;

    public static final String CLIENT="CLIENT";
    public static final String SERVER="SERVER";
    public static final String TOPIC="TOPIC";
    public static final String STATUS="STATUS";
    public static final String MESSAGGIO="messaggio";

    public static final String CLIENT_DEF="home";
    public static final String SERVER_DEF="192.168.1.103";
    public static final String TOPIC_DEF="prova";
    public static final boolean STATUS_DEF=false;





    private SharedPreferencesSingleton(Context context){
        if(mySHaredPref==null){
            mySHaredPref=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        }
    }
    public static SharedPreferencesSingleton init(Context context){
        if(currentInstance==null){
            currentInstance = new SharedPreferencesSingleton(context);
        }
        return currentInstance;
    }

    public static SharedPreferencesSingleton setStringPreferences(String key, String value){
        SharedPreferences.Editor editor= currentInstance.mySHaredPref.edit();
        editor.putString(key,value);
        editor.commit();
        return currentInstance;
    }

    public static SharedPreferencesSingleton setBooleanPreferences(String key, boolean value){
        SharedPreferences.Editor editor= currentInstance.mySHaredPref.edit();
        editor.putBoolean(key,value);
        editor.commit();
        return currentInstance;
    }

    public static String getStringPreferences(String key,String def){
        return currentInstance.mySHaredPref.getString(key,def);
    }

    public static boolean getBooleanPreferences(String key, boolean def){
        return currentInstance.mySHaredPref.getBoolean(key,def);
    }


}
