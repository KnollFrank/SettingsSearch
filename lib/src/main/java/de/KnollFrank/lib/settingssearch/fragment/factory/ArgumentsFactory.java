package de.KnollFrank.lib.settingssearch.fragment.factory;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

class ArgumentsFactory {

    public static Arguments createArguments(final String fragmentClassName,
                                            final Optional<PreferenceWithHost> preferenceWithHost) {
        return new Arguments(
                fragmentClassName,
                getKeyOfPreference(preferenceWithHost),
                getHostOfPreference(preferenceWithHost));
    }

    private static Optional<String> getKeyOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost
                .map(_preferenceWithHost -> _preferenceWithHost.preference)
                .map(Preference::getKey);
    }

    private static Optional<Class<? extends PreferenceFragmentCompat>> getHostOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
        return preferenceWithHost.map(_preferenceWithHost -> _preferenceWithHost.host.getClass());
    }
}
