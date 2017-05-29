package com.example.angelo.doorbelliot;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.angelo.doorbelliot.MyAdapter;
import com.example.angelo.doorbelliot.R;

import java.util.ArrayList;


/**
 * Created by angelo on 08/05/17.
 */

public class CronologiaFragment extends android.support.v4.app.Fragment  {
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "CronologiaFragment";


    public CronologiaFragment () {
        if ( mAdapter == null )
            mAdapter= new MyAdapter(new ArrayList<String>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.crono_layout,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }




    public static void addMessageStatic(String payload) {
        Log.d(TAG,"messaggio: " + payload);
        if ( mAdapter == null )
            mAdapter= new MyAdapter(new ArrayList<String>());
        ((MyAdapter)mAdapter).add(payload);


    }

}
