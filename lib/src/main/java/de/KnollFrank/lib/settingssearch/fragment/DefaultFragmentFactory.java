package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.common.Classes;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public <T extends Fragment> T instantiate(final Class<T> fragmentClass,
                                              final Optional<PreferenceOfHost> src,
                                              final Context context,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return Classes.instantiateFragmentClass(fragmentClass, peekExtrasOfPreference(src));
    }

    private static Optional<Bundle> peekExtrasOfPreference(final Optional<PreferenceOfHost> preferenceWithHost) {
        return preferenceWithHost
                .map(PreferenceOfHost::preference)
                .map(Preference::peekExtras);
    }
}
