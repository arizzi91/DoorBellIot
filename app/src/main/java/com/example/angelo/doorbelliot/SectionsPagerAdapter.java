package com.example.angelo.doorbelliot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.angelo.fragments.CronologiaFragment;
import com.example.angelo.fragments.NewConnectionFragment;
import com.example.angelo.fragments.PublishFragment;



/**
 *This class implements Pager Adapter that represents each page as a Fragment that is persistently kept in the fragment manager as long as the user can return to the page.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {

    Fragment uno, due, tre;

    /**
     *
     * @param fm
     */
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Initializes fragments for each position
     * @param position
     * @return
     */
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

    /**
     *
     * @return number of fragments in the pager adapter
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Get page title
     * @param position
     * @return
     */
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




}
