package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record HostWithArguments(Class<? extends PreferenceFragmentCompat> host,
                                Optional<BundleWithEquality> arguments) {
}
