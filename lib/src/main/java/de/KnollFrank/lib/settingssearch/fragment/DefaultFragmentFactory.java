package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    // FK-TODO: ersetze Class<? extends Fragment> durch Class<? extends PreferenceFragmentCompat>?
    public Fragment instantiate(final Class<? extends Fragment> fragmentClass,
                                final Optional<PreferenceWithHost> src,
                                final Context context,
                                final Fragments fragments) {
        return FragmentHelper.instantiateFragmentClass(fragmentClass, peekExtrasOfPreference(src));
    }

    private static Optional<Bundle> peekExtrasOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .map(PreferenceWithHost::preference)
                .map(Preference::peekExtras);
    }
}
