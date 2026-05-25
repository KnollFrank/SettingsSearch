package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

@FunctionalInterface
public interface PreferenceDialogAndSearchableInfoProvider {

    Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<?>> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(Preference preference, Fragment hostOfPreference);
}
