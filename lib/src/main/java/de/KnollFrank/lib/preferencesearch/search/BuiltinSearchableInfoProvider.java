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
            return Optional.of(join(((ListPreference) preference).getEntries()));
        }
        if (DropDownPreference.class.equals(preference.getClass())) {
            return Optional.of(join(((DropDownPreference) preference).getEntries()));
        }
        if (MultiSelectListPreference.class.equals(preference.getClass())) {
            return Optional.of(join(((MultiSelectListPreference) preference).getEntries()));
        }
        return Optional.empty();
    }

    private static String join(final CharSequence[] charSequences) {
        return charSequences == null ?
                "" :
                String.join(", ", charSequences);
    }
}
