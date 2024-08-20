package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

public class PreferenceScreenWithHost {

    public final PreferenceScreen preferenceScreen;
    public final PreferenceFragmentCompat host;

    public PreferenceScreenWithHost(final PreferenceScreen preferenceScreen,
                                    final PreferenceFragmentCompat host) {
        this.preferenceScreen = preferenceScreen;
        this.host = host;
    }

    public static PreferenceScreenWithHost fromPreferenceFragment(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceScreenWithHost that = (PreferenceScreenWithHost) o;
        return Objects.equals(preferenceScreen, that.preferenceScreen) && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferenceScreen, host);
    }

    @Override
    public String toString() {
        return "PreferenceScreenWithHost{" +
                "preferenceScreen=" + preferenceScreen +
                ", host=" + host +
                '}';
    }
}
