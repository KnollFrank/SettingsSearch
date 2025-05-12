package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.markFragmentInstance;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.settingssearch.R;

public class PrefsFragmentSecond extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences2);
        final Optional<Bundle> arguments = Optional.ofNullable(getArguments());
        final Bundle extras =
                getPreferenceScreen()
                        .findPreference("link_to_third_fragment")
                        .getExtras();
        arguments.ifPresent(extras::putAll);
        markFragmentInstance(extras, PrefsFragmentThird.class);
    }
}
