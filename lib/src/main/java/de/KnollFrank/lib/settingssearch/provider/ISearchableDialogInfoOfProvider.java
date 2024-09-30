package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface ISearchableDialogInfoOfProvider {

    Optional<String> getSearchableDialogInfoOfPreference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
