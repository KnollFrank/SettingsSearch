package de.KnollFrank.lib.settingssearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
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
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayerFactory;
import de.KnollFrank.lib.settingssearch.results.ShowPreferenceScreenAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.results.recyclerview.SearchResultsFragment;

public class MergedPreferenceScreens {

    public static MergedPreferenceScreen createMergedPreferenceScreen(
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final FragmentManager fragmentManager,
            final MergedPreferenceScreenData mergedPreferenceScreenData,
            final PreferenceManager preferenceManager,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        final PreferencePathNavigator preferencePathNavigator =
                new PreferencePathNavigator(
                        mergedPreferenceScreenData.hostByPreference(),
                        fragmentFactoryAndInitializer,
                        preferenceManager.getContext());
        return new MergedPreferenceScreen(
                mergedPreferenceScreenData.preferencePathByPreference(),
                mergedPreferenceScreenData.preferences(),
                SearchResultsDisplayerFactory.createSearchResultsDisplayer(
                        new SearchResultsFragment(
                                mergedPreferenceScreenData.preferencePathByPreference(),
                                new ShowPreferenceScreenAndHighlightPreference(
                                        preferencePathNavigator,
                                        mergedPreferenceScreenData.preferencePathByPreference(),
                                        fragmentContainerViewId,
                                        prepareShow,
                                        fragmentManager)),
                        preferenceManager,
                        pojoEntityMap ->
                                convertPojoKeys2EntityKeys(
                                        mergedPreferenceScreenData.preferencePathByPreference(),
                                        pojoEntityMap)),
                preferencePathNavigator,
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
