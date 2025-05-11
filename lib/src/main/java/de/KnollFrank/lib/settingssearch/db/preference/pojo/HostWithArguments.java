package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public record HostWithArguments(Class<? extends PreferenceFragmentCompat> host,
                                // FK-TODO: use Optional<BundleWithEqualsAndHashCode>
                                Optional<Bundle> arguments) {
}
