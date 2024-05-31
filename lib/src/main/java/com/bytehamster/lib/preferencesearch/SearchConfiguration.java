package com.bytehamster.lib.preferencesearch;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SearchConfiguration {

    private final List<String> bannedKeys = new ArrayList<>();
    private boolean historyEnabled = true;
    private String historyId = null;
    private boolean breadcrumbsEnabled = false;
    private boolean fuzzySearchEnabled = true;
    private boolean searchBarEnabled = true;
    private FragmentActivity activity;
    @IdRes
    private int fragmentContainerViewId = View.NO_ID;
    @IdRes
    private int dummyFragmentContainerViewId = View.NO_ID;
    private RevealAnimationSetting revealAnimationSetting = null;
    private String textClearHistory;
    private String textNoResults;
    private String textHint;
    private Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments = Collections.emptySet();

    SearchConfiguration() {
    }

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
     * Show a history of recent search terms if nothing was typed yet. Default is true
     *
     * @param historyEnabled True if history should be enabled
     */
    public void setHistoryEnabled(final boolean historyEnabled) {
        this.historyEnabled = historyEnabled;
    }

    boolean isHistoryEnabled() {
        return historyEnabled;
    }

    /**
     * Sets the id to use for saving the history. Preference screens with the same history id will share the same
     * history. The default id is null (no id).
     *
     * @param historyId the history id
     */
    public void setHistoryId(final String historyId) {
        this.historyId = historyId;
    }

    String getHistoryId() {
        return historyId;
    }

    /**
     * Allow to enable and disable fuzzy searching. Default is true
     *
     * @param fuzzySearchEnabled True if search should be fuzzy
     */
    public void setFuzzySearchEnabled(final boolean fuzzySearchEnabled) {
        this.fuzzySearchEnabled = fuzzySearchEnabled;
    }

    boolean isFuzzySearchEnabled() {
        return fuzzySearchEnabled;
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
     * Show the search bar above the list. When setting this to false, you have to use {@see SearchPreferenceFragment#setSearchTerm(String) setSearchTerm} instead
     * Default is true
     *
     * @param searchBarEnabled True if search bar should be shown
     */
    public void setSearchBarEnabled(final boolean searchBarEnabled) {
        this.searchBarEnabled = searchBarEnabled;
    }

    boolean isSearchBarEnabled() {
        return searchBarEnabled;
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

    public void setRevealAnimationSetting(final RevealAnimationSetting revealAnimationSetting) {
        this.revealAnimationSetting = revealAnimationSetting;
    }

    RevealAnimationSetting getRevealAnimationSetting() {
        return revealAnimationSetting;
    }

    public void setPreferenceFragments(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments) {
        this.preferenceFragments = preferenceFragments;
    }

    public Set<Class<? extends PreferenceFragmentCompat>> getPreferenceFragments() {
        return preferenceFragments;
    }

    /**
     * @param key of the preference to be ignored
     */
    public void ignorePreference(@NonNull final String key) {
        bannedKeys.add(key);
    }

    List<String> getBannedKeys() {
        return bannedKeys;
    }

    public void setTextClearHistory(final String textClearHistory) {
        this.textClearHistory = textClearHistory;
    }

    public String getTextClearHistory() {
        return textClearHistory;
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
