package de.KnollFrank.lib.settingssearch.client;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.DefaultMarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.DefaultShowSettingsFragmentAndHighlightSetting;
import de.KnollFrank.lib.settingssearch.results.MarkupsFactory;
import de.KnollFrank.lib.settingssearch.results.SearchResultsByPreferencePathSorter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsFilter;
import de.KnollFrank.lib.settingssearch.results.SearchResultsSorter;
import de.KnollFrank.lib.settingssearch.results.ShowSettingsFragmentAndHighlightSetting;
import de.KnollFrank.lib.settingssearch.results.recyclerview.DefaultPreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathDisplayer;
import de.KnollFrank.lib.settingssearch.search.ui.DefaultSearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.DefaultSearchResultsFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchPreferenceFragmentUI;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchConfigBuilder {

    private final @IdRes int fragmentContainerViewId;
    private Optional<String> queryHint = Optional.empty();
    private IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate = preference -> true;
    private ShowPreferencePathPredicate showPreferencePathPredicate = preferencePath -> true;
    private PrepareShow prepareShow = fragment -> {
    };
    private PreferencePathDisplayer preferencePathDisplayer = new DefaultPreferencePathDisplayer();
    private SearchResultsFilter searchResultsFilter = preference -> true;
    private SearchResultsSorter searchResultsSorter = new SearchResultsByPreferencePathSorter();
    private SearchPreferenceFragmentUI searchPreferenceFragmentUI = new DefaultSearchPreferenceFragmentUI();
    private SearchResultsFragmentUI searchResultsFragmentUI = new DefaultSearchResultsFragmentUI();
    private MarkupsFactory markupsFactory;
    private ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting;

    SearchConfigBuilder(final @IdRes int fragmentContainerViewId, final FragmentActivity activity) {
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.markupsFactory = new DefaultMarkupsFactory(activity);
        this.showSettingsFragmentAndHighlightSetting =
                new DefaultShowSettingsFragmentAndHighlightSetting(
                        fragmentContainerViewId,
                        activity.getSupportFragmentManager());
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withQueryHint(final String queryHint) {
        this.queryHint = Optional.of(queryHint);
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withIncludePreferenceInSearchResultsPredicate(final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        this.includePreferenceInSearchResultsPredicate = includePreferenceInSearchResultsPredicate;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withShowPreferencePathPredicate(final ShowPreferencePathPredicate showPreferencePathPredicate) {
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withPreferencePathDisplayer(final PreferencePathDisplayer preferencePathDisplayer) {
        this.preferencePathDisplayer = preferencePathDisplayer;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withSearchResultsFilter(final SearchResultsFilter searchResultsFilter) {
        this.searchResultsFilter = searchResultsFilter;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withSearchResultsSorter(final SearchResultsSorter searchResultsSorter) {
        this.searchResultsSorter = searchResultsSorter;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withSearchPreferenceFragmentUI(final SearchPreferenceFragmentUI searchPreferenceFragmentUI) {
        this.searchPreferenceFragmentUI = searchPreferenceFragmentUI;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withSearchResultsFragmentUI(final SearchResultsFragmentUI searchResultsFragmentUI) {
        this.searchResultsFragmentUI = searchResultsFragmentUI;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withMarkupsFactory(final MarkupsFactory markupsFactory) {
        this.markupsFactory = markupsFactory;
        return this;
    }

    @SuppressWarnings("unused")
    public SearchConfigBuilder withShowSettingsFragmentAndHighlightSetting(final ShowSettingsFragmentAndHighlightSetting showSettingsFragmentAndHighlightSetting) {
        this.showSettingsFragmentAndHighlightSetting = showSettingsFragmentAndHighlightSetting;
        return this;
    }

    public SearchConfig build() {
        return new SearchConfig(
                fragmentContainerViewId,
                queryHint,
                includePreferenceInSearchResultsPredicate,
                showPreferencePathPredicate,
                prepareShow,
                preferencePathDisplayer,
                searchResultsFilter,
                searchResultsSorter,
                searchPreferenceFragmentUI,
                searchResultsFragmentUI,
                markupsFactory,
                showSettingsFragmentAndHighlightSetting);
    }
}
