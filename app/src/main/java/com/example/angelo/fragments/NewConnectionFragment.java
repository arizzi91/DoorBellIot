package com.example.angelo.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.servicemqtt.Connection;
import com.example.angelo.doorbelliot.R;
import com.example.angelo.doorbelliot.SharedPreferencesSingleton;


/**
 *
 */

public class NewConnectionFragment extends android.support.v4.app.Fragment {
    private EditText client, server, port, topic;
    private Button conn,disc;
    private TextView status;
    String clientName,serverName, topicName;
    int portName;
    PassValues pass;
    private static final String TAG="NewConnectionFragment";


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.new_connection, container, false);
        status=(TextView)view.findViewById(R.id.status_connection);


        /**
         * @see NewConnectionFragment#updateStatus()
         */
        updateStatus();
        return view;
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        client=(EditText)view.findViewById(R.id.clientId);
        server=(EditText)view.findViewById(R.id.serverURI);
        port=(EditText)view.findViewById(R.id.port);
        topic=(EditText)view.findViewById(R.id.sub);
        conn=(Button)view.findViewById(R.id.btn_newConnection);
        disc=(Button)view.findViewById(R.id.btn_disconnect);

        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ""+ SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,SharedPreferencesSingleton.STATUS_DEF));
                if(parseValori(client.getText().toString(),server.getText().toString(),port.getText().toString(),topic.getText().toString())){
                    clientName=client.getText().toString();
                    serverName=server.getText().toString();
                    portName=Integer.parseInt(port.getText().toString());
                    topicName=topic.getText().toString();
                    serverName="tcp://"+serverName+":"+portName;
                    if(SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,SharedPreferencesSingleton.STATUS_DEF)){
                        Toast.makeText(getContext(),"Disconnettiti prima di avviare una nuova connessione.",Toast.LENGTH_LONG).show();
                        Log.d(TAG,"sei già connesso");


                    }else{
                        pass.passage(clientName,serverName,topicName);
                        updateStatus();

                    }
                }else{
                    Toast.makeText(getContext(),"inserire correttamente i campi",Toast.LENGTH_LONG).show();

                }

            }
        });

        disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ""+SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,SharedPreferencesSingleton.STATUS_DEF));

                if(SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,true)){
                    Connection connection=new Connection(getContext(),SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,"client"),
                            SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,"server"),
                            SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,"topic"));
                    connection.disconnect();
                    updateStatus();


                }else{
                    Log.d(TAG,"non sei connesso");
                    Toast.makeText(getContext(),"non sei connesso",Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    /**
     *
     * @param client
     * @param server
     * @param port
     * @param topic
     * @return
     */
    private boolean parseValori(String client, String server, String port, String topic) {
        boolean ok=false;
        if(client.equals("") || server.equals("")  || port.equals("") || topic.equals("")){
        }else if(client.length()<10) ok=true;
        return ok;

    }

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        pass=(PassValues)context;
    }

    /**
     *Interface to pass values
     */
    public interface PassValues{
        void passage (String client, String server,String topic);
    }

    /**
     * Shows current info about connection
     */

    public void updateStatus(){
        if(SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,false)){
            status.setText("Sei connesso al broker: "+SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF)+
            ".\nCon clientID: "+SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF)+
                    ".\nSottoscritto al topic: "+SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.TOPIC_DEF));
            status.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            status.setText("Non sei attualmente connesso");
            status.setTextColor(getResources().getColor(R.color.colorAccent));

        }

    }

}
