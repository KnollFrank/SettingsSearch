package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Lists;

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
                        String.join(
                                ", ",
                                concat(
                                        Optional.ofNullable(preference.getEntries()),
                                        Optional.ofNullable(preference.getDialogTitle()))));
    }

    private static PreferenceDescription<SwitchPreference> getSwitchPreferenceDescription() {
        return new PreferenceDescription<>(
                SwitchPreference.class,
                preference ->
                        String.join(
                                ", ",
                                Lists.getNonEmptyElements(
                                        ImmutableList.of(
                                                Optional.ofNullable(preference.getSummaryOff()),
                                                Optional.ofNullable(preference.getSummaryOn())))));
    }

    private static PreferenceDescription<DropDownPreference> getDropDownPreferenceDescription() {
        return new PreferenceDescription<>(
                DropDownPreference.class,
                preference ->
                        String.join(
                                ", ",
                                Lists.asList(Optional.ofNullable(preference.getEntries()))));
    }

    private static PreferenceDescription<MultiSelectListPreference> getMultiSelectListPreferenceDescription() {
        return new PreferenceDescription<>(
                MultiSelectListPreference.class,
                preference ->
                        String.join(
                                ", ",
                                concat(
                                        Optional.ofNullable(preference.getEntries()),
                                        Optional.ofNullable(preference.getDialogTitle()))));
    }

    private static List<CharSequence> concat(final Optional<CharSequence[]> elements,
                                             final Optional<? extends CharSequence> element) {
        final Builder<CharSequence> builder = ImmutableList.builder();
        builder.addAll(Lists.asList(elements));
        element.ifPresent(builder::add);
        return builder.build();
    }
}
