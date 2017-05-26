package com.example.angelo.doorbelliot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by angelo on 08/05/17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {

    Fragment uno, due, tre;

    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(uno==null) uno=new NewConnectionFragment();
                return uno;
            case 1:
                if(due==null) due=new CronologiaFragment();
                return due;
            case 2:
                if(tre==null) tre= new PublishFragment();
                return tre;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Connetti";
            case 1:
                return "Cronologia";
            case 2:
                return "Query";
            default:
                break;
        }
        return null;
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
