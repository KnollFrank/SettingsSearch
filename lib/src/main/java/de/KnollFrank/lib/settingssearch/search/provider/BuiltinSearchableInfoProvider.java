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
        if (preference instanceof final MultiSelectListPreference multiSelectListPreference) {
            return Optional.of(getMultiSelectListPreferenceSearchableInfo(multiSelectListPreference));
        }
        if (preference instanceof final DropDownPreference dropDownPreference) {
            return Optional.of(getDropDownPreferenceSearchableInfo(dropDownPreference));
        }
        if (preference instanceof final ListPreference listPreference) {
            return Optional.of(getListPreferenceSearchableInfo(listPreference));
        }
        if (preference instanceof final SwitchPreference switchPreference) {
            return Optional.of(getSwitchPreferenceSearchableInfo(switchPreference));
        }
        if (preference instanceof final EditTextPreference editTextPreference) {
            return Optional.of(getEditTextPreferenceSearchableInfo(editTextPreference));
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
