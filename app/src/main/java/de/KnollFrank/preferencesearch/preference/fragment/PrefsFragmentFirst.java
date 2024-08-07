package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.preferencesearch.R;
import de.KnollFrank.preferencesearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String SUMMARY_OF_SRC_PREFERENCE = "summaryOfSrcPreference";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createPreferenceConnectedToPreferenceFragmentWithSinglePreference());
        final Preference preference = findPreference("keyOfPreferenceWithOnPreferenceClickListener");
        preference.setOnPreferenceClickListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if ("keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        return false;
    }

    private @NonNull Preference createPreferenceConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(getContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference from src to dst");
        preference.setKey("keyOfSrcPreference");
        final String summary = "summary of src preference";
        preference.setSummary(summary);
        preference.getExtras().putString(SUMMARY_OF_SRC_PREFERENCE, summary);
        return preference;
    }
}
