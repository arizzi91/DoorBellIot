package com.example.angelo.doorbelliot;


import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;

/**
 * Created by angelo on 08/05/17.
 */

public class NewConnectionFragment extends android.support.v4.app.Fragment {
    private EditText client, server, port, topic;
    private Button conn,disc;
    String clientName,serverName, topicName;
    int portName;
    PassValues pass;
    private static final String TAG="NewConnectionFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.new_connection, container, false);
        return view;
    }

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
                if(parseValori(client.getText().toString(),server.getText().toString(),port.getText().toString(),topic.getText().toString())){
                    clientName=client.getText().toString();
                    serverName=server.getText().toString();
                    portName=Integer.parseInt(port.getText().toString());
                    topicName=topic.getText().toString();
                    serverName="tcp://"+serverName+":"+portName;
                    if(SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,SharedPreferencesSingleton.STATUS_DEF) &&
                            clientName.equals(SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF)) &&
                            serverName.equals(SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF)) &&
                            topicName.equals(SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,SharedPreferencesSingleton.TOPIC_DEF))){
                        Toast.makeText(getContext(),"sei già connesso con i parametri inseriti",Toast.LENGTH_LONG).show();
                        Log.d(TAG,"sei già connesso con i parametri inseriti");
                    }else{
                        pass.passage(clientName,serverName,topicName);
                    }
                }else Toast.makeText(getContext(),"inserire campi mancanti",Toast.LENGTH_LONG).show();






            }
        });

        disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreferencesSingleton.getBooleanPreferences(SharedPreferencesSingleton.STATUS,true)){
                    Connection connection=new Connection(getContext(),SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,"client"),
                            SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,"server"),
                            SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.TOPIC,"topic"));
                    connection.disconnect();

                }else{
                    Log.d(TAG,"non sei connesso");
                    Toast.makeText(getContext(),"non sei connesso",Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    private boolean parseValori(String client, String server, String port, String topic) {
        boolean ok=false;
        if(client.equals("") || server.equals("")  || port.equals("") || topic.equals("")){
        }else ok=true;
        return ok;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        pass=(PassValues)context;
    }

    public interface PassValues{
        public void passage (String client, String server,String topic);
    }
}