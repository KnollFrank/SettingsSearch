package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;

public class MergedPreferenceScreens {

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.preferences(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        preferenceManager,
                        pojoEntityMap ->
                                convertPojoKeys2EntityKeys(
                                        mergedPreferenceScreenData.preferencePathByPreference(),
                                        pojoEntityMap)),
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext()),
                mergedPreferenceScreenData.hostByPreference());
    }

    private static Map<Preference, PreferencePath> convertPojoKeys2EntityKeys(
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .filter(entry -> pojoEntityMap.containsKey(entry.getKey()))
                .collect(
                        Collectors.toMap(
                                entry -> pojoEntityMap.get(entry.getKey()),
                                Entry::getValue));
    }
}
