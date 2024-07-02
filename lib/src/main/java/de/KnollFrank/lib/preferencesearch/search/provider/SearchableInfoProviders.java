package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SearchableInfoProviders {

    public static Map<Class<? extends Preference>, SearchableInfoProvider<?>> combineSearchableInfoProviders(
            final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviders1,
            final Map<Class<? extends Preference>, SearchableInfoProvider<?>> searchableInfoProviders2) {
        return ImmutableMap.
                <Class<? extends Preference>, SearchableInfoProvider<?>>builder()
                .putAll(searchableInfoProviders1)
                .putAll(searchableInfoProviders2)
                .build();
    }
}
