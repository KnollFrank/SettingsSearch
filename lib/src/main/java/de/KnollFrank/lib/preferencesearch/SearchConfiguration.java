package de.KnollFrank.lib.preferencesearch;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SearchConfiguration {

    private FragmentActivity activity;
    @IdRes
    private int fragmentContainerViewId = View.NO_ID;
    private String textHint;
    private Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;

    /**
     * Sets the current activity that also receives callbacks
     *
     * @param activity The Activity that receives callbacks. Must implement SearchPreferenceResultListener.
     */
    public void setActivity(final FragmentActivity activity) {
        this.activity = activity;
        if (!(activity instanceof SearchPreferenceResultListener)) {
            throw new IllegalArgumentException("Activity must implement SearchPreferenceResultListener");
        }
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    /**
     * Sets the container to use when loading the fragment
     *
     * @param fragmentContainerViewId Resource id of the container
     */
    public void setFragmentContainerViewId(@IdRes final int fragmentContainerViewId) {
        this.fragmentContainerViewId = fragmentContainerViewId;
    }

    @IdRes
    public int getFragmentContainerViewId() {
        return fragmentContainerViewId;
    }

    public void setRootPreferenceFragment(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        this.rootPreferenceFragment = rootPreferenceFragment;
    }

    public Class<? extends PreferenceFragmentCompat> getRootPreferenceFragment() {
        return rootPreferenceFragment;
    }

    public void setTextHint(final String textHint) {
        this.textHint = textHint;
    }

    public String getTextHint() {
        return textHint;
    }
}
