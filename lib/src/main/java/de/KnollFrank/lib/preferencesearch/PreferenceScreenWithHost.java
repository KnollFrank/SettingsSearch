package de.KnollFrank.lib.preferencesearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

public class PreferenceScreenWithHost {

    public final PreferenceScreen preferenceScreen;
    public final Class<? extends PreferenceFragmentCompat> host;

    public PreferenceScreenWithHost(final PreferenceScreen preferenceScreen,
                                    final Class<? extends PreferenceFragmentCompat> host) {
        this.preferenceScreen = preferenceScreen;
        this.host = host;
    }

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
