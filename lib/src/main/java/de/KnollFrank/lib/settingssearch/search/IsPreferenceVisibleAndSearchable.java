package de.KnollFrank.lib.settingssearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

class IsPreferenceVisibleAndSearchable implements IsPreferenceSearchable {

    private final IsPreferenceSearchable delegate;

    public IsPreferenceVisibleAndSearchable(final IsPreferenceSearchable delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isPreferenceOfHostSearchable(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return preference.isVisible() && delegate.isPreferenceOfHostSearchable(preference, hostOfPreference);
    }
}
