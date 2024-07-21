package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenceDescriptions {

    public static SearchableInfoProviders getSearchableInfoProviders(final List<PreferenceDescription> preferenceDescriptions) {
        return new SearchableInfoProviders(getSearchableInfoProvidersByPreferenceClass(preferenceDescriptions));
    }

    public static Map<Class<? extends Preference>, SearchableInfoProvider> getSearchableInfoProvidersByPreferenceClass(final List<PreferenceDescription> preferenceDescriptions) {
        return preferenceDescriptions
                .stream()
                .collect(
                        Collectors.toMap(
                                preferenceDescription -> preferenceDescription.preferenceClass,
                                preferenceDescription -> preferenceDescription.searchableInfoProvider));
    }
}
