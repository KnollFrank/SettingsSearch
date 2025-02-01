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
    // FK-TODO: ersetze den String fragmentClassName durch ein Class-Object mit Typangabe PreferenceFragmentCompat.
    public Fragment instantiate(final String fragmentClassName,
                                final Optional<PreferenceWithHost> src,
                                final Context context,
                                final Fragments fragments) {
        // FK-TODO: setzte die Punkte in der @deprecated-Warnung der folgenden Methode um.
        return Fragment.instantiate(context, fragmentClassName, peekExtrasOfPreference(src));
    }

    private static @Nullable Bundle peekExtrasOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .map(PreferenceWithHost::preference)
                .map(Preference::peekExtras)
                .orElse(null);
    }
}
