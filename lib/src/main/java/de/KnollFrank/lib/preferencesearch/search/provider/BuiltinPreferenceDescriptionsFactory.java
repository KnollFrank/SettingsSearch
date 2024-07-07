package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.SwitchPreference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Strings;

public class BuiltinPreferenceDescriptionsFactory {

    // FK-TODO: hier noch andere Preferences behandeln: SwitchPreference, ...
    public static List<PreferenceDescription> createBuiltinPreferenceDescriptions() {
        return Arrays.asList(
                getListPreferenceDescription(),
                getSwitchPreferenceDescription(),
                getDropDownPreferenceDescription(),
                getMultiSelectListPreferenceDescription());
    }

    private static PreferenceDescription<ListPreference> getListPreferenceDescription() {
        return new PreferenceDescription<>(
                ListPreference.class,
                preference ->
                        Strings.join(
                                ", ",
                                Optional.ofNullable(preference.getEntries())));
    }

    private static PreferenceDescription<SwitchPreference> getSwitchPreferenceDescription() {
        return new PreferenceDescription<>(
                SwitchPreference.class,
                preference ->
                        Strings.joinNonNullElements(
                                ", ",
                                Arrays.asList(
                                        preference.getSummaryOff(),
                                        preference.getSummaryOn())));
    }

    private static PreferenceDescription<DropDownPreference> getDropDownPreferenceDescription() {
        return new PreferenceDescription<>(
                DropDownPreference.class,
                preference ->
                        Strings.join(
                                ", ",
                                Optional.ofNullable(preference.getEntries())));
    }

    private static PreferenceDescription<MultiSelectListPreference> getMultiSelectListPreferenceDescription() {
        return new PreferenceDescription<>(
                MultiSelectListPreference.class,
                preference ->
                        Strings.join(
                                ", ",
                                Optional.ofNullable(preference.getEntries())));
    }
}
