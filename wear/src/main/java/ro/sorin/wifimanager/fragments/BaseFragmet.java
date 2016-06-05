package ro.sorin.wifimanager.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by sorin on 2/7/16.
 */
public abstract class BaseFragmet extends Fragment {
    private final static String TAG = BaseFragmet.class.getName();

    public abstract int getLayoutID();

    public abstract String printLogMessage();

    public abstract String getFragmentName();
}
