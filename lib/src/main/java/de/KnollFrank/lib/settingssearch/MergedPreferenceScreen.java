package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

public class MergedPreferenceScreen {

    public final SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper;
    public final Set<SearchablePreferencePOJO> allPreferencesForSearch;

    public MergedPreferenceScreen(final PreferenceScreenWithMap preferenceScreenWithMap,
                                  final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                  final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, PreferencePathNavigator> preferencePathNavigatorFactory,
                                  final Set<SearchablePreferencePOJO> allPreferencesForSearch) {
        this.allPreferencesForSearch = allPreferencesForSearch;
        this.searchResultsPreferenceScreenHelper =
                new SearchResultsPreferenceScreenHelper(
                        preferenceScreenWithMap,
                        preferencePathNavigatorFactory,
                        preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap()));
    }
}
