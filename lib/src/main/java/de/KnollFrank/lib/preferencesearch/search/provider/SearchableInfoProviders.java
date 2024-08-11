package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

public class SearchableInfoProviders {

    public final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass;

    public SearchableInfoProviders(final Map<Class<? extends Preference>, SearchableInfoProvider> searchableInfoProviderByPreferenceClass) {
        this.searchableInfoProviderByPreferenceClass = searchableInfoProviderByPreferenceClass;
    }
}
