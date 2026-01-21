package de.KnollFrank.lib.settingssearch.fragment.factory;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;

class ArgumentsFactory {

    private ArgumentsFactory() {
    }

    public static Arguments createArguments(final Class<? extends Fragment> fragmentClass,
											final Optional<PreferenceWithHost> preferenceWithHost) {
		return new Arguments(
				fragmentClass,
				getKeyOfPreference(preferenceWithHost),
				getHostOfPreference(preferenceWithHost));
	}

	private static Optional<String> getKeyOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
		return preferenceWithHost
				.map(PreferenceWithHost::preference)
				.map(Preference::getKey);
	}

	private static Optional<PreferenceFragmentCompat> getHostOfPreference(final Optional<PreferenceWithHost> preferenceWithHost) {
		return preferenceWithHost.map(PreferenceWithHost::host);
	}
}
