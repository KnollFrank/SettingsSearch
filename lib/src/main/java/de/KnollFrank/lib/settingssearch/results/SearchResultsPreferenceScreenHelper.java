package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreenHelper {

    private final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
    private final PreferenceScreenWithMap preferenceScreenWithMap;
    private final Map<Preference, PreferencePath> preferencePathByPreference;

    public SearchResultsPreferenceScreenHelper(final PreferenceScreenWithMap preferenceScreenWithMap,
                                               final Map<Preference, PreferencePath> preferencePathByPreference) {
        this.preferenceScreenWithMap = preferenceScreenWithMap;
        this.preferencePathByPreference = preferencePathByPreference;
    }

    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(preferenceScreenWithMap.preferenceScreen());
    }

    public void displayPreferenceMatchesOnPreferenceScreen(final List<PreferenceMatch> preferenceMatches) {
        preferenceScreenWithMap.preferenceScreen().removeAll();
        SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                getPreferences(preferenceMatches),
                preferenceScreenWithMap.preferenceScreen());
    }

    public void preparePreferenceScreenForSearch() {
        PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch(preferenceScreenWithMap.preferenceScreen());
    }

    public void prepareSearch(final String needle) {
        new PreferenceScreenResetter(preferenceScreenWithMap.preferenceScreen(), searchableInfoAttribute).resetPreferenceScreen();
        MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreenWithMap.preferenceScreen(),
                searchableInfoAttribute,
                needle);
    }

    public SearchResultsDisplayer createSearchResultsDisplayer(final Context context) {
        return new SearchResultsDisplayer(
                preferenceScreenWithMap.pojoEntityMap(),
                searchableInfoAttribute,
                preferenceScreenWithMap.preferenceScreen(),
                context);
    }

    public Map<Preference, PreferencePath> getPreferencePathByPreference() {
        return preferencePathByPreference;
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }

    public SearchableInfoAttribute getSearchableInfoAttribute() {
        return searchableInfoAttribute;
    }
}
