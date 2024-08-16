package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;

public class PreferenceDescription<T extends Preference> {

    public final Class<T> preferenceClass;
    public final SearchableInfoProvider searchableInfoProvider;

    public PreferenceDescription(final Class<T> preferenceClass,
                                 final SearchableInfoProvider searchableInfoProvider) {
        this.preferenceClass = preferenceClass;
        this.searchableInfoProvider = searchableInfoProvider;
    }
}
