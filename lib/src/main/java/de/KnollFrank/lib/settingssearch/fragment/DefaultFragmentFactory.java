package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Classes;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public <T extends Fragment> T instantiate(final Class<T> fragmentClass,
                                              final Optional<PreferenceOfHostOfActivity> src,
                                              final Context context,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        return Classes.instantiateFragmentClass(fragmentClass, peekExtrasOfPreference(src));
    }

    private static Optional<Bundle> peekExtrasOfPreference(final Optional<PreferenceOfHostOfActivity> preferenceWithHost) {
        return preferenceWithHost
                .map(PreferenceOfHostOfActivity::preference)
                .map(Preference::peekExtras);
    }
}
