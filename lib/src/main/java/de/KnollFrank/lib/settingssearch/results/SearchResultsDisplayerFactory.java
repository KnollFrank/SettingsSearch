package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.MarkupFactory;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsDisplayerFactory {

    public static SearchResultsDisplayer createSearchResultsDisplayer(
            final PreferenceManager preferenceManager,
            final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory) {
        return new SearchResultsDisplayer(
                preferencePathByPreferenceFactory,
                () -> MarkupFactory.createMarkups(preferenceManager.getContext()),
                createInitialSearchResultsDescription(preferenceManager, preferencePathByPreferenceFactory));
    }

    private static SearchResultsDescription createInitialSearchResultsDescription(final PreferenceManager preferenceManager,
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
