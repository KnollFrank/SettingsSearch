package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Maps;

public class SearchableInfoProviders {

    public final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProvidersByPreferenceClass;

    public SearchableInfoProviders(final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProvidersByPreferenceClass) {
        this.searchableInfoProvidersByPreferenceClass = searchableInfoProvidersByPreferenceClass;
    }

    public SearchableInfoProviders combineWith(final SearchableInfoProviders other) {
        return new SearchableInfoProviders(
                Maps.merge(
                        this.searchableInfoProvidersByPreferenceClass,
                        other.searchableInfoProvidersByPreferenceClass));
    }
}
