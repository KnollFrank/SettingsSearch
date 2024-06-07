package de.KnollFrank.lib.preferencesearch;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Collections;
import java.util.Set;

public class SearchConfiguration {

    private boolean breadcrumbsEnabled = false;
    private FragmentActivity activity;
    @IdRes
    private int fragmentContainerViewId = View.NO_ID;
    @IdRes
    private int dummyFragmentContainerViewId = View.NO_ID;
    private String textNoResults;
    private String textHint;
    private Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments = Collections.emptySet();
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
     * Show breadcrumbs in the list of search results, containing of
     * the prefix given in addResourceFileToIndex, PreferenceCategory and PreferenceScreen.
     * Default is false
     *
     * @param breadcrumbsEnabled True if breadcrumbs should be shown
     */
    public void setBreadcrumbsEnabled(final boolean breadcrumbsEnabled) {
        this.breadcrumbsEnabled = breadcrumbsEnabled;
    }

    boolean isBreadcrumbsEnabled() {
        return breadcrumbsEnabled;
    }

    /**
     * Sets the container to use when loading the fragment
     *
     * @param fragmentContainerViewId Resource id of the container
     */
    public void setFragmentContainerViewId(@IdRes final int fragmentContainerViewId) {
        this.fragmentContainerViewId = fragmentContainerViewId;
    }

    public int getFragmentContainerViewId() {
        return fragmentContainerViewId;
    }

    public void setDummyFragmentContainerViewId(@IdRes final int dummyFragmentContainerViewId) {
        this.dummyFragmentContainerViewId = dummyFragmentContainerViewId;
    }

    public int getDummyFragmentContainerViewId() {
        return dummyFragmentContainerViewId;
    }

    public void setPreferenceFragments(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    public Set<Class<? extends PreferenceFragmentCompat>> getPreferenceFragments() {
        return preferenceFragments;
    }

    public void setRootPreferenceFragment(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        this.rootPreferenceFragment = rootPreferenceFragment;
    }

    public Class<? extends PreferenceFragmentCompat> getRootPreferenceFragment() {
        return rootPreferenceFragment;
    }

    public void setTextNoResults(final String textNoResults) {
        this.textNoResults = textNoResults;
    }

    public String getTextNoResults() {
        return textNoResults;
    }

    public void setTextHint(final String textHint) {
        this.textHint = textHint;
    }

    public String getTextHint() {
        return textHint;
    }
}
