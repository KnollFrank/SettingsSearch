package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreen {

    private final PreferenceScreen preferenceScreen;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public SearchResultsPreferenceScreen(final PreferenceScreen preferenceScreen,
                                         final SearchableInfoAttribute searchableInfoAttribute,
                                         final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        this.preferenceScreen = preferenceScreen;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.pojoEntityMap = pojoEntityMap;
        this.preferenceScreenResetter =
                new PreferenceScreenResetter(
                        preferenceScreen,
                        searchableInfoAttribute);
    }

    public void preparePreferenceScreenForSearch() {
        PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch(preferenceScreen);
    }

    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(preferenceScreen);
    }

    public void displayPreferenceMatchesOnPreferenceScreen(final List<PreferenceMatch> preferenceMatches) {
        preferenceScreen.removeAll();
        SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    public void prepareSearch(final String needle) {
        preferenceScreenResetter.reset();
        MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoAttribute,
                needle);
    }

    public SearchResultsDisplayer createSearchResultsDisplayer(final Context context) {
        return new SearchResultsDisplayer(
                pojoEntityMap,
                searchableInfoAttribute,
                preferenceScreen,
                context);
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }
}
