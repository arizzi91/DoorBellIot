package com.example.angelo.doorbelliot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.eclipse.paho.android.service.MqttAndroidClient;

import org.eclipse.paho.android.service.MqttAndroidClient;

/**
 * Created by angelo on 19/05/17.
 */

public class PublishFragment extends android.support.v4.app.Fragment {
    String query= "";
    private final static String TOPIC_CONNECT="query";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.publish_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button button= (Button)view.findViewById(R.id.btn_query);
        final EditText time_from=(EditText)view.findViewById(R.id.time_from);
        final EditText time_to=(EditText)view.findViewById(R.id.time_to);
        final EditText date_from=(EditText)view.findViewById(R.id.date_from);
        final EditText date_to=(EditText)view.findViewById(R.id.date_to);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeFrom= time_from.getText().toString();
                String timeTo= time_to.getText().toString();
                String dateFrom= date_from.getText().toString();
                String dateTo= date_to.getText().toString();

                query="SELECT PATH FROM IMAGES WHERE TIMESTAMP BETWEEN TIMESTAMP('"+dateFrom+" "+timeFrom+"') AND TIMESTAMP('"+dateTo+" "+timeTo+"');";

                Connection connection= new Connection(getContext(), SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF),
                        SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF),
                        TOPIC_CONNECT);
                connection.publish(query,TOPIC_CONNECT);



            }
        });

    }

}
