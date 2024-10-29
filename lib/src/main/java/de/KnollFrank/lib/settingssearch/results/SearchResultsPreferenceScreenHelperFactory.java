package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreenHelperFactory {

    public static SearchResultsPreferenceScreenHelper createSearchResultsPreferenceScreenHelper(
            final PreferenceManager preferenceManager,
            final PreferencePathNavigator preferencePathNavigator,
            final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory) {
        return new SearchResultsPreferenceScreenHelper(
                preferencePathNavigator,
                preferencePathByPreferenceFactory,
                preferenceManager.getContext(),
                createInitialInfo(preferenceManager, preferencePathByPreferenceFactory));
    }

    private static SearchResultsDescription createInitialInfo(final PreferenceManager preferenceManager,
                                                              final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory) {
        final PreferenceScreenWithMap preferenceScreenWithMap = createEmptyPreferenceScreenWithMap(preferenceManager);
        return new SearchResultsDescription(
                preferenceScreenWithMap,
                preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap()),
                new SearchableInfoAttribute());
    }

    private static PreferenceScreenWithMap createEmptyPreferenceScreenWithMap(final PreferenceManager preferenceManager) {
        return new PreferenceScreenWithMap(
                preferenceManager.createPreferenceScreen(preferenceManager.getContext()),
                HashBiMap.create());
    }
}
