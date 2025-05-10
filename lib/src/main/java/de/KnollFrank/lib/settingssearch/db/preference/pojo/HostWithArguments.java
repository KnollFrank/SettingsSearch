package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.Optional;

public record HostWithArguments(Class<? extends PreferenceFragmentCompat> host,
                                Optional<Bundle> arguments) {

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        final HostWithArguments that = (HostWithArguments) obj;
        return equalArguments(this.arguments(), that.arguments()) && Objects.equals(host(), that.host());
    }

    // FK-TODO: does not conform to contract of equals() & hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(host(), arguments());
    }

    private static boolean equalArguments(final Optional<Bundle> arguments1, final Optional<Bundle> arguments2) {
        return arguments1.isEmpty() && arguments2.isEmpty() ||
                arguments1.isPresent()
                        && arguments2.isPresent()
                        && BundleEquality.equalBundles(arguments1.orElseThrow(), arguments2.orElseThrow());
    }
}
