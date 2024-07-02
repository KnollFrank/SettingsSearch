package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class BuiltinSearchableInfoProvidersFactory {

    public static Map<Class<? extends Preference>, SearchableInfoProvider<?>> createBuiltinSearchableInfoProviders() {
        return ImmutableMap.
                <Class<? extends Preference>, SearchableInfoProvider<?>>builder()
                .put(ListPreference.class, new ListPreferenceSearchableInfoProvider())
                .put(DropDownPreference.class, new DropDownPreferenceSearchableInfoProvider())
                .put(MultiSelectListPreference.class, new MultiSelectListPreferenceSearchableInfoProvider())
                .build();
    }
}
