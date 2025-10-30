package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;

import androidx.annotation.IdRes;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.MarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.ShowSettingsFragmentAndHighlightSetting;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.StringMatcher;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

// FK-TODO: Suchergebnisse gruppierbar machen
public class SearchConfig {

    @IdRes
    public final int fragmentContainerViewId;
    public final Optional<String> queryHint;
    public final StringMatcher stringMatcher;
    public final ShowPreferencePathPredicate showPreferencePathPredicate;
    public final PrepareShow prepareShow;
    public final PreferencePathDisplayer preferencePathDisplayer;
    public final SearchResultsFilter searchResultsFilter;
    public final SearchResultsSorter searchResultsSorter;
    public final SearchPreferenceFragmentUI searchPreferenceFragmentUI;
    public final SearchResultsFragmentUI searchResultsFragmentUI;
    public final MarkupsFactory markupsFactory;
    public final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;

    SearchConfig(final @IdRes int fragmentContainerViewId,
                 final Optional<String> queryHint,
                 final StringMatcher stringMatcher,
                 final ShowPreferencePathPredicate showPreferencePathPredicate,
                 final PrepareShow prepareShow,
                 final PreferencePathDisplayer preferencePathDisplayer,
                 final SearchResultsFilter searchResultsFilter,
                 final SearchResultsSorter searchResultsSorter,
                 final SearchPreferenceFragmentUI searchPreferenceFragmentUI,
                 final SearchResultsFragmentUI searchResultsFragmentUI,
                 final MarkupsFactory markupsFactory,
                 final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting) {
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.queryHint = queryHint;
        this.stringMatcher = stringMatcher;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
        this.preferencePathDisplayer = preferencePathDisplayer;
        this.searchResultsFilter = searchResultsFilter;
        this.searchResultsSorter = searchResultsSorter;
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        this.markupsFactory = markupsFactory;
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
    }

    public static SearchConfigBuilder builder(final @IdRes int fragmentContainerViewId, final Context context) {
        return new SearchConfigBuilder(fragmentContainerViewId, context);
    }
}
