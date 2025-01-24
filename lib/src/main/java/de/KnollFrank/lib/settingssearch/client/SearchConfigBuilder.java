package de.KnollFrank.lib.settingssearch.client;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.DefaultMarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.MarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsByPreferencePathSorter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.recyclerview.DefaultPreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.ui.DefaultSearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.DefaultSearchResultsFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchConfigBuilder {

    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = preference -> true;
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> true;
    private PrepareShow prepareShow = preferenceFragment -> {
    };
    private PreferencePathDisplayer preferencePathDisplayer = new DefaultPreferencePathDisplayer();
    private SearchResultsFilter searchResultsFilter = preference -> true;
    private SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();
    private SearchPreferenceFragmentUI searchPreferenceFragmentUI = new DefaultSearchPreferenceFragmentUI();
    private SearchResultsFragmentUI searchResultsFragmentUI = new DefaultSearchResultsFragmentUI();
    private MarkupsFactory markupsFactory;

    public SearchConfigBuilder(final Context context) {
        this.markupsFactory = new DefaultMarkupsFactory(context);
    }

    public SearchConfigBuilder withIncludePreferenceInSearchResultsPredicate(final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        return this;
    }

    public SearchConfigBuilder withShowPreferencePathPredicate(final ShowPreferencePathPredicate showPreferencePathPredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        return this;
    }

    public SearchConfigBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
        return this;
    }

    public SearchConfigBuilder withPreferencePathDisplayer(final PreferencePathDisplayer preferencePathDisplayer) {
        this.preferencePathDisplayer = preferencePathDisplayer;
        return this;
    }

    public SearchConfigBuilder withSearchResultsFilter(final SearchResultsFilter searchResultsFilter) {
        this.searchResultsFilter = searchResultsFilter;
        return this;
    }

    public SearchConfigBuilder withSearchResultsSorter(final SearchResultsSorter searchResultsSorter) {
        this.searchResultsSorter = searchResultsSorter;
        return this;
    }

    public SearchConfigBuilder withSearchPreferenceFragmentUI(final SearchPreferenceFragmentUI searchPreferenceFragmentUI) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        return this;
    }

    public SearchConfigBuilder withSearchResultsFragmentUI(final SearchResultsFragmentUI searchResultsFragmentUI) {
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        return this;
    }

    public SearchConfigBuilder withMarkupsFactory(final MarkupsFactory markupsFactory) {
        this.markupsFactory = markupsFactory;
        return this;
    }

    public SearchConfig build() {
        return new SearchConfig(
                includePreferenceInSearchResultsPredicate,
                showPreferencePathPredicate,
                prepareShow,
                preferencePathDisplayer,
                searchResultsFilter,
                searchResultsSorter,
                searchPreferenceFragmentUI,
                searchResultsFragmentUI,
                markupsFactory);
    }
}
