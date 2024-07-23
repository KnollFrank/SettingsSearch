package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.base.Function;

import java.util.Optional;

// FK-TODO: remove this class completely, no inling, no clickability of Preferences needed any more
class ClickablePreferenceProvider {

    private final PreferenceFragmentCompat preferenceFragment;

    public static ClickablePreferenceProvider create(final Class<? extends PreferenceFragmentCompat> classOfPreferenceFragment,
                                                     final Function<String, Fragment> instantiateAndInitializeFragment) {
        return new ClickablePreferenceProvider((PreferenceFragmentCompat) instantiateAndInitializeFragment.apply(classOfPreferenceFragment.getName()));
    }

    public ClickablePreferenceProvider(final PreferenceFragmentCompat preferenceFragment) {
        this.preferenceFragment = preferenceFragment;
    }

    public Optional<Preference> asClickablePreference(final Preference preference) {
        return reloadPreference(Optional.ofNullable(preference.getKey()));
    }

    private Optional<Preference> reloadPreference(final Optional<String> key) {
        return key.map(preferenceFragment::findPreference);
    }
}
