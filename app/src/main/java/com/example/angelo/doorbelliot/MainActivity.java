package com.example.angelo.doorbelliot;



import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;



public class MainActivity extends AppCompatActivity implements NewConnectionFragment.PassValues {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG="MainActivity";




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


    }



    @Override
    public void passage(final String client, final String server, final String topic) {

        Intent mIntent= new Intent(getApplicationContext(), NotificationService.class);
        mIntent.putExtra("SERVER",server);
        mIntent.putExtra("CLIENT",client);
        mIntent.putExtra("TOPIC",topic);
        getApplicationContext().startService(mIntent);




    }




}