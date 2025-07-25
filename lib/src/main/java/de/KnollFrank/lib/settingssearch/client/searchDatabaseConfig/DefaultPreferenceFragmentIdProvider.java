package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class DefaultPreferenceFragmentIdProvider implements PreferenceFragmentIdProvider {

    @Override
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        return preferenceFragment.getClass().getName() + getArguments(preferenceFragment);
    }

    private static String getArguments(final PreferenceFragmentCompat preferenceFragment) {
        return Optional
                .ofNullable(preferenceFragment.getArguments())
                .map(Bundle::toString)
                .orElse("");
    }
}
