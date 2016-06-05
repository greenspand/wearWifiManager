package ro.sorin.wifimanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


/**
 * Created by sorin on 20.12.14.
 */
public class WearFragAdapter extends FragmentPagerAdapter {


    private String[] TITLES = {"Color Picker"};
    private ArrayList<Fragment> fragments = new ArrayList<>(1);

    public WearFragAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TITLES.length;
    }

}

