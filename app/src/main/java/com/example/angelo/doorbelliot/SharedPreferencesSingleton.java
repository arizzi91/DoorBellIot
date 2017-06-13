package com.example.angelo.doorbelliot;

import android.content.Context;
import android.content.SharedPreferences;



/**
 *Class to handle shared preferences
 */
public class SharedPreferencesSingleton {

    private SharedPreferences mySHaredPref;
    private static SharedPreferencesSingleton currentInstance;

    public static final String FIRST_START="start";
    public static final String CLIENT="CLIENT";
    public static final String SERVER="SERVER";
    public static final String TOPIC="TOPIC";
    public static final String STATUS="STATUS";
    public static final String MESSAGGIO="messaggio";
    public static final String MESS_STATUS="status_conn";

    public static final String CLIENT_DEF="home";
    public static final String SERVER_DEF="192.168.1.69";
    public static final String TOPIC_DEF="ring";
    public static final String TOPIC_QUERY_DEF="query";
    public static final String MESS_STATUS_DEF="Non sei attualmente connesso";
    public static final String QUERY_RESULT = "query_result";
    public static final boolean STATUS_DEF=false;



    /**
     *
     * @param context
     */
    private SharedPreferencesSingleton(Context context){
        if(mySHaredPref==null){
            mySHaredPref=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        }
    }

    /**
     *Init SharedPrefernces
     * @param context
     * @return instance
     */
    public static SharedPreferencesSingleton init(Context context){
        if(currentInstance==null){
            currentInstance = new SharedPreferencesSingleton(context);
        }
        return currentInstance;
    }

    /**
     * Save string value
     * @param key
     * @param value
     * @return current instance of shared preferences
     */
    public static SharedPreferencesSingleton setStringPreferences(String key, String value){
        SharedPreferences.Editor editor= currentInstance.mySHaredPref.edit();
        editor.putString(key,value);
        editor.commit();
        return currentInstance;
    }

    /**
     * Save boolean value
     * @param key
     * @param value
     * @return current instance of shared preferences
     */
    public static SharedPreferencesSingleton setBooleanPreferences(String key, boolean value){
        SharedPreferences.Editor editor= currentInstance.mySHaredPref.edit();
        editor.putBoolean(key,value);
        editor.commit();
        return currentInstance;
    }

    /**
     * Get string value from preferences
     * @param key
     * @param def
     * @return string value
     */
    public static String getStringPreferences(String key,String def){
        return currentInstance.mySHaredPref.getString(key,def);
    }

    /**
     * Get boolean value from preferences
     * @param key
     * @param def
     * @return boolean value
     *
     */
    public static boolean getBooleanPreferences(String key, boolean def){
        return currentInstance.mySHaredPref.getBoolean(key,def);
    }


}
