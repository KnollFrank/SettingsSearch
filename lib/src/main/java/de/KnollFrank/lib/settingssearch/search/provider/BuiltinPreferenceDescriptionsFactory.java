package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;

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
                preference -> {
                    final ListPreference listPreference = (ListPreference) preference;
                    return String.join(
                            ", ",
                            concat(
                                    Optional.ofNullable(listPreference.getEntries()),
                                    Optional.ofNullable(listPreference.getDialogTitle())));
                });
    }

    private static PreferenceDescription<SwitchPreference> getSwitchPreferenceDescription() {
        return new PreferenceDescription<>(
                SwitchPreference.class,
                preference -> {
                    final SwitchPreference switchPreference = (SwitchPreference) preference;
                    return String.join(
                            ", ",
                            Lists.getNonEmptyElements(
                                    ImmutableList.of(
                                            Optional.ofNullable(switchPreference.getSummaryOff()),
                                            Optional.ofNullable(switchPreference.getSummaryOn()))));
                });
    }

    private static PreferenceDescription<DropDownPreference> getDropDownPreferenceDescription() {
        return new PreferenceDescription<>(
                DropDownPreference.class,
                preference ->
                        String.join(
                                ", ",
                                Lists.asList(Optional.ofNullable(((DropDownPreference) preference).getEntries()))));
    }

    private static PreferenceDescription<MultiSelectListPreference> getMultiSelectListPreferenceDescription() {
        return new PreferenceDescription<>(
                MultiSelectListPreference.class,
                preference -> {
                    final MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
                    return String.join(
                            ", ",
                            concat(
                                    Optional.ofNullable(multiSelectListPreference.getEntries()),
                                    Optional.ofNullable(multiSelectListPreference.getDialogTitle())));
                });
    }

    private static List<CharSequence> concat(final Optional<CharSequence[]> elements,
                                             final Optional<? extends CharSequence>... evenMoreElements) {
        return ImmutableList
                .<CharSequence>builder()
                .addAll(Lists.asList(elements))
                .addAll(Lists.getNonEmptyElements(Arrays.asList(evenMoreElements)))
                .build();
    }
}
