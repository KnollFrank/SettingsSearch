package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableMap;

public class BuiltinSearchableInfoProvidersFactory {

    // FK-TODO: CheckBoxPreference (und TwoStatePreference) und alle anderen von Preference abgeleiteten Preferences behandeln
    public static SearchableInfoProviders createBuiltinSearchableInfoProviders() {
        return new SearchableInfoProviders(
                ImmutableMap.
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
                        .build());
    }
}
