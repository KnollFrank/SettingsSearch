package com.bytehamster.lib.preferencesearch;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.util.Supplier;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// FK-TODO: refactor
public class SearchConfiguration {

    private final List<String> bannedKeys = new ArrayList<>();
    private boolean historyEnabled = true;
    private String historyId = null;
    private boolean breadcrumbsEnabled = false;
    private boolean fuzzySearchEnabled = true;
    private boolean searchBarEnabled = true;
    private FragmentActivity activity;
    private int containerResId = android.R.id.content;
    private RevealAnimationSetting revealAnimationSetting = null;
    private String textClearHistory;
    private String textNoResults;
    private String textHint;
    private Supplier<Set<Class<? extends PreferenceFragmentCompat>>> preferenceFragmentsSupplier = Collections::emptySet;

    SearchConfiguration() {
    }

    public SearchConfiguration(final FragmentActivity activity) {
        setActivity(activity);
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

    /**
     * Sets the id to use for saving the history. Preference screens with the same history id will share the same
     * history. The default id is null (no id).
     *
     * @param historyId the history id
     */
    public void setHistoryId(final String historyId) {
        this.historyId = historyId;
    }

    /**
     * Allow to enable and disable fuzzy searching. Default is true
     *
     * @param fuzzySearchEnabled True if search should be fuzzy
     */
    public void setFuzzySearchEnabled(final boolean fuzzySearchEnabled) {
        this.fuzzySearchEnabled = fuzzySearchEnabled;
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

    /**
     * Show the search bar above the list. When setting this to false, you have to use {@see SearchPreferenceFragment#setSearchTerm(String) setSearchTerm} instead
     * Default is true
     *
     * @param searchBarEnabled True if search bar should be shown
     */
    public void setSearchBarEnabled(final boolean searchBarEnabled) {
        this.searchBarEnabled = searchBarEnabled;
    }

    /**
     * Sets the container to use when loading the fragment
     *
     * @param containerResId Resource id of the container
     */
    public void setFragmentContainerViewId(@IdRes final int containerResId) {
        this.containerResId = containerResId;
    }

    public int getFragmentContainerViewId() {
        return containerResId;
    }

    /**
     * Display a reveal animation
     *
     * @param centerX     Origin of the reveal animation
     * @param centerY     Origin of the reveal animation
     * @param width       Size of the main container
     * @param height      Size of the main container
     * @param colorAccent Accent color to use
     */
    public void useAnimation(int centerX, int centerY, int width, int height, @ColorInt int colorAccent) {
        revealAnimationSetting = new RevealAnimationSetting(centerX, centerY, width, height, colorAccent);
    }

    public void setRevealAnimationSetting(final RevealAnimationSetting revealAnimationSetting) {
        this.revealAnimationSetting = revealAnimationSetting;
    }

    public void setPreferenceFragmentsSupplier(final Supplier<Set<Class<? extends PreferenceFragmentCompat>>> preferenceFragmentsSupplier) {
        this.preferenceFragmentsSupplier = preferenceFragmentsSupplier;
    }

    public Supplier<Set<Class<? extends PreferenceFragmentCompat>>> getPreferenceFragmentsSupplier() {
        return preferenceFragmentsSupplier;
    }

    List<String> getBannedKeys() {
        return bannedKeys;
    }

    /**
     * @param key of the preference to be ignored
     */
    public void ignorePreference(@NonNull String key) {
        bannedKeys.add(key);
    }

    boolean isHistoryEnabled() {
        return historyEnabled;
    }

    String getHistoryId() {
        return historyId;
    }

    boolean isBreadcrumbsEnabled() {
        return breadcrumbsEnabled;
    }

    boolean isFuzzySearchEnabled() {
        return fuzzySearchEnabled;
    }

    boolean isSearchBarEnabled() {
        return searchBarEnabled;
    }

    RevealAnimationSetting getRevealAnimationSetting() {
        return revealAnimationSetting;
    }

    public String getTextClearHistory() {
        return textClearHistory;
    }

    public void setTextClearHistory(String textClearHistory) {
        this.textClearHistory = textClearHistory;
    }

    public String getTextNoResults() {
        return textNoResults;
    }

    public void setTextNoResults(String textNoResults) {
        this.textNoResults = textNoResults;
    }

    public String getTextHint() {
        return textHint;
    }

    public void setTextHint(String textHint) {
        this.textHint = textHint;
    }
}
