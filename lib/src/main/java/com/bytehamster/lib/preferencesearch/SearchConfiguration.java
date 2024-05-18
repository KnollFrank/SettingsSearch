package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.XmlRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.bytehamster.lib.preferencesearch.ui.RevealAnimationSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// FK-TODO: refactor
public class SearchConfiguration {
    private static final String ARGUMENT_INDEX_FILES = "items";
    private static final String ARGUMENT_INDEX_INDIVIDUAL_PREFERENCES = "individual_prefs";
    private static final String ARGUMENT_FUZZY_ENABLED = "fuzzy";
    private static final String ARGUMENT_HISTORY_ENABLED = "history_enabled";
    private static final String ARGUMENT_HISTORY_ID = "history_id";
    private static final String ARGUMENT_SEARCH_BAR_ENABLED = "search_bar_enabled";
    private static final String ARGUMENT_BREADCRUMBS_ENABLED = "breadcrumbs_enabled";
    private static final String ARGUMENT_REVEAL_ANIMATION_SETTING = "reveal_anim_setting";
    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_TEXT_CLEAR_HISTORY = "text_clear_history";
    private static final String ARGUMENT_TEXT_NO_RESULTS = "text_no_results";

    private ArrayList<SearchIndexItem> filesToIndex = new ArrayList<>();
    private ArrayList<PreferenceItem> preferencesToIndex = new ArrayList<>();
    private final List<String> bannedKeys = new ArrayList<>();
    private boolean historyEnabled = true;
    private String historyId = null;
    private boolean breadcrumbsEnabled = false;
    private boolean fuzzySearchEnabled = true;
    private boolean searchBarEnabled = true;
    AppCompatActivity activity;
    private int containerResId = android.R.id.content;
    private RevealAnimationSetting revealAnimationSetting = null;
    private String textClearHistory;
    private String textNoResults;
    private String textHint;

    SearchConfiguration() {
    }

    public SearchConfiguration(AppCompatActivity activity) {
        setActivity(activity);
    }

    public SearchPreferenceFragment showSearchFragment() {
        if (activity == null) {
            throw new IllegalStateException("setActivity() not called");
        }

        final Bundle arguments = this.toBundle();
        final SearchPreferenceFragment fragment = new SearchPreferenceFragment();
        fragment.setArguments(arguments);
        activity.getSupportFragmentManager().beginTransaction()
                .add(containerResId, fragment, SearchPreferenceFragment.TAG)
                .addToBackStack(SearchPreferenceFragment.TAG)
                .commit();
        return fragment;
    }

    private Bundle toBundle() {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(ARGUMENT_INDEX_FILES, filesToIndex);
        arguments.putParcelableArrayList(ARGUMENT_INDEX_INDIVIDUAL_PREFERENCES, preferencesToIndex);
        arguments.putBoolean(ARGUMENT_HISTORY_ENABLED, historyEnabled);
        arguments.putParcelable(ARGUMENT_REVEAL_ANIMATION_SETTING, revealAnimationSetting);
        arguments.putBoolean(ARGUMENT_FUZZY_ENABLED, fuzzySearchEnabled);
        arguments.putBoolean(ARGUMENT_BREADCRUMBS_ENABLED, breadcrumbsEnabled);
        arguments.putBoolean(ARGUMENT_SEARCH_BAR_ENABLED, searchBarEnabled);
        arguments.putString(ARGUMENT_TEXT_HINT, textHint);
        arguments.putString(ARGUMENT_TEXT_CLEAR_HISTORY, textClearHistory);
        arguments.putString(ARGUMENT_TEXT_NO_RESULTS, textNoResults);
        arguments.putString(ARGUMENT_HISTORY_ID, historyId);
        return arguments;
    }

    static SearchConfiguration fromBundle(Bundle bundle) {
        SearchConfiguration config = new SearchConfiguration();
        config.filesToIndex = bundle.getParcelableArrayList(ARGUMENT_INDEX_FILES);
        config.preferencesToIndex = bundle.getParcelableArrayList(ARGUMENT_INDEX_INDIVIDUAL_PREFERENCES);
        config.historyEnabled = bundle.getBoolean(ARGUMENT_HISTORY_ENABLED);
        config.revealAnimationSetting = bundle.getParcelable(ARGUMENT_REVEAL_ANIMATION_SETTING);
        config.fuzzySearchEnabled = bundle.getBoolean(ARGUMENT_FUZZY_ENABLED);
        config.breadcrumbsEnabled = bundle.getBoolean(ARGUMENT_BREADCRUMBS_ENABLED);
        config.searchBarEnabled = bundle.getBoolean(ARGUMENT_SEARCH_BAR_ENABLED);
        config.textHint = bundle.getString(ARGUMENT_TEXT_HINT);
        config.textClearHistory = bundle.getString(ARGUMENT_TEXT_CLEAR_HISTORY);
        config.textNoResults = bundle.getString(ARGUMENT_TEXT_NO_RESULTS);
        config.historyId = bundle.getString(ARGUMENT_HISTORY_ID);
        return config;
    }

    /**
     * Sets the current activity that also receives callbacks
     *
     * @param activity The Activity that receives callbacks. Must implement SearchPreferenceResultListener.
     */
    public void setActivity(@NonNull AppCompatActivity activity) {
        this.activity = activity;
        if (!(activity instanceof SearchPreferenceResultListener)) {
            throw new IllegalArgumentException("Activity must implement SearchPreferenceResultListener");
        }
    }

    /**
     * Show a history of recent search terms if nothing was typed yet. Default is true
     *
     * @param historyEnabled True if history should be enabled
     */
    public void setHistoryEnabled(boolean historyEnabled) {
        this.historyEnabled = historyEnabled;
    }

    /**
     * Sets the id to use for saving the history. Preference screens with the same history id will share the same
     * history. The default id is null (no id).
     *
     * @param historyId the history id
     */
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    /**
     * Allow to enable and disable fuzzy searching. Default is true
     *
     * @param fuzzySearchEnabled True if search should be fuzzy
     */
    public void setFuzzySearchEnabled(boolean fuzzySearchEnabled) {
        this.fuzzySearchEnabled = fuzzySearchEnabled;
    }

    /**
     * Show breadcrumbs in the list of search results, containing of
     * the prefix given in addResourceFileToIndex, PreferenceCategory and PreferenceScreen.
     * Default is false
     *
     * @param breadcrumbsEnabled True if breadcrumbs should be shown
     */
    public void setBreadcrumbsEnabled(boolean breadcrumbsEnabled) {
        this.breadcrumbsEnabled = breadcrumbsEnabled;
    }

    /**
     * Show the search bar above the list. When setting this to false, you have to use {@see SearchPreferenceFragment#setSearchTerm(String) setSearchTerm} instead
     * Default is true
     *
     * @param searchBarEnabled True if search bar should be shown
     */
    public void setSearchBarEnabled(boolean searchBarEnabled) {
        this.searchBarEnabled = searchBarEnabled;
    }

    /**
     * Sets the container to use when loading the fragment
     *
     * @param containerResId Resource id of the container
     */
    public void setFragmentContainerViewId(@IdRes int containerResId) {
        this.containerResId = containerResId;
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

    /**
     * Adds a new file to the index
     *
     * @param resId The preference file to index
     */
    public SearchIndexItem index(@XmlRes int resId) {
        SearchIndexItem item = new SearchIndexItem(resId, this);
        filesToIndex.add(item);
        return item;
    }

    public PreferenceItem indexItem(final Preference preference, @XmlRes final int resId) {
        final PreferenceItem preferenceItem =
                new PreferenceItem(
                        preference.getTitle() != null ? preference.getTitle().toString() : null,
                        preference.getSummary() != null ? preference.getSummary().toString() : null,
                        preference.getKey(),
                        null,
                        null,
                        resId);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = ((ListPreference) preference);
            if (listPreference.getEntries() != null) {
                preferenceItem.entries = Arrays.toString(listPreference.getEntries());
            }
        }
        preferencesToIndex.add(preferenceItem);
        return preferenceItem;
    }

    public List<PreferenceItem> indexItems(final List<Preference> preferences, @XmlRes final int resId) {
        return preferences
                .stream()
                .map(preference -> indexItem(preference, resId))
                .collect(Collectors.toList());
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

    List<SearchIndexItem> getFiles() {
        return filesToIndex;
    }

    List<PreferenceItem> getPreferencesToIndex() {
        return preferencesToIndex;
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
