package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenceDescriptions {

    public static Map<Class<? extends Preference>, SearchableInfoProvider> getSearchableInfoProviders(
            final List<PreferenceDescription<? extends Preference>> preferenceDescriptions) {
        return getSearchableInfoProviderByPreferenceClass(preferenceDescriptions);
    }

    public static Map<Class<? extends Preference>, SearchableInfoProvider> getSearchableInfoProviderByPreferenceClass(
            final List<PreferenceDescription<? extends Preference>> preferenceDescriptions) {
        return preferenceDescriptions
                .stream()
                .collect(
                        Collectors.toMap(
                                PreferenceDescription::preferenceClass,
                                PreferenceDescription::searchableInfoProvider));
    }
}
