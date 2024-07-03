package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class BuiltinSearchableInfoProvidersFactory {

    // FK-TODO: CheckBoxPreference (und TwoStatePreference) und alle anderen von Preference abgeleiteten Preferences behandeln
    public static Map<Class<? extends Preference>, SearchableInfoProvider<?>> createBuiltinSearchableInfoProviders() {
        return ImmutableMap.
                <Class<? extends Preference>, SearchableInfoProvider<?>>builder()
                .put(
                        ListPreference.class,
                        new ListPreferenceSearchableInfoProvider())
                .put(
                        SwitchPreference.class,
                        new SwitchPreferenceSearchableInfoProvider())
                .put(
                        DropDownPreference.class,
                        new DropDownPreferenceSearchableInfoProvider())
                .put(
                        MultiSelectListPreference.class,
                        new MultiSelectListPreferenceSearchableInfoProvider())
                .build();
    }
}
