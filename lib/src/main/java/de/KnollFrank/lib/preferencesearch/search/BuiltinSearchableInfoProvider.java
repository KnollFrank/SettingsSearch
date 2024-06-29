package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;

import java.util.Optional;

public class BuiltinSearchableInfoProvider implements SearchableInfoProvider {

    @Override
    public Optional<String> getSearchableInfo(final Preference preference) {
        if (ListPreference.class.equals(preference.getClass())) {
            return Optional.of(enumerateNullableEntries(((ListPreference) preference).getEntries()));
        }
        if (DropDownPreference.class.equals(preference.getClass())) {
            return Optional.of(enumerateNullableEntries(((DropDownPreference) preference).getEntries()));
        }
        if (MultiSelectListPreference.class.equals(preference.getClass())) {
            return Optional.of(enumerateNullableEntries(((MultiSelectListPreference) preference).getEntries()));
        }
        return Optional.empty();
    }

    private static String enumerateNullableEntries(final CharSequence[] entries) {
        return entries == null ? "" : enumerate(entries);
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }
}
