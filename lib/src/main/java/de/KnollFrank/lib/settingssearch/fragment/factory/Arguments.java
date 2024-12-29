package de.KnollFrank.lib.settingssearch.fragment.factory;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

record Arguments(String fragmentClassName,
                 Optional<String> keyOfPreference,
                 Optional<PreferenceFragmentCompat> hostOfPreference) {
}
