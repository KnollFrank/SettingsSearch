package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

public class SearchableInfoProviders {

    public final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProvidersByPreferenceClass;

    public SearchableInfoProviders(final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProvidersByPreferenceClass) {
        this.searchableInfoProvidersByPreferenceClass = searchableInfoProvidersByPreferenceClass;
    }
}
