package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class HostByPreferenceProvider {

    public static Map<Preference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final Collection<PreferenceScreenWithHostClassPOJO> preferenceScreens,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        return Maps.merge(
                preferenceScreens
                        .stream()
                        .map(preferenceScreen -> getHostByPreference(preferenceScreen, pojoEntityMap))
                        .collect(Collectors.toList()));
    }

    private static Map<Preference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final PreferenceScreenWithHostClassPOJO preferenceScreen,
            final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap) {
        final List<SearchablePreferencePOJO> preferencesRecursively = SearchablePreferences
                .getPreferencesRecursively(preferenceScreen.preferenceScreen().children());
        return preferencesRecursively
                .stream()
                .map(pojoEntityMap::get)
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> preferenceScreen.host()));
    }
}
