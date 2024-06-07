package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceWrapper implements IPreferenceItem {

    public final Preference preference;

    public PreferenceWrapper(Preference preference) {
        this.preference = preference;
    }

    @Override
    public boolean matches(final String keyword) {
        return asPreferenceItem(preference).matches(keyword);
    }

    private static PreferenceItem asPreferenceItem(final Preference preference) {
        return PreferenceItems.getPreferenceItem(preference, PreferenceFragmentCompat.class);
    }
}
