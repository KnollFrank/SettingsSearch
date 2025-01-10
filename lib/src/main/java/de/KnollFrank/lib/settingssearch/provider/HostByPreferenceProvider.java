package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class HostByPreferenceProvider {

    public static Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final Collection<PreferenceScreenWithHostClass> preferenceScreens) {
        return Maps.merge(
                preferenceScreens
                        .stream()
                        .map(HostByPreferenceProvider::getHostByPreference)
                        .collect(Collectors.toList()));
    }

    private static Map<SearchablePreference, Class<? extends PreferenceFragmentCompat>> getHostByPreference(
            final PreferenceScreenWithHostClass preferenceScreen) {
        return SearchablePreferences
                .getPreferencesRecursively(preferenceScreen.preferenceScreen().children())
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> preferenceScreen.host()));
    }
}
