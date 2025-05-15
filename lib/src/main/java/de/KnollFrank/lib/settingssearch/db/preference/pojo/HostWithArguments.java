package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.ColumnInfo;

import java.util.Optional;

public record HostWithArguments(
        @ColumnInfo(name = HOST)
        Class<? extends PreferenceFragmentCompat> host,

        // FK-tODO: remove BundleWithEquality, remove gson
        @ColumnInfo(name = ARGUMENTS)
        Optional<BundleWithEquality> arguments) {

    public static final String HOST = "host";
    public static final String ARGUMENTS = "arguments";

    public static HostWithArguments of(final PreferenceFragmentCompat preferenceFragment) {
        return new HostWithArguments(
                preferenceFragment.getClass(),
                Optional
                        .ofNullable(preferenceFragment.getArguments())
                        .map(BundleWithEquality::new));
    }
}
