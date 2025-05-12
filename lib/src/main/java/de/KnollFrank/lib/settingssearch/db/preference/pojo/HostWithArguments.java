package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record HostWithArguments(@NonNull Class<? extends PreferenceFragmentCompat> host,
                                @NonNull Optional<BundleWithEquality> arguments) {

    public static HostWithArguments of(final PreferenceFragmentCompat preferenceFragment) {
        return new HostWithArguments(
                preferenceFragment.getClass(),
                Optional
                        .ofNullable(preferenceFragment.getArguments())
                        .map(BundleWithEquality::new));
    }
}
