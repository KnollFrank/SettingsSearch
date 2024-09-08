package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

public class DefaultFragmentFactory implements FragmentFactory {

    @Override
    public Fragment instantiate(final String fragmentClassName, Optional<PreferenceWithHost> src, final Context context) {
        return Fragment.instantiate(context, fragmentClassName, getExtrasOfPreference(src));
    }

    private static @Nullable Bundle getExtrasOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .map(PreferenceWithHost::preference)
                .map(Preference::getExtras)
                .orElse(null);
    }
}
