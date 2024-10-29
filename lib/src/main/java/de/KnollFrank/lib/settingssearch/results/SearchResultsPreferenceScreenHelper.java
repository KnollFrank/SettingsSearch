package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreenHelper {

    private final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
    private final PreferenceScreenWithMap preferenceScreenWithMap;
    private final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferencePathNavigator preferencePathNavigator;
    private final Context context;

    public SearchResultsPreferenceScreenHelper(final Supplier<PreferenceScreenWithMap> preferenceScreenWithMapFactory,
                                               final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, PreferencePathNavigator> preferencePathNavigatorFactory,
                                               final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                               final Context context) {
        this.preferenceScreenWithMap = preferenceScreenWithMapFactory.get();
        this.preferencePathByPreference = preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap());
        this.preferencePathNavigator = preferencePathNavigatorFactory.apply(preferenceScreenWithMap.pojoEntityMap());
        this.context = context;
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

    public SearchResultsDisplayer createSearchResultsDisplayer() {
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

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(preferencePathByPreference.get(preference));
    }
}
