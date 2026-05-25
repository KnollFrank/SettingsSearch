package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

import java.util.Optional;

public class DefaultPreferenceFragmentIdProvider implements PreferenceFragmentIdProvider {

    @Override
    public String getId(final Fragment preferenceFragment) {
        final String className = preferenceFragment.getClass().getName();
        return Optional
                .ofNullable(preferenceFragment.getArguments())
                .map(arguments -> className + " " + arguments)
                .orElse(className);
    }
}
