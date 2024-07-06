package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

public class PreferenceDescription<T extends Preference> {

    public final Class<T> preferenceClass;
    public final SearchableInfoProvider<T> searchableInfoProvider;

    public PreferenceDescription(final Class<T> preferenceClass,
                                 final SearchableInfoProvider<T> searchableInfoProvider) {
        this.preferenceClass = preferenceClass;
        this.searchableInfoProvider = searchableInfoProvider;
    }
}
