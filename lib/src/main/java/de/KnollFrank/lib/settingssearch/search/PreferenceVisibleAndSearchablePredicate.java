package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

public class PreferenceVisibleAndSearchablePredicate implements PreferenceSearchablePredicate {

    private final PreferenceSearchablePredicate delegate;

    public PreferenceVisibleAndSearchablePredicate(final PreferenceSearchablePredicate delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isPreferenceSearchable(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return preference.isVisible() && delegate.isPreferenceSearchable(preference, hostOfPreference);
    }
}
