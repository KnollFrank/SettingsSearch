package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class HostByPreferenceProvider {

    public static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final Collection<PreferenceScreenWithHostClassPOJO> preferenceScreens) {
        return Maps.merge(
                preferenceScreens
                        .stream()
                        .map(preferenceScreen -> getHostByPreference(preferenceScreen))
                        .collect(Collectors.toList()));
    }

    private static Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final PreferenceScreenWithHostClassPOJO preferenceScreen) {
        return SearchablePreferences
                .getPreferencesRecursively(preferenceScreen.preferenceScreen().children())
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> preferenceScreen.host()));
    }
}
