package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record HostWithArguments(Class<? extends PreferenceFragmentCompat> host,
                                Optional<BundleWithEquality> arguments) {

    public static HostWithArguments of(final PreferenceFragmentCompat preferenceFragment) {
        return new HostWithArguments(
                preferenceFragment.getClass(),
                Optional
                        .ofNullable(preferenceFragment.getArguments())
                        .map(BundleWithEquality::new));
    }
}
