package de.KnollFrank.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

record PreferenceScreenWithHost(PreferenceScreen preferenceScreen,
                                Class<? extends PreferenceFragmentCompat> host) {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceScreenWithHost that = (PreferenceScreenWithHost) o;
        return Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host);
    }
}
