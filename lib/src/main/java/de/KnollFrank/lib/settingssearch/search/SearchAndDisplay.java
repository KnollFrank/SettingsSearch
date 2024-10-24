package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final SearchResultsDisplayer searchResultsDisplayer;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                            final SearchableInfoAttribute searchableInfoAttribute,
                            final PreferenceScreen preferenceScreen,
                            final Context context) {
        this.preferenceSearcher = preferenceSearcher;
        this.searchResultsDisplayer =
                new SearchResultsDisplayer(
                        pojoEntityMap,
                        searchableInfoAttribute,
                        preferenceScreen,
                        context);
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        searchResultsDisplayer.displaySearchResults(preferenceMatches);
    }
}
