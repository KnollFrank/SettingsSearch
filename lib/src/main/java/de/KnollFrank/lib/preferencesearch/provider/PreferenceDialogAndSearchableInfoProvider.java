package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceDialogAndSearchableInfoProvider {

    Optional<PreferenceDialogAndSearchableInfoByPreferenceDialog> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(PreferenceFragmentCompat hostOfPreference, Preference preference);
}
