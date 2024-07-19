package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.preferencesearch.R;
import de.KnollFrank.preferencesearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
    }

    @Override
    public void onDisplayPreferenceDialog(final @NonNull Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getChildFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
