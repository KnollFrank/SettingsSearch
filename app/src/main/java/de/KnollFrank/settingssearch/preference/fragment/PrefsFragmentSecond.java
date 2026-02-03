package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markExtrasOfPreferenceConnectingSrcWithDst;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.settingssearch.R;

public class PrefsFragmentSecond extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences2);
        final Preference preference = getPreferenceScreen().findPreference("link_to_third_fragment");
        copySrcToDst(Optional.ofNullable(getArguments()), preference.getExtras());
        markExtrasOfPreferenceConnectingSrcWithDst(preference, this, PrefsFragmentThird.class);
    }

    private static void copySrcToDst(final Optional<Bundle> src, final Bundle dst) {
        src.ifPresent(dst::putAll);
    }
}
