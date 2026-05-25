package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;

public class BuiltinSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        if (preference instanceof MultiSelectListPreference) {
            return Optional.of(getMultiSelectListPreferenceSearchableInfo((MultiSelectListPreference) preference));
        }
        if (preference instanceof DropDownPreference) {
            return Optional.of(getDropDownPreferenceSearchableInfo((DropDownPreference) preference));
        }
        if (preference instanceof ListPreference) {
            return Optional.of(getListPreferenceSearchableInfo((ListPreference) preference));
        }
        if (preference instanceof SwitchPreference) {
            return Optional.of(getSwitchPreferenceSearchableInfo((SwitchPreference) preference));
        }
        if (preference instanceof EditTextPreference) {
            return Optional.of(getEditTextPreferenceSearchableInfo((EditTextPreference) preference));
        }
        return Optional.empty();
    }

    private static String getListPreferenceSearchableInfo(final ListPreference listPreference) {
        return String.join(
                ", ",
                concat(
                        Optional.ofNullable(listPreference.getDialogTitle()),
                        Optional.ofNullable(listPreference.getEntries())));
    }

    private static String getSwitchPreferenceSearchableInfo(final SwitchPreference switchPreference) {
        return Optionals
                .streamOfPresentElements(
                        Optional.ofNullable(switchPreference.getSummaryOff()),
                        Optional.ofNullable(switchPreference.getSummaryOn()))
                .collect(Collectors.joining(", "));
    }

    private static String getDropDownPreferenceSearchableInfo(final DropDownPreference dropDownPreference) {
        return String.join(
                ", ",
                Optionals.asList(Optional.ofNullable(dropDownPreference.getEntries())));
    }

    private static String getMultiSelectListPreferenceSearchableInfo(final MultiSelectListPreference multiSelectListPreference) {
        return String.join(
                ", ",
                concat(
                        Optional.ofNullable(multiSelectListPreference.getDialogTitle()),
                        Optional.ofNullable(multiSelectListPreference.getEntries())));
    }

    private static String getEditTextPreferenceSearchableInfo(final EditTextPreference editTextPreference) {
        return Optionals
                .streamOfPresentElements(Optional.ofNullable(editTextPreference.getDialogTitle()))
                .collect(Collectors.joining(", "));
    }

    private static List<CharSequence> concat(final Optional<CharSequence> dialogTitle,
                                             final Optional<CharSequence[]> entries) {
        return ImmutableList
                .<CharSequence>builder()
                .addAll(
                        Optionals
                                .streamOfPresentElements(dialogTitle)
                                .collect(Collectors.toList()))
                .addAll(Optionals.asList(entries))
                .build();
    }
}
