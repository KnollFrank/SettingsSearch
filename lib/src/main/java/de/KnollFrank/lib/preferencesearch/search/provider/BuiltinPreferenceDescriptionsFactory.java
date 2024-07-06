package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.SwitchPreference;

import java.util.Arrays;
import java.util.List;

public class BuiltinPreferenceDescriptionsFactory {

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    public static List<PreferenceDescription> createBuiltinPreferenceDescriptions() {
        return Arrays.asList(
                new PreferenceDescription<>(
                        ListPreference.class,
                        new ListPreferenceSearchableInfoProvider()),
                new PreferenceDescription<>(
                        SwitchPreference.class,
                        new SwitchPreferenceSearchableInfoProvider()),
                new PreferenceDescription<>(
                        DropDownPreference.class,
                        new DropDownPreferenceSearchableInfoProvider()),
                new PreferenceDescription<>(
                        MultiSelectListPreference.class,
                        new MultiSelectListPreferenceSearchableInfoProvider()));
    }
}
