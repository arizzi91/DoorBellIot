package com.example.angelo.doorbelliot;



import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.angelo.fragments.CronologiaFragment;
import com.example.angelo.fragments.NewConnectionFragment;
import com.example.angelo.servicemqtt.MyMqttService;

/**
 * Main Activity for application.
 */

public class MainActivity extends AppCompatActivity implements NewConnectionFragment.PassValues {
    /**
     * Debug Tag for use logging debug output to LogCat
     */
    private static final String TAG="MainActivity";
    /**
     * Variable for Pager Adapter
     * @see SectionsPagerAdapter
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * Layout manager that allows the user to flip left and right through pages of data. You supply an implementation of a PagerAdapter to generate the pages that the view shows.
     */
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
        SharedPreferencesSingleton.init(getApplicationContext());

        /**
         * @see MainActivity#onNewIntent(Intent)
         */
        onNewIntent(getIntent());

    }


    /**
     * Override passage method of the interface PassValue. This method get parameters from NewConnectionFragment fragment  and use them to start service.
     *
     * @see com.example.angelo.fragments.NewConnectionFragment.PassValues
     * @param client ID of the client
     * @param server URL of server
     * @param topic name of topic
     */
    @Override
    public void passage(final String client, final String server, final String topic) {

        Intent mIntent= new Intent(getApplicationContext(), MyMqttService.class);
        mIntent.putExtra(SharedPreferencesSingleton.SERVER,server);
        mIntent.putExtra(SharedPreferencesSingleton.CLIENT,client);
        mIntent.putExtra(SharedPreferencesSingleton.TOPIC,topic);
        getApplicationContext().startService(mIntent);

    }

    /**
     * Get intent form service and pass extras to CronologiaFragment
     * @param intent intent
     */
    public void onNewIntent(Intent intent){

        try{
            if(intent.hasExtra(SharedPreferencesSingleton.MESSAGGIO)){
                mViewPager.setCurrentItem(1);

                CronologiaFragment cronologiaFragment= (CronologiaFragment)mSectionsPagerAdapter.getItem(1);
                /**
                 * @see CronologiaFragment#addMessage(String)
                 */
                cronologiaFragment.addMessage(intent.getStringExtra(SharedPreferencesSingleton.MESSAGGIO));
            }else if(intent.hasExtra(SharedPreferencesSingleton.QUERY_RESULT)){
                mViewPager.setCurrentItem(1);

                CronologiaFragment cronologiaFragment= (CronologiaFragment)mSectionsPagerAdapter.getItem(1);
                String [] queryArrived= intent.getStringArrayExtra(SharedPreferencesSingleton.QUERY_RESULT);
                for(int i=0; i<queryArrived.length; i++){

                    /**
                     * @see CronologiaFragment#addMessage(String)
                     */
                    cronologiaFragment.addMessage(queryArrived[i]);
                    Log.d(TAG,queryArrived[i]);
                }
            }
        }catch (NullPointerException e){
            e.getMessage();
        }




    }





}