package com.example.angelo.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.angelo.servicemqtt.Connection;
import com.example.angelo.doorbelliot.R;
import com.example.angelo.doorbelliot.SharedPreferencesSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 *
 */


public class PublishFragment extends android.support.v4.app.Fragment {
    String query = "";


    private TextView mDisplayDateFrom;
    private DatePickerDialog.OnDateSetListener mDateSetListenerFrom;
    private TextView mDisplayTimeFrom;
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerFrom;

    private TextView mDisplayDateTo;
    private DatePickerDialog.OnDateSetListener mDateSetListenerTo;
    private TextView mDisplayTimeTo;
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerTo;


    private String timefrom="";
    private String timeto="";
    private String datefrom="";
    private String dateto="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_layout, container, false);
        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button button = (Button) view.findViewById(R.id.btn_query);
        mDisplayDateFrom = (TextView) view.findViewById(R.id.tvDateFrom);
        mDisplayTimeFrom = (TextView) view.findViewById(R.id.tvTimeFrom);
        mDisplayDateTo = (TextView) view.findViewById(R.id.tvDateTo);
        mDisplayTimeTo = (TextView) view.findViewById(R.id.tvTimeTo);


        mDisplayDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int yearFrom = cal.get(Calendar.YEAR);
                int monthFrom = cal.get(Calendar.MONTH);
                int dayFrom = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerFrom,
                        yearFrom, monthFrom, dayFrom);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearFrom, int monthFrom, int dayFrom) {
                datefrom=fixValueDate(dayFrom,monthFrom+1,yearFrom);
                mDisplayDateFrom.setText(datefrom);
            }
        };


        mDisplayDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int yearTo = cal.get(Calendar.YEAR);
                int monthTo = cal.get(Calendar.MONTH);
                int dayTo = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerTo,
                        yearTo, monthTo, dayTo);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yearTo, int monthTo, int dayTo) {
                dateto=fixValueDate(dayTo,monthTo+1,yearTo);
                mDisplayDateTo.setText(dateto);
            }
        };


        mDisplayTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hourFrom = cal.get(Calendar.HOUR_OF_DAY);
                int minuteFrom = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListenerFrom,
                        hourFrom, minuteFrom, DateFormat.is24HourFormat(getContext()));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mTimeSetListenerFrom = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourFrom, int minuteFrom) {

                timefrom=fixValueTime(hourFrom,minuteFrom);
                mDisplayTimeFrom.setText(timefrom);

            }
        };


        mDisplayTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hourTo = cal.get(Calendar.HOUR_OF_DAY);
                int minuteTo = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListenerTo,
                        hourTo, minuteTo, DateFormat.is24HourFormat(getContext()));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mTimeSetListenerTo = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourTo, int minuteTo) {
                timeto=fixValueTime(hourTo,minuteTo);
                mDisplayTimeTo.setText(timeto);
            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    Date datef = formatter.parse(datefrom+" "+timefrom);
                    Date datet = formatter.parse(dateto+" "+timeto);

                    if(datef.compareTo(datet)<0)
                    {
                        query="SELECT PATH FROM Images WHERE DATA_SCATTO BETWEEN TIMESTAMP('"+datefrom+" "+timefrom+"') AND TIMESTAMP('"+dateto+" "+timeto+"');";

                        Connection connection= new Connection(getContext(), SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.CLIENT,SharedPreferencesSingleton.CLIENT_DEF),
                                SharedPreferencesSingleton.getStringPreferences(SharedPreferencesSingleton.SERVER,SharedPreferencesSingleton.SERVER_DEF),
                                SharedPreferencesSingleton.TOPIC_QUERY_DEF);
                        connection.publish(query,SharedPreferencesSingleton.TOPIC_QUERY_DEF);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Valori non corretti",Toast.LENGTH_SHORT).show();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


        });


    }

    private String fixValueTime(int value1, int value2){
        String valueFix;
        if (value1<10){
            valueFix="0"+value1;
        }else{
            valueFix=""+value1;
        }

        if (value2<10){
            valueFix=valueFix+":"+"0"+value2;
        }else{
            valueFix=valueFix+":"+value2;
        }

        return valueFix;
    }

    private String fixValueDate(int value1, int value2, int value3){
        String valueFix=""+value3+"-";
        if (value2<10){
            valueFix=valueFix+"0"+value2;
        }else{
            valueFix=valueFix+""+value2;
        }

        if (value1<10){
            valueFix=valueFix+"-"+"0"+value1;
        }else{
            valueFix=valueFix+"-"+value1;
        }
        return valueFix;
    }


}
